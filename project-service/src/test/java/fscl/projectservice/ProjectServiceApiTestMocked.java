package fscl.projectservice;

import fscl.core.api.Response;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.CodeFormat;
import fscl.projectservice.api.RetrieveProjectsResponse;
import fscl.projectservice.api.NewProjectCodeRequest;
import fscl.projectservice.api.NewProjectCodeResponse;
import fscl.projectservice.adapters.db.ProjectRepository;
import fscl.projectservice.adapters.db.ProjectCodeCacheRepository;
import fscl.projectservice.domain.Project;
import fscl.projectservice.domain.IdRegistration;
import fscl.projectservice.api.ProjectData;
import fscl.projectservice.api.CreateProjectRequest;
import org.junit.runner.RunWith;
import org.junit.Test;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProjectServiceApiTestMocked {
	
	/**
	 * Unfortunately, HierarchicalContextRunner apparently cannot be used together with 
	 * @MockBean, so we cannot apply the nested classed pattern. Therefore, we're applying
	 * the ridiculously long method name pattern :-)
	 */
		
	@MockBean
	private ProjectRepository projectRepo;
	
	@MockBean
	private ProjectCodeCacheRepository codeCacheRepo; 
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	private UUID bob = UUID.randomUUID();
	private UUID sharon= UUID.randomUUID();
		
	private final String datePartOfCode = LocalDate.now().format( DateTimeFormatter.ofPattern("yyyyMMdd"));
	private final int cacheTimeOutSeconds = 1000;
	
	private CodeFormat codeCfg = new CodeFormat("=", ".");
	
	// choose a common reference date for consistency		 
	private final LocalDateTime expirationExpired = LocalDateTime.now();
	private final LocalDateTime expirationValid = expirationExpired
			.plusSeconds(this.cacheTimeOutSeconds);
			
	
		
	@Test
	public void GivenProjectList_WhenRetrievingProjects_ShouldRetrieveAll() {
				
		// GIVEN ----------------------------------------------------------------------
		// a list of projects
		List<Project> expected = new ArrayList<Project>();
		expected.add(new Project(new ProjectCode("test.code.001"), "test.name.001", "test.description.001"));
		expected.add(new Project(new ProjectCode("test.code.002"), "test.name.002", "test.description.002"));
		expected.add(new Project(new ProjectCode("test.code.003"), "test.name.003", "test.description.003"));
		expected.add(new Project(new ProjectCode("test.code.004"), "test.name.004", "test.description.004"));		
		given(projectRepo.findAll()).willReturn(expected);
		
		// WHEN -----------------------------------------------------------------------
		ResponseEntity<RetrieveProjectsResponse> response = this.restTemplate.getForEntity(
				"/api/v4/projects", 
				RetrieveProjectsResponse.class);
		
		// THEN
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.hasBody());
		ProjectData[] respData = response.getBody().getProjects();
		List<Project> received = new ArrayList<Project>();
		for(int i = 0; i < respData.length; i++) {
			received.add(respData[i].asProject());
		}
		assertThat(received.containsAll(expected));		
	}	
		
	@Test
	public void GivenEmptyProjectList_WhenRetrievingProjects_ShouldRetrieveNone() {
		// GIVEN ----------------------------------------------------------------------
		// an empty list
		given(projectRepo.findAll()).willReturn(new ArrayList<Project>()); 
		
		// WHEN -----------------------------------------------------------------------
		ResponseEntity<RetrieveProjectsResponse> response = this.restTemplate.getForEntity(
				"/api/v4/projects", 
				RetrieveProjectsResponse.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.hasBody());
		ProjectData[] respData = response.getBody().getProjects();
		assertThat(respData.length == 0);
	}
			
	@Test
	public void GivenValidCachedCodes_WhenCachingNewCodeForClient_ShouldSucceedWithIncrement() {
		
		// GIVEN ----------------------------------------------------------------------
		List<IdRegistration> bobsCodes = this.setUpCachedCodesForClient(this.bob, 1, false);		
		given(codeCacheRepo.findByClientId(bob)).willReturn(bobsCodes);
		
		IdRegistration expected = new IdRegistration(this.datePartOfCode+"-004", this.bob, 100);
		
		// WHEN -----------------------------------------------------------------------
		NewProjectCodeRequest bobsRequest = new NewProjectCodeRequest(100, this.bob);
		ResponseEntity<NewProjectCodeResponse> bobsResponse = this.restTemplate.postForEntity(
				"/api/v4/projects/newidrequest",
				bobsRequest,				
				NewProjectCodeResponse.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(bobsResponse.getStatusCode().is2xxSuccessful());
		assertThat(bobsResponse.hasBody());		
		assertThat(bobsResponse.getBody().getCode().equals(expected.getCode()));
	
	}	
	
	@Test
	public void GivenExpiredCachedCodes_WhenCalledOverlapping_ShouldSucceedWithIncrement() {

		// GIVEN Bob and Sharon both preregistered overlapping codes ----------------- 
		List<IdRegistration> bobsCodes = this.setUpCachedCodesForClient(this.bob, 1, true);
		List<IdRegistration> sharonsCodes = this.setUpCachedCodesForClient(this.sharon, 2, true);
		given(codeCacheRepo.findByClientId(bob)).willReturn(bobsCodes);
		given(codeCacheRepo.findByClientId(sharon)).willReturn(sharonsCodes);
		
		// WHEN Bob calls for a new code ----------------------------------------------
		NewProjectCodeRequest bobsRequest = new NewProjectCodeRequest(100, this.bob);
		ResponseEntity<NewProjectCodeResponse> bobsResponse = this.restTemplate.postForEntity(
				"/api/v4/projects/newidrequest",
				bobsRequest,				
				NewProjectCodeResponse.class);
						
		// THEN -----------------------------------------------------------------------
		assertThat(bobsResponse.getStatusCode().is2xxSuccessful());
		assertThat(bobsResponse.hasBody());		
		assertThat(bobsResponse.getBody().getCode().equals(datePartOfCode + "-004"));
		
	}
	
	@Test
	public void GivenPristineDB_WhenUserCreatesProject_ShouldSucceed() {
		
		// GIVEN ----------------------------------------------------------------------		
		CreateProjectRequest requested = new CreateProjectRequest(
				datePartOfCode + "-001", 
				"test-name-001", 
				"test-description-001",
				this.bob,
				codeCfg,
				codeCfg,
				codeCfg,
				codeCfg);
		given(this.projectRepo.findByCode(requested.getCode().toString())).willReturn(null);
		
		// WHEN Bob creates a project ------------------------------------------------
		ResponseEntity<Response> bobsResponse = this.restTemplate.postForEntity(
				"/api/v4/projects/new", 
				requested, 
				Response.class);
		
		// THEN he should succeed ---------------------------------------------------
		assertThat(bobsResponse.getStatusCode().is2xxSuccessful());
		assertThat(bobsResponse.hasBody());
		assertThat(bobsResponse.getBody().getError()).isEmpty();
	}
	
	@Test
	public void GivenCollidingProjectInDB_WhenUserCreatesProject_ShouldFail() {
		// GIVEN ----------------------------------------------------------------------		
		CreateProjectRequest request = new CreateProjectRequest(
				datePartOfCode + "-001", 
				"test-name-001", 
				"test-description-001",
				this.bob,
				codeCfg,
				codeCfg,
				codeCfg,
				codeCfg);
		Project colliding = new Project(
				request.getCode(),
				request.getName(),
				request.getDescription());
		given(this.projectRepo.findByCode(request.getCode().toString())).willReturn(colliding);
		
		// WHEN -----------------------------------------------------------------------
		ResponseEntity<Response> bobsResponse = this.restTemplate.postForEntity(
				"/api/v4/projects/new", 
				request, 
				Response.class);
		// THEN -----------------------------------------------------------------------
		assertThat(bobsResponse.getStatusCode().is4xxClientError());
		assertThat(bobsResponse.getBody().getError()).contains(request.getCode().toString());
		
	}
	
	@Test
	public void GivenValidCodesCached_WhenCreatingCollidingProject_ShouldFail() {
		
		String collidingCode = datePartOfCode + "-004";
		
		// GIVEN Bob and Sally both preregistered overlapping codes...
		// ... where Bobs codes are expired,
		List<IdRegistration> bobsCodes = new ArrayList<IdRegistration>();
		bobsCodes.add(new IdRegistration(collidingCode, this.bob, this.expirationExpired));
		// ... but Sharons are not.
		List<IdRegistration> sharonsCodes = new ArrayList<IdRegistration>();
		sharonsCodes.add(new IdRegistration(collidingCode, this.sharon, this.expirationValid ));
				
		List<IdRegistration> allCodes = new ArrayList<IdRegistration>(bobsCodes);
		allCodes.addAll(sharonsCodes);		
		given(codeCacheRepo.findByCode(collidingCode)).willReturn(allCodes);

		// WHEN Bob calls for creating a new project		
		CreateProjectRequest bobsRequest = new CreateProjectRequest(
				collidingCode,
				"test-name-004", 
				"Rather long and elaborate test-description-004 sentence",
				this.bob,
				codeCfg,
				codeCfg,
				codeCfg,
				codeCfg);
		
		ResponseEntity<Response> bobsResponse = this.restTemplate.postForEntity(
				"/api/v4/projects/new", 
				bobsRequest, 
				Response.class);
		
		// THEN he should be receiving an error
		assertThat(bobsResponse.getStatusCode().is4xxClientError());
		assertThat(bobsResponse.hasBody());	
		
		String rejectedCode = bobsRequest.getCode().toString();
		assertThat(bobsResponse.getBody().getError()).contains(rejectedCode);
		assertThat(bobsResponse.getBody().getError()).contains("locked");
		assertThat(bobsResponse.getBody().getError()).contains("client");
		
	}
	
	@Test
	public void GivenTimedoutCollidingCodesCached_WhenCreatingProject_ShouldSucceed() {

		String collidingCode = datePartOfCode + "-004";
		
		// GIVEN Bob and Sharon both preregistered overlapping codes...		
		// ... where Bobs codes are expired,
		List<IdRegistration> bobsCodes = new ArrayList<IdRegistration>();
		bobsCodes.add(new IdRegistration(collidingCode, this.bob, this.expirationExpired));
		// ... and Sharons are as well.
		List<IdRegistration> sharonsCodes = new ArrayList<IdRegistration>();
		sharonsCodes.add(new IdRegistration(collidingCode, this.sharon, this.expirationExpired ));
		
		List<IdRegistration> allCodes = new ArrayList<IdRegistration>(bobsCodes);
		allCodes.addAll(sharonsCodes);		
		given(codeCacheRepo.findByCode(collidingCode)).willReturn(allCodes);
		
		// WHEN Bob calls for creating a new project		
		CreateProjectRequest bobsRequest = new CreateProjectRequest(
				collidingCode,
				"test-name-004", 
				"Rather long and elaborate test-description-004 sentence",
				this.bob,
				codeCfg,
				codeCfg,
				codeCfg,
				codeCfg);
		
		ResponseEntity<Response> bobsResponse = this.restTemplate.postForEntity(
				"/api/v4/projects/new", 
				bobsRequest, 
				Response.class);
		
		// THEN he should succeed
		assertThat(bobsResponse.getStatusCode().is2xxSuccessful());
		assertThat(bobsResponse.hasBody());
		assertThat(bobsResponse.getBody().getError()).isEmpty();
	}
	
	@Test
	public void GivenProjectsInDB_WhenDeletingMatchingProject_ShouldSucceed() {
		
		String deleteeCode = "20191030-034";
		
		// GIVEN ----------------------------------------------------------------------
		Project deletee = new Project(new ProjectCode(deleteeCode), "deletee", "to be deleted, sob");
		given(this.projectRepo.findByCode(deleteeCode)).willReturn(deletee);
		
		// WHEN -----------------------------------------------------------------------
		String url = "/api/v4/projects/" + deleteeCode;
		ResponseEntity<Response> response = this.restTemplate.exchange(
				url, 
				HttpMethod.DELETE, 
				null, 
				Response.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.hasBody());
		assertThat(response.getBody().getError()).isEmpty();
	}
	
	@Test
	public void GivenProjectsInDB_WhenDeletingUnmatchedProject_ShouldFail() {
		String deleteeCode = "20191030-034";
		String unmatchingCode = "20191030-039";
		
		// GIVEN ----------------------------------------------------------------------
		Project deletee = new Project(new ProjectCode(deleteeCode), "deletee", "to be deleted, sob");
		given(this.projectRepo.findByCode(deleteeCode)).willReturn(deletee);
		
		// WHEN -----------------------------------------------------------------------
		String url = "/api/v4/projects/" + unmatchingCode;
		ResponseEntity<Response> response = this.restTemplate.exchange(
				url, 
				HttpMethod.DELETE, 
				null, 
				Response.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(response.getStatusCode().is4xxClientError());
		assertThat(response.hasBody());
		assertThat(response.getBody().getError()).contains(unmatchingCode);
		assertThat(response.getBody().getError()).contains("not found");		
	}
	
	@Test
	public void GivenProjectsInDB_WhenUpdatingMatchingProject_ShouldSucceed() {
		
		// GIVEN ----------------------------------------------------------------------
		String updateeCode = "20191030-034";
		ProjectData updateeData = new ProjectData(updateeCode, "updatee", "up, up we date !");
		given(this.projectRepo.findByCode(updateeCode)).willReturn(updateeData.asProject());
		
		// WHEN -----------------------------------------------------------------------
		String url = "/api/v4/projects/" + updateeCode;
		ResponseEntity<Response> response = this.restTemplate.exchange(
				url, 
				HttpMethod.PUT, 
				new HttpEntity<>(updateeData), 
				Response.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(response.getStatusCode().is2xxSuccessful());
		assertThat(response.hasBody());
		assertThat(response.getBody().getError()).isEmpty();
	}
	
	@Test
	public void GivenProjectsInDB_WhenUpdatingUnmatchedProject_ShouldFail() {
		
		// GIVEN ----------------------------------------------------------------------
		String unmatchingCode = "20191030-039";		
		given(this.projectRepo.findByCode(unmatchingCode)).willReturn(null);
		
		// WHEN -----------------------------------------------------------------------
		String url = "/api/v4/projects/" + unmatchingCode;
		ProjectData updateeData = new ProjectData(unmatchingCode, "updatee", "up, up we date !");
		ResponseEntity<Response> response = this.restTemplate.exchange(
				url, 
				HttpMethod.PUT, 
				new HttpEntity<>(updateeData), 
				Response.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(response.getStatusCode().is4xxClientError());
		assertThat(response.hasBody());
		assertThat(response.getBody().getError()).contains(unmatchingCode);
		assertThat(response.getBody().getError()).contains("not found");	

	}
	
	@Test
	public void GivenProjectsInDB_WhenUpdatingProjectWithRequestMismatch_ShouldFail() {
		
		// GIVEN ----------------------------------------------------------------------
		String updateeCode = "20191030-034";
		ProjectData updateeData = new ProjectData(updateeCode, "updatee", "up, up we date !");
		given(this.projectRepo.findByCode(updateeCode)).willReturn(updateeData.asProject());
		
		// WHEN -----------------------------------------------------------------------
		String unmatchingCode = "20191030-039";
		String url = "/api/v4/projects/" + unmatchingCode;
		ResponseEntity<Response> response = this.restTemplate.exchange(
				url, 
				HttpMethod.PUT, 
				new HttpEntity<>(updateeData), 
				Response.class);
		
		// THEN -----------------------------------------------------------------------
		assertThat(response.getStatusCode().is4xxClientError());
		assertThat(response.hasBody());
		assertThat(response.getBody().getError()).contains("mismatching resource code");
	}
		
	private List<IdRegistration> setUpCachedCodesForClient(
			UUID client, 
			int startcode, 
			boolean expired) {
		
		List<IdRegistration> codes = new ArrayList<IdRegistration>();
		
		int endcode = startcode + 3;
		for(int i = startcode; i < endcode; i++) {
			String code = datePartOfCode + String.format("-%03d", i);
			if(!expired) {
				codes.add(new IdRegistration(code, client, this.expirationExpired));						
			} else {
				codes.add(new IdRegistration(code, client, this.expirationValid));
			}
		}	return codes;
	}
}
