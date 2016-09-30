package navigation;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import pages.Page;
import stats.StatsAction;
import stats.StatsTracker;
import sut.Environment;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Representation of a Navigator. This (Singleton) class is responsible for
 * properly using the WebDriver and some additional functionality.
 * @author Thibault Helsmoortel
 */
public final class Navigator {
    private static Navigator navigatorInstance = new Navigator();

    private static final Logger LOGGER = Logger.getLogger(Navigator.class);

    private WebDriver driver;
    private Wait<WebDriver> wait;
    private Environment environment;

    /**
     * Class constructor. Publicly unavailable.
     */
    private Navigator() {
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public Wait<WebDriver> getWait() {
        return wait;
    }

    public void setWait(Wait<WebDriver> wait) {
        this.wait = wait;
    }

    /**
     * Clicks on a specified element.
     * @param element the element to click on
     */
    public void click(WebElement element) {
        LOGGER.debug("Performing click on " + element.toString());
        element.click();
        StatsTracker.getInstance().track(StatsAction.MOUSE_LMB_CLICK);
    }

    /**
     * Clicks on a specified element using ng-click (Angular).
     * Using this method in stead of the default one has a slight
     * performance impact compared to the default click method.
     * @param element the element to click on
     */
    public void NGClick(WebElement element) {
        LOGGER.debug("Performing ng-click on " + element.toString());
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();

        //Implicit wait is needed because Selenium doesn't know how angular loads and works
        implicitlyWait(1);

        explicitlyWaitForElementClickable(element);
        actions.moveToElement(element).click().perform();
        StatsTracker.getInstance().track(StatsAction.MOUSE_LMB_CLICK);
    }

    /**
     * Double clicks on a specified element.
     * @param element the element to double click
     */
    public void doubleClick(WebElement element) {
        LOGGER.debug("Performing double click on " + element.toString());
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
        StatsTracker.getInstance().track(StatsAction.MOUSE_LMB_DOUBLE_CLICK);
    }

    /**
     * Sends keys on a specified element.
     * @param element the element to send keys to
     * @param charSequence the keys to send
     */
    public void sendKeys(WebElement element, String charSequence) {
        LOGGER.debug("Sending keys [" + charSequence + "] to " + element.toString());
        Actions actions = new Actions(driver);
        actions.sendKeys(element, charSequence).perform();
        StatsTracker.getInstance().track(StatsAction.KEYBOARD_TYPE);
    }

    /**
     * Sends specified keys.
     * @param charSequence the keys to send
     */
    public void sendKeys(String charSequence) {
        LOGGER.debug("Sending keys [" + charSequence + "]");
        Actions actions = new Actions(driver);
        actions.sendKeys(charSequence).perform();
        StatsTracker.getInstance().track(StatsAction.KEYBOARD_TYPE);
    }

    /**
     * Navigates to a specified url.
     * @param url the url to navigate to
     */
    public void navigateTo(URL url) {
        LOGGER.debug("Navigating to " + url.toString());
        driver.navigate().to(url);
    }

    /**
     * Navigates to a specified page.
     * @param page the page to navigate to
     */
    public void navigateTo(Page page) {
        LOGGER.debug("Navigating to " + page.toString());
        driver.navigate().to(page.getCompleteURL());
    }

    /**
     * Navigates back to the previous page.
     */
    public void navigateBack() {
        LOGGER.debug("Navigating back");
        driver.navigate().back();
    }

    /**
     * Navigates to the next page.
     */
    public void navigateForward() {
        LOGGER.debug("Navigating forward");
        driver.navigate().forward();
    }

    /**
     * Refreshes the current page.
     */
    public void navigateRefresh() {
        LOGGER.debug("Performing refresh");
        driver.navigate().refresh();
    }

    /**
     * Performs an implicit wait for a given amount of seconds.
     * @param seconds the amount of seconds to implicitly wait
     */
    public void implicitlyWait(int seconds) {
        implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    /**
     * Performs an implicit wait for a given amount of time and the corresponding time unit.
     * @param value the amount of time to wait
     * @param timeUnit the time unit for the given amount to wait
     */
    public void implicitlyWait(long value, TimeUnit timeUnit) {
        LOGGER.debug("Implicitly wait for " + value + " " + timeUnit.toString());
        driver.manage().timeouts().implicitlyWait(value, timeUnit);
    }

    /**
     * Performs an explicit wait for an element until it is present on the page.
     * @param locator the method used to find the element
     */
    public void explicitlyWaitForElementPresent(By locator) {
        LOGGER.debug("Explicitly waiting for an element to be present");
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Performs an explicit wait for an element until it is visible on the page.
     * @param locator the method used to find the element
     */
    public void explicitlyWaitForElementVisible(By locator) {
        LOGGER.debug("Explicitly waiting for an element to be visible");
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Performs an explicit wait for an element until it is invisible on the page.
     * @param locator the method used to find the element
     */
    public void explicitlyWaitForElementInvisible(By locator) {
        LOGGER.debug("Explicitly waiting for an element to be invisible");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Performs an explicit wait for an element until it is clickable.
     * @param locator the method used to find the element
     */
    public void explicitlyWaitForElementClickable(By locator) {
        LOGGER.debug("Explicitly waiting for an element to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Performs an explicit wait for given WebElement until it is clickable.
     * @param element the element for which to wait until it is clickable
     */
    public void explicitlyWaitForElementClickable(WebElement element) {
        LOGGER.debug("Explicitly waiting for element " + element.toString() + " to be clickable");
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Scrolls the specified element into the view.
     *
     * @param element the element to scroll in the view
     */
    public void scrollElementIntoView(WebElement element) {
        LOGGER.debug("Scrolling element " + element.toString() + " into view");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Moves te mouse to the specified element.
     *
     * @param element the element to which the mouse should move
     */
    public void moveToElement(WebElement element) {
        LOGGER.debug("Moving to element: " + element.toString());
        Actions actions = new Actions(driver);
        actions.moveToElement(element).build().perform();
    }

    /**
     * Returns the parent WebElement of the specified element.
     *
     * @param element the element from which to find the parent
     * @return the parent element of the specified element
     */
    public WebElement getParent(WebElement element) {
        LOGGER.debug("Retrieving parent element of: " + element.toString());
        return (WebElement) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].parentNode;", element);
    }

    /**
     * Drags an element with an offset of the current position.
     *
     * @param element the element to drag
     * @param xOffset the x-axis offset
     * @param yOffset the y-axis offset
     */
    public void dragElement(WebElement element, int xOffset, int yOffset) {
        LOGGER.debug("Dragging element: " + element.toString());
        Actions action = new Actions(driver);
        action.moveToElement(element);
        action.clickAndHold();
        action.moveByOffset(xOffset, yOffset);
        action.release();
        action.build().perform();
    }

    /**
     * Drags and drops a source element onto a target element.
     *
     * @param source the source element to drag to the target element
     * @param target the target element that will accept the source element
     */
    public void dragAndDropElement(WebElement source, WebElement target) {
        LOGGER.debug("Dragging element: " + source.toString() + " and dropping on: " + target.toString());
        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Returns the title of the current page.
     * @return the title of the current page
     */
    public String getTitle() {
        return driver.getTitle();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public static synchronized Navigator getInstance() {
        return navigatorInstance;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
