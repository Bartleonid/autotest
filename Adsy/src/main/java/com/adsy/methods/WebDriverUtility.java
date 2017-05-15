package com.adsy.methods;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverUtility {

	protected WebDriver driver;

	public void start() {
		ChromeOptions options = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("profile.default_content_settings.popups", 0);
		options.addArguments("disable-extensions");
		prefs.put("credentials_enable_service", false);
		prefs.put("password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs);
		options.addArguments("chrome.switches", "--disable-extensions");
		options.addArguments("--test-type");
		options.addArguments("disable-infobars");
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		cap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
				UnexpectedAlertBehaviour.ACCEPT);
		System.setProperty("webdriver.chrome.driver",
				"L:\\Distributives\\Selenium\\Chrome\\chromedriver.exe");
		driver = new ChromeDriver(cap);
		driver.get("https://adsy.com.ua/test/automation-qa/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		// driver.manage().deleteAllCookies();
	}

	private final static int TIMEOUT = 30000;

	public static WebElement findElement(WebDriver driver, By locator) {
		By cssLocator = locator;
		waitForElement(driver, cssLocator, TIMEOUT);
		WebElement element = driver.findElement(cssLocator);
		return element;
	}

	public void compareTitle(String expectedTitle) throws InterruptedException {
		// waitForPageLoaded(driver);
		String actualTitle = driver.getTitle();
		System.out.println("actualTitle  " + actualTitle);
		System.out.println("expectedTitle  " + expectedTitle);
		assertEquals(expectedTitle, actualTitle);
	}

	protected static void waitForPageLoaded(WebDriver driver)
			throws InterruptedException {
		Thread.sleep(2000);
		ArrayList<String> tabs = new ArrayList<String>(
				driver.getWindowHandles());
		for (int i = 0; i < tabs.size(); i++) {
			driver.switchTo().window(tabs.get(i));
			ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return document.readyState").equals("complete");
				}
			};
			org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(
					driver, 30);
			try {
				wait.until(expectation);
			} catch (Throwable error) {
			}
		}
	}

	protected WebElement getElement(WebDriver driver, Object obj) {
		// System.out.println(driver + "   driver");
		// System.out.println(obj + "   object");
		if (obj.getClass().getName().contains(By.class.getName())) {
			return waitForElement(driver, (By) obj, 30);
		}
		return (WebElement) obj;
	}

	protected void click(WebDriver driver, By by) throws InterruptedException {
		WebElement element = waitForElement(driver, by, TIMEOUT);

		try {
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			action.click(element).build().perform();
			System.out.println("element is clicked  " + by);
			waitForPageLoaded(driver);
		} catch (Exception e) {
		}
	}

	protected void type(WebDriver driver, By by, Object text) {
		text = String.valueOf(text);
		System.out.println("Filling a text '" + text + "' into the field '"
				+ by + "' on page '" + driver.getCurrentUrl() + "'");
		WebElement element = waitForElement(driver, by, TIMEOUT);
		try {
			element.clear();
			element.sendKeys((String) text);
		} catch (Exception e) {
		}
	}

	public static WebElement waitForElement(WebDriver driver, final By by,
			int timeout) {
		org.openqa.selenium.support.ui.Wait<WebDriver> wait = new WebDriverWait(
				driver, timeout);

		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);

		ExpectedCondition<WebElement> expectation = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(by);
			}
		};

		try {
			return wait.until(expectation);
		} catch (Throwable error) {
			return null;
		}
	}

}
