package fscl.functionservice.domain;

import fscl.core.db.IdRegistrationRepository;
import fscl.core.db.NoSuchCodedItemException;
import fscl.core.domain.ProjectCode;
import fscl.core.domain.EntityId;
import fscl.core.domain.EntityCode;
import fscl.core.domain.EntityContent;
import fscl.core.domain.CodeFormat;
import fscl.core.domain.registration.IdRegistration;
import fscl.core.domain.registration.CollidingClientForRegistrationException;
import fscl.core.domain.registration.NoSuchRegistrationException;
import fscl.function.service.adapters.db.FunctionRepository;
import fscl.function.service.adapters.db.ProjectRepository;
import fscl.function.service.domain.Function;
import fscl.function.service.domain.FunctionIdManager;
import fscl.project.foreignkeys.Project;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FunctionIdManagerFastUnitTest {
	
	@Autowired
	FunctionIdManager SUT;
	
	@MockBean
	IdRegistrationRepository registry;
		
	@MockBean
	FunctionRepository functionRepo;
	
	@MockBean
	ProjectRepository projectRepo;	
	
		
	/**
	 * Note all @Nested test classes have short-form names to avoid 
	 * the *.class file from breaking the OS file name length limit. 
	 * Refer to the @DisplayName annotations for clarity  
	 */
	@Nested	
	@DisplayName("Given Project, Clients and Roots")
	class A { 
		
		//
		// project
		//
		ProjectCode p1Code = new ProjectCode("T001-001");
		CodeFormat functionFormat = new CodeFormat("=", ".");		
		Project p1 = new Project(
				p1Code.toString(), 
				functionFormat,
				null, null, null);
		
		//
		// clients
		//
		UUID clientA = UUID.randomUUID();
		UUID clientB = UUID.randomUUID();
		
		
		//
		// entities
		//
		private EntityId id1 = new EntityId(
				p1Code, 
				new EntityCode("=001", functionFormat));
		private EntityId id2 = new EntityId(
				p1Code, 
				new EntityCode("=002", functionFormat));
		private EntityId id3 = new EntityId(
				p1Code, 
				new EntityCode("=003", functionFormat));
		private EntityId id4 = new EntityId(
				p1Code, 
				new EntityCode("=004", functionFormat));
		
		protected abstract class WhenCreating {
			
			protected EntityId grantabletoA, grantabletoB;
			
			WhenCreating(String idGrantableToA, String idGrantableToB) {
				
				this.grantabletoA = new EntityId(p1Code, new EntityCode(idGrantableToA));
				// to ensure equals() will be true when called with outer-class entities
				this.grantabletoA.entity.init(functionFormat);
										
				grantabletoB = new EntityId(p1Code, new EntityCode(idGrantableToB));
				this.grantabletoB.entity.init(functionFormat);
			}
		}
		
		protected abstract class GivenRegs {
				
			protected Long timeout = 20000L;
			protected IdRegistration regFromA;
			protected IdRegistration regFromB;				
			protected List<IdRegistration> registrations = new ArrayList<IdRegistration>();
			
			protected GivenRegs(EntityId idA, EntityId idB) {
				regFromA = new IdRegistration(	idA, clientA, this.timeout);
				regFromB = new IdRegistration(	idB, clientB, this.timeout);
			}
			
			protected GivenRegs() {				
			}
		}
		
		protected abstract class GivenExpiredRegs extends GivenRegs {
			
			protected GivenExpiredRegs(EntityId idA, EntityId idB) {
				super.timeout = 0L;
				super.regFromA = new IdRegistration(	idA, clientA, super.timeout);
				super.regFromB = new IdRegistration(	idB, clientB, super.timeout);
				
			}
		}
		
		protected class TestHelper {
		
			void createAndSave (
				ProjectCode	project,
				String	parentEntityCode,
				String	entityCodeExpected,
				UUID 	clientId,
				CodeFormat format ) throws NoSuchCodedItemException  {
			
				ArgumentCaptor<IdRegistration> argCap 
				= ArgumentCaptor.forClass(IdRegistration.class);
				
				IdRegistration capturedArg;
				EntityId expected = new EntityId(project, new EntityCode(entityCodeExpected, format));		
				Long timeout = 2000L;
				
				EntityId actual = SUT.createRegisteredId(
						project, 
						parentEntityCode, 
						clientId, 
						timeout);
				
				assertEquals(expected, actual);
				verify(registry,times(1)).save(argCap.capture());	
				capturedArg = argCap.getValue();
				assertTrue(capturedArg.getId().equals(expected));
				assertTrue(capturedArg.getClientId().equals(clientId));
				
				assertEquals(EntityCode.STATE.INITIALIZED, actual.entity.getState());
			}
		
			/**
			 * call IdRegistrationManager.grantRegisteredId(..) and verify
			 * 		1) 	delete(..) has been called on the registration registry 
			 * 			for an appropriate IdRegistration instance 
			 * 		2) 	none of the Exceptions has been thrown
			 * 
			 * @param toBeGranted	
			 * @param toClient
			 */
			protected void grantAndDelete(				
					EntityId toBeGranted,
					UUID 	toClient,
					UUID 	clientFromRegistration) {
				
				ArgumentCaptor<IdRegistration> argCap
					= ArgumentCaptor.forClass(IdRegistration.class);
				IdRegistration capturedArg;
			
				try {
					
					SUT.grantRegisteredId(toBeGranted, toClient);				
					verify(registry, times(1)).delete(argCap.capture());	
					capturedArg = argCap.getValue();
					assertTrue(capturedArg.getId().equals(toBeGranted));
					assertTrue(capturedArg.getClientId().equals(clientFromRegistration));	
					
				} catch(NoSuchRegistrationException e) {
					assertTrue(
						false, 
						"should'nt be catching a NoSuchRegistrationException here !");
				} catch(CollidingClientForRegistrationException e) {
					assertTrue(
						false, 
						"should'nt be catching a CollidingClientForRegistrationException here !");
				}
				
			}
			
			protected void rejectForCollisionNoDelete(
				EntityId expected,			
				UUID 	toClient) {
				
							
				CollidingClientForRegistrationException e = 
					assertThrows(CollidingClientForRegistrationException.class, () -> {
						SUT.grantRegisteredId(expected, toClient);
					});
							
				assertEquals(expected, e.getRegistration().getId());
				assertNotEquals(e.getRegistration().getClientId(), toClient);
				verify(registry, never()).delete(any(IdRegistration.class));
				
			}
			
			protected void rejectForUnregisteredNoDelete(
				EntityId expected,			
				UUID 	toClient) {
				
							
				NoSuchRegistrationException e = 
					assertThrows(NoSuchRegistrationException.class, () -> {
						SUT.grantRegisteredId(expected, toClient);
					});
							
				assertEquals(expected, e.getId());			
				verify(registry, never()).delete(any(IdRegistration.class));
				
			}
		}
		
		@BeforeEach
		void setup() {
			when(projectRepo.findByCode(p1Code.toString()))
				.thenReturn(p1);
		}
		
		@Nested
		@DisplayName("When Root Requested")
		class AB1 {
			
			String parentCode = null;
						
			@Nested
			@DisplayName("Given Siblings Committed")
			class AB1C1 {
				
				private EntityContent content1 = new EntityContent("root1", "a root");
				private Function root1 = new Function(id1, null, content1);
				private EntityContent content2 = new EntityContent("root2", "a root");
				private Function root2 = new Function(id2, null, content2);
									
				private List<Function> siblings;
				
				@BeforeEach
				void setup() {
					
					siblings = new ArrayList<Function>();
					siblings.add(root1);
					siblings.add(root2);
					
					when(functionRepo.findRootsByProjectCode(p1Code.toString()))
						.thenReturn(siblings);					
				}
																
				@Nested
				@DisplayName("Given Unexpired Registrations")
				class AB1C1D1 extends GivenRegs {
					
					AB1C1D1() {
						super(id3, id4);
					}
					
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB1C1D1E1 {					
																		
						@Test
						@DisplayName("Then Should Register A With One Up And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=005",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With One Up And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=005",
									clientB,
									functionFormat);
						}
					}
						
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB1C1D1E2 extends WhenCreating {
						
						AB1C1D1E2() {
							super("=003", "=004");
						}
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id3)).thenReturn(regFromA);
							when(registry.findByEntityId(id4)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Reject A And Not Delete Registration")
						void thenShoudRejectAAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();
							
							helper.rejectForCollisionNoDelete(this.grantabletoB, clientA);						
							
						}
						
						@Test
						@DisplayName("Then Should Reject B And Not Delete Registration")
						void thenShoudRejectBAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();							
							helper.rejectForCollisionNoDelete(this.grantabletoA, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB1C1D1E3 extends WhenCreating {
						
						AB1C1D1E3() {
							super("=003", "=004");
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id3)).thenReturn(regFromA);
							when(registry.findByEntityId(id4)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
				
					
				}
				
				@Nested
				@DisplayName("Given Expired Registrations")
				class AB1C1D2 extends GivenExpiredRegs {
					
					AB1C1D2() {
						super(id3, id4);
					}					
						
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB1C1D2E1 {					
																							
						@Test
						@DisplayName("Then Should Register A With One Up And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=005",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With One Up And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=005",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB1C1D2E3 extends WhenCreating {
						
						AB1C1D2E3() {
							super("=003", "=004");
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id3)).thenReturn(regFromA);
							when(registry.findByEntityId(id4)).thenReturn(regFromB);
						}
						
						// We have registered 
						// 	{{p1Code, "=003"}, clientA, ...}
						// 	{{p1Code, "=004"}, clientB, ...}
						// 
						// and expect to get these granted now for the respective clients.							 																				
						
																						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB1C1D2E2 extends WhenCreating {
						
						AB1C1D2E2() {
							super("=003", "=004");
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id3)).thenReturn(regFromA);
							when(registry.findByEntityId(id4)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant B to A and Delete Registration from B")
						void thenShouldGrantAAndDelete() {		
						
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoB, clientA, clientB);						
							
						}
						
						@Test
						@DisplayName("Then Should Grant A to B and Delete Registration from A")
						void thenShouldGrantBAndDelete() {		
						
							TestHelper helper = new TestHelper();							
							helper.grantAndDelete(this.grantabletoA, clientB, clientA);
						}
					}
					
					
				}
			
				@Nested
				@DisplayName("Given No Registrations")
				class AB1C1D3 {
					
					private List<IdRegistration> registrations = new ArrayList<IdRegistration>();
					
					@BeforeEach
					void setup() {						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);						
						when(registry.findByEntityId(any(EntityId.class))).thenReturn(null);						
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Any Registration")
					class AB1C1D3E1 extends WhenCreating {
						
						AB1C1D3E1() {
							super("=003", "=004");
						}
						
						
						@Test
						@DisplayName("Then Should Reject A and Not Delete Registration")
						void thenShouldRejectAAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.rejectForUnregisteredNoDelete(this.grantabletoA, clientA);							
						}
						
						@Test
						@DisplayName("Then Should Reject B and Not Delete Registration")
						void thenShouldRejectBAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.rejectForUnregisteredNoDelete(this.grantabletoB, clientB);							
						}
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB1C1D3E2 {
						
						@Test
						@DisplayName("Then Should Register A with with One Up Against Siblings and Save")
						void thenShouldRegisterAWithDefaultAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=003",			// one up against siblings
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B with One Up Against Siblings and Save")
						void thenShouldRegisterBWithoneUpAndSave() throws Exception {
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=003",			// one up against siblings
									clientB,
									functionFormat);
						}
						
					}
				}	
			}

			@Nested
			@DisplayName("Given No Siblings Committed")
			class AB1C2 {
				
				private List<Function> siblings = new ArrayList<Function> ();
				
				@BeforeEach
				void setup() {
					
					when(functionRepo.findRootsByProjectCode(p1Code.toString()))
						.thenReturn(siblings);					
				}
				
				@Nested
				@DisplayName("Given Unexpired Registrations")
				class AB1C2D1 extends GivenRegs {
					
					AB1C2D1() {
						super(id1, id2);
					}
					
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB1C2D1E1 {					
																		
						@Test
						@DisplayName("Then Should Register A With Default And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=003",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With Default And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=003",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB1C2D1E2 extends WhenCreating {
						
						AB1C2D1E2() {
							super("=001", "=002");
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id1)).thenReturn(regFromA);
							when(registry.findByEntityId(id2)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB1C2D1E3 extends WhenCreating {
						
						AB1C2D1E3() {
							super("=001", "=002");
						}
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id1)).thenReturn(regFromA);
							when(registry.findByEntityId(id2)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Reject A And Not Delete Registration")
						void thenShoudRejectAAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();
							
							helper.rejectForCollisionNoDelete(this.grantabletoB, clientA);						
							
						}
						
						@Test
						@DisplayName("Then Should Reject B And Not Delete Registration")
						void thenShoudRejectBAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();							
							helper.rejectForCollisionNoDelete(this.grantabletoA, clientB);
						}
					}
				}
				
				@Nested
				@DisplayName("Given Expired Registrations")
				class AB1C2D2 extends GivenExpiredRegs {
					
					AB1C2D2() {
						super(id1, id2);
					}
					
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					public class AB1C2D2E1 {					
																		
						@Test
						@DisplayName("Then Should Register A With Default And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=003",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With Default And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=003",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB1C2D1E2 extends WhenCreating {
						
						AB1C2D1E2() {
							super("=001", "=002");
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id1)).thenReturn(regFromA);
							when(registry.findByEntityId(id2)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB1C2D1E3 extends WhenCreating {
						
						AB1C2D1E3() {
							super("=001", "=002");
						}
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id1)).thenReturn(regFromA);
							when(registry.findByEntityId(id2)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from B")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoB, clientA, clientB);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from A")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoA, clientB, clientA);
						}
					}
				}
				
				@Nested
				@DisplayName("Given No Registrations")
				class AB1C2D3 {
					
					private List<IdRegistration> registrations = new ArrayList<IdRegistration>();
					
					@BeforeEach
					void setup() {						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);						
						when(registry.findByEntityId(any(EntityId.class))).thenReturn(null);						
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					public class AB1C2D3E1 {
						
						@Test
						@DisplayName("Then Should Register A with Default and Save")
						void thenShouldRegisterAWithDefaultAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=001",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B with Default and Save")
						void thenShouldRegisterBWithDefaultAndSave() throws Exception {
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=001",
									clientB,
									functionFormat);
						}
						
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Any Registration")
					class AB1C1D3E1 extends WhenCreating {
						
						AB1C1D3E1() {
							super("=001", "=002");
						}
						
						
						@Test
						@DisplayName("Then Should Reject A and Not Delete Registration")
						void thenShouldRejectAAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.rejectForUnregisteredNoDelete(this.grantabletoA, clientA);							
						}
						
						@Test
						@DisplayName("Then Should Reject B and Not Delete Registration")
						void thenShouldRejectBAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.rejectForUnregisteredNoDelete(this.grantabletoB, clientB);							
						}
					}
				}
			}
		}
		
		@Nested
		@DisplayName("When Child Requested")
		class AB2 {
			
			String parentCode = "=001";
						
			@Nested
			@DisplayName("Given Siblings Committed")
			class AB2C1 {
				
				private EntityContent content1 = new EntityContent("root1", "a root");
				private Function root1 = new Function(id1, null, content1);
							
				// committed siblings
				private EntityId id11Committed = new EntityId(
						p1Code, 
						new EntityCode("=001.001", functionFormat));
				private EntityContent content11 = new EntityContent("child1.1", "a child");
				private Function child11Committed= new Function(id11Committed, null, content11);

				private EntityId id12Committed = new EntityId(
						p1Code, 
						new EntityCode("=001.002", functionFormat));
				private EntityContent content12 = new EntityContent("child1.2", "a child");
				private Function child12Committed= new Function(id12Committed, null, content12);
				
				// registered "siblings"
				private EntityId id13Registered = new EntityId(
						p1Code, 
						new EntityCode("=001.003", functionFormat));				
				private EntityId id14Registered = new EntityId(
						p1Code, 
						new EntityCode("=001.004", functionFormat));								
				
				
				AB2C1() {
					root1.addChild(child11Committed);
					root1.addChild(child12Committed);
				}
				
				@BeforeEach
				void setup() {
					
					when(functionRepo.findByCode(id1)).thenReturn(root1);					
				}
																
				@Nested
				@DisplayName("Given Unexpired Registrations")
				class AB2C1D1 extends GivenRegs {
					
					AB2C1D1() {
						super(id13Registered, id14Registered);
					}
					
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB2C1D1E1 {					
																		
						@Test
						@DisplayName("Then Should Register A With One Up Against Siblings + Registrations And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.005",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With One Up Against Siblings + Registrations And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.005",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB2C1D1E2 extends WhenCreating {
						
						AB2C1D1E2() {
							super(
								id13Registered.entity.toString(), 
								id14Registered.entity.toString());
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id13Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id14Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
						
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB2C1D1E3 extends WhenCreating {
						
						AB2C1D1E3() {
							super(
								id13Registered.entity.toString(), 
								id14Registered.entity.toString());
						}
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id13Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id14Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Reject A And Not Delete Registration")
						void thenShouldRejectAAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();
							
							helper.rejectForCollisionNoDelete(this.grantabletoB, clientA);						
							
						}
						
						@Test
						@DisplayName("Then Should Reject B And Not Delete Registration")
						void thenShouldRejectBAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();							
							helper.rejectForCollisionNoDelete(this.grantabletoA, clientB);
						}
					}
				}
				
				@Nested
				@DisplayName("Given Expired Registrations")
				class AB2C1D2 extends GivenExpiredRegs {
					
					AB2C1D2() {
						super(id13Registered, id14Registered);
					}					
						
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB2C1D2E1 {					
																							
						@Test
						@DisplayName("Then Should Register A With One Up And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.005",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With One Up And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.005",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB2C1D2E2 extends WhenCreating {
						
						AB2C1D2E2() {
							super(
								id13Registered.entity.toString(), 
								id14Registered.entity.toString());
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id13Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id14Registered)).thenReturn(regFromB);
						}
																							
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB1C1D2E2 extends WhenCreating {
						
						AB1C1D2E2() {
							super(
								id13Registered.entity.toString(), 
								id14Registered.entity.toString());
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id13Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id14Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant B to A and Delete Registration from B")
						void thenShouldGrantAAndDelete() {		
						
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoB, clientA, clientB);						
							
						}
						
						@Test
						@DisplayName("Then Should Grant A to B and Delete Registration from A")
						void thenShouldGrantBAndDelete() {		
						
							TestHelper helper = new TestHelper();							
							helper.grantAndDelete(this.grantabletoA, clientB, clientA);
						}
					}
					
					
				}
			
				@Nested
				@DisplayName("Given No Registrations")
				class AB1C1D3 {
					
					private List<IdRegistration> registrations = new ArrayList<IdRegistration>();
					
					@BeforeEach
					void setup() {						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);						
						when(registry.findByEntityId(any(EntityId.class))).thenReturn(null);						
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Any Registration")
					class AB1C1D3E1 extends WhenCreating {
						
						AB1C1D3E1() {
							super("=001.003", "=001.004");
						}
						
						
						@Test
						@DisplayName("Then Should Reject A and Not Delete Registration")
						void thenShouldRejectAAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();							
							helper.rejectForUnregisteredNoDelete(this.grantabletoA, clientA);							
						}
						
						@Test
						@DisplayName("Then Should Reject B and Not Delete Registration")
						void thenShouldRejectBAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();							
							helper.rejectForUnregisteredNoDelete(this.grantabletoB, clientB);							
						}
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB1C1D3E2 {
						
						@Test
						@DisplayName("Then Should Register A with with One Up Against Siblings and Save")
						void thenShouldRegisterAWithDefaultAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=001.003",			// one up against siblings
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B with One Up Against Siblings and Save")
						void thenShouldRegisterBWithoneUpAndSave() throws Exception {
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=001.003",			// one up against siblings
									clientB,
									functionFormat);
						}
						
					}
				}	
			}

			@Nested
			@DisplayName("Given No Siblings Committed")
			class AB2C2 {
				
				private EntityId id11Registered = new EntityId(
						p1Code, 
						new EntityCode("=001.001", functionFormat));				
				private EntityId id12Registered = new EntityId(
						p1Code, 
						new EntityCode("=001.002", functionFormat));								
				private EntityContent content1 = new EntityContent("root1", "a root");
				
				private Function root1 = new Function(id1, null, content1);				
				
				//private List<Function> siblings = new ArrayList<Function> ();
				
				
				@BeforeEach
				void setup() {
					
					when(functionRepo.findByCode(id1)).thenReturn(root1);
				}
				
				@Nested
				@DisplayName("Given Unexpired Registrations")
				class AB2C2D1 extends GivenRegs {
					
					AB2C2D1() {
						super(id11Registered, id12Registered);
					}
					
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					class AB2C2D1E1 {					
																		
						@Test
						@DisplayName("Then Should Register A With One Up Against Registration And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.003",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With One Up Against Registration And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.003",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB2C2D1E2 extends WhenCreating {
						
						AB2C2D1E2() {
							super(
								id11Registered.entity.toString(), 
								id12Registered.entity.toString());
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id11Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id12Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB2C2D1E3 extends WhenCreating {
						
						AB2C2D1E3() {
							super(
									id11Registered.entity.toString(), 
									id12Registered.entity.toString());
						}
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id11Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id12Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Reject A And Not Delete Registration")
						void thenShoudRejectAAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();
							
							helper.rejectForCollisionNoDelete(this.grantabletoB, clientA);						
							
						}
						
						@Test
						@DisplayName("Then Should Reject B And Not Delete Registration")
						void thenShoudRejectBAndNotDeleteRegistration() {		
						
							TestHelper helper = new TestHelper();							
							helper.rejectForCollisionNoDelete(this.grantabletoA, clientB);
						}
					}
				}
				
				@Nested
				@DisplayName("Given Expired Registrations")
				class AB2C2D2 extends GivenExpiredRegs {
					
					AB2C2D2() {
						super(id11Registered, id12Registered);
					}
					
					@BeforeEach
					void setup() {
						registrations.add(this.regFromA);
						registrations.add(this.regFromB);
						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					public class AB1C2D2E1 {					
																		
						@Test
						@DisplayName("Then Should Register A With Default And Save")
						void thenShouldRegister_A_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();							
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.003",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B With Default And Save")
						void thenShouldRegister_B_WithOneUpAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode,
									"=001.003",
									clientB,
									functionFormat);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Own Registration")
					class AB2C2D1E2 extends WhenCreating {
						
						AB2C2D1E2() {
							super(
									id11Registered.entity.toString(), 
									id12Registered.entity.toString());
						}
						
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id11Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id12Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from A")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoA, clientA, clientA);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from B")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoB, clientB, clientB);
						}
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Colliding Registration")
					class AB2C2D1E3 extends WhenCreating {
						
						AB2C2D1E3() {
							super(
									id11Registered.entity.toString(), 
									id12Registered.entity.toString());
						}
						
						@BeforeEach
						void setup() {
							reset(registry);
							when(registry.findByEntityId(id11Registered)).thenReturn(regFromA);
							when(registry.findByEntityId(id12Registered)).thenReturn(regFromB);
						}
						
						@Test
						@DisplayName("Then Should Grant to A and Delete Registration from B")
						void thenShouldGrantAAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.grantAndDelete(this.grantabletoB, clientA, clientB);
						}
						
						@Test
						@DisplayName("Then Should Grant to B and Delete Registration from A")
						void thenShouldGrantBAndDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							helper.grantAndDelete(this.grantabletoA, clientB, clientA);
						}
					}
				}
				
				@Nested
				@DisplayName("Given No Registrations")
				class AB2C2D3 {
					
					private List<IdRegistration> registrations = new ArrayList<IdRegistration>();
					
					@BeforeEach
					void setup() {						
						reset(registry);
						when(registry.findByProjectCode(p1Code)).thenReturn(registrations);						
						when(registry.findByEntityId(any(EntityId.class))).thenReturn(null);						
					}
					
					@Nested
					@DisplayName("When Any Client Registers")
					public class AB2C2D3E1 {
						
						@Test
						@DisplayName("Then Should Register A with Default and Save")
						void thenShouldRegisterAWithDefaultAndSave() throws Exception {
							
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=001.001",
									clientA,
									functionFormat);
						}
						
						@Test
						@DisplayName("Then Should Register B with Default and Save")
						void thenShouldRegisterBWithDefaultAndSave() throws Exception {
							TestHelper helper = new TestHelper();
							helper.createAndSave(
									p1Code, 
									parentCode, 
									"=001.001",
									clientB,
									functionFormat);
						}
						
					}
					
					@Nested
					@DisplayName("When Client Wants to Create on Any Registration")
					class AB2C2D3E2 extends WhenCreating {
						
						AB2C2D3E2() {
							super("=001", "=002");		// don't care values !
						}
						
						
						@Test
						@DisplayName("Then Should Reject A and Not Delete Registration")
						void thenShouldRejectAAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.rejectForUnregisteredNoDelete(this.grantabletoA, clientA);							
						}
						
						@Test
						@DisplayName("Then Should Reject B and Not Delete Registration")
						void thenShouldRejectBAndNotDeleteRegistration() {
							
							TestHelper helper = new TestHelper();
							
							helper.rejectForUnregisteredNoDelete(this.grantabletoB, clientB);							
						}
					}
				}
			}
		}
	}
}
