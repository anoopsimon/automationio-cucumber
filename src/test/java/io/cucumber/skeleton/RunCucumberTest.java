package io.cucumber.skeleton;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty"},
    tags = {"@excel"},
    features={"src/test/resources/features"},
    glue = {"com.automationio.stepdefinitions"}
    //C:\dev\Java\automationio-cucumber\src\test\resources\features
    )
public class RunCucumberTest {
}
