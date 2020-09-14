package fscl;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.io.PrintWriter;

import fscl.function.EmptyFunctionPageTest;
import fscl.function.FunctionLifeCycleTest;



public class FsclEndToEndTest {
	
	SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
		
	public static void main(String[] args) {
		
		FsclEndToEndTest test = new FsclEndToEndTest();		
		test.run(EmptyFunctionPageTest.class);
		test.run(FunctionLifeCycleTest.class);
		
		TestExecutionSummary summary = test.summaryListener.getSummary();
		PrintWriter writer = new PrintWriter(System.out);
		summary.printTo(writer);
		summary.printFailuresTo(writer);		
	}
	
	protected void run(Class testClass) {
		
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
