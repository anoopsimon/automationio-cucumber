package automationio.framework.ui;

import io.github.sukgu.Shadow;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class ShadowWrapper {
    private WebDriver driver;
    public  ShadowWrapper(WebDriver driver)
    {
        this.driver=driver;
    }

    public Shadow getShadow(){
        return new Shadow(driver);
    }
    private String getLocatorType(String locator)
    {
        if(locator== null|| locator.isEmpty() ) throw  new RuntimeException("Locator cannot be null or empty");
        if(locator.toLowerCase().startsWith("//")) return "XPATH";

        return "CSS";
    }

    /**
     * get a webelement using shadow dom
     * @param locator
     * @return
     */
    public WebElement getElement(String locator)
    {
        if(getLocatorType(locator).equalsIgnoreCase("XPATH"))
            return getShadow().findElementByXPath(locator);

        return getShadow().findElement(locator);
    }
 public List<WebElement> getElements(String locator)
    {
        if(getLocatorType(locator).equalsIgnoreCase("XPATH"))
            return getShadow().findElementsByXPath(locator);

        return getShadow().findElements(locator);
    }

    /**
     * type text in to input field
     * @param locator
     * @param text
     */
    public void type(String locator, String text)
    {
        getElement(locator).sendKeys(text);
    }

    /**
     * click on element
     * @param locator
     */
    public void click(String locator)
    {
        getElement(locator).click();
    }

    /**
     * Select from dropdown by value.
     * @param locator
     * @param value
     */
    public void selectByValue(String locator,String value)
    {
        new Select(getElement(locator)).selectByValue(value);
    }

    /**
     * Select from dropdown by index.
     * @param locator
     * @param index
     */
    public void selectByIndex(String locator,int index)
    {
        new Select(getElement(locator)).selectByIndex(index);
    }

    /**
     * Select from dropdown by text.
     * @param locator
     * @param text
     */
    public void selectByText(String locator,String text)
    {
        new Select(getElement(locator)).selectByVisibleText(text);
    }

    /**
     * wait for page to load
     */
    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
        }
    }

    public  void captureScreenshot()
    {
        int bodyHeight = getElement("body").getSize().getHeight();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int windowChromeHeight = (int) (long)  js.executeScript("return window.outerHeight - window.innerHeight");
        Dimension size = new Dimension(driver.manage().window().getSize().getWidth(), bodyHeight + windowChromeHeight);
        js.executeScript("window.scrollTo(0, 0);");
        driver.manage().window().setSize(size);

        File screenshotFile = new File("C:/Temp/screenshot.png");
        TakesScreenshot scr = (TakesScreenshot)driver;
        try {
            Files.write(screenshotFile.toPath(), scr.getScreenshotAs(OutputType.BYTES));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void webElementScreenshot(String locator){
        WebElement element = getElement(locator);
        WrapsDriver wrapsDriver = (WrapsDriver) element;
        File screenshot = ((TakesScreenshot) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        Rectangle rectangle = new Rectangle(element.getSize().width, element.getSize().height, element.getSize().height, element.getSize().width);
        Point location = element.getLocation();
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage destImage = bufferedImage.getSubimage(location.x, location.y, rectangle.width, rectangle.height);
        try {
            ImageIO.write(destImage, "png", screenshot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File("C:/Temp/"+UUID.randomUUID()+".png");

        try {
            FileUtils.copyFile(screenshot, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
