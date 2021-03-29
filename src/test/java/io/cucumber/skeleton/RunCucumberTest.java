package io.cucumber.skeleton;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},tags = {"@ui"},glue = {"com.automationio.stepdefinitions.ui"},features = {"src/test/resources/features"})
public class RunCucumberTest {
}
