package fscl;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.io.PrintWriter;
import java.util.Arrays;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import fscl.function.EmptyFunctionPageTest;
import fscl.function.FunctionLifeCycleTest;

public class EndToEndTestRunner {
	
	SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
	
	public static void main(String[] args) {
		
		EndToEndTestRunner testRunner = new EndToEndTestRunner();
		
		FsclEndToEndTest tests[] = new FsclEndToEndTest[] {
			new EmptyFunctionPageTest(),
			new FunctionLifeCycleTest()
		};
		
		testRunner.runAll(tests);
		
		TestExecutionSummary summary = testRunner.summaryListener.getSummary();
		PrintWriter writer = new PrintWriter(System.out);
		summary.printTo(writer);
		summary.printFailuresTo(writer);
	}
	
	protected void runAll(FsclEndToEndTest[] tests) {
		Arrays.stream(tests).forEach(test -> this.run(test.getClass()));
	}

	protected <T extends FsclEndToEndTest> void run(Class<T> testClass) {
		
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
	      .selectors(selectClass(testClass))
	      .build();
	    Launcher launcher = LauncherFactory.create();
	    TestExecutionListener[] listeners = new TestExecutionListener[] { 
	    		this.summaryListener
	    };
	    
	    launcher.registerTestExecutionListeners(listeners);
	    launcher.execute(request);        
	    
	}

}
