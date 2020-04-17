package io.cucumber.skeleton;

//import io.cucumber.core.exception.CompositeCucumberException;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;

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

    private EventHandler<TestRunStarted> runStartedHandler = new EventHandler<TestRunStarted>() {
        @Override
        public void receive(TestRunStarted event) {
            startReport(event);
        }


    };

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, runStartedHandler);
        publisher.registerHandlerFor(TestRunFinished.class, runtTestFinishedHandler);

    }

    private void startReport(TestRunStarted event) {
        System.out.println("Start Report");
    }
    private void finishReport(TestRunFinished event) {
        System.out.println("Close Report");
    }
}