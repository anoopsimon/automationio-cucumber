package io.cucumber.skeleton;

//import io.cucumber.core.exception.CompositeCucumberException;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.io.File;

public class CucumberTestReporter implements ConcurrentEventListener {
    private File reportDir;

    public CucumberTestReporter(String outputDir) {
        createOutputDir(outputDir);
    }

    private void createOutputDir(String outputDir) {
        reportDir = new File(outputDir);
        if (!reportDir.exists() && !reportDir.mkdirs()) {
            System.out.printf("report dir creation Failed");
        }
    }

    private EventHandler<TestRunFinished> runtTestFinishedHandler = new EventHandler<TestRunFinished>()
    {
        @Override
        public void receive(TestRunFinished event) {
            finishReport(event);
        }
    };

    private EventHandler<TestRunStarted> runTestStartedHandler = new EventHandler<TestRunStarted>() {
        @Override
        public void receive(TestRunStarted event) {
            startReport(event);
        }


    };
    private EventHandler<TestCaseStarted> runTestCaseStartedHandler = new EventHandler<TestCaseStarted>() {
        @Override
        public void receive(TestCaseStarted event) {
            testCaseStarted(event);
        }


    };
    private EventHandler<TestCaseFinished> runTestCaseFinishedHandler = new EventHandler<TestCaseFinished>() {
        @Override
        public void receive(TestCaseFinished event) {
            testCaseFinished(event);
        }


    };


    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, runTestStartedHandler);
        publisher.registerHandlerFor(TestRunFinished.class, runtTestFinishedHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, runTestCaseStartedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, runTestCaseFinishedHandler);

    }

    private void startReport(TestRunStarted event) {
        System.out.println("Start Report");
    }
    private void finishReport(TestRunFinished event) {
        System.out.println("Close Report");
    }
    private void testCaseStarted(TestCaseStarted event) {
        System.out.println("Start Testcase");
    }
    private void testCaseFinished(TestCaseFinished event) {
        System.out.println("Finished Testcase");
    }
}