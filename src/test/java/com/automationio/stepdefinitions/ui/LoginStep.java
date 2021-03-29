package com.automationio.stepdefinitions.ui;
import automationio.framework.ui.IWebDriverManager;
import automationio.framework.ui.LocalWebDriver;
import automationio.framework.ui.ShadowWrapper;
import io.cucumber.java.en.Given;
import io.github.sukgu.Shadow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginStep
{
    private WebDriver driver;
    private ShadowWrapper getCommands(){
        return new ShadowWrapper(this.driver);
    }
    @Given("I launch {string} browser")
    public void i_launch_browser(String string)
    {
        System.setProperty("webdriver.chrome.driver","C:/selenoid/chromedriver.exe");
        IWebDriverManager driverManager =new LocalWebDriver();
        this.driver = driverManager.launch("Chrome");
        this.driver.get("https://www.bing.com");

        getCommands().waitForPageLoaded();
        getCommands().type("#loanOptions-loanAmount","5000");

        String locator= "div[data-component-id='FieldWrapper']";
        String labelTag= "label";
        String lookupText ="How often would you like to make repayments?";
        String answerTag = "label";
        String answer = "Weekly";

        getCommands().click("//legend[text()=' repayments?']//parent::fieldset//label[text()='Weekly']");
    }

}
