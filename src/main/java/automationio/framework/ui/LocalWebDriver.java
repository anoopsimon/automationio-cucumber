package automationio.framework.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class LocalWebDriver implements IWebDriverManager{
    @Override
    public WebDriver launch(String browser)
    {
        if(!isBrowserSupported(browser))
            throw new RuntimeException("Browser => " + browser + "not supported.");

        if(chromeDriverExeFound())
        {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            options.addArguments("incognito");
            options.addArguments("disable-infobars");
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
            return driver;
        }

        throw new RuntimeException("please set chromedriver exe as a system property.'webdriver.chrome.driver'.");

    }

    private boolean chromeDriverExeFound()
    {
        return System.getProperty("webdriver.chrome.driver")!=null;
    }

    private boolean isBrowserSupported(String browser)
    {
       return browser.equalsIgnoreCase("chrome");

    }
}
