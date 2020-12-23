package testng_basics;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Testng_basics {
	public static WebDriver driver;

	@Test
	public void testInizalise(String browser) throws Exception {
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					"F:\\selenium files\\DriverFiles\\chromedriver_win32\\chromedriver.exe");
			driver = new ChromeDriver();

		} else if (browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.firefox.driver",
					"F:\\selenium files\\DriverFiles\\geckodriver-v0.28.0-win64 (1)\\geckodriver.exe");
			driver = new FirefoxDriver();

		} else if (browser.equalsIgnoreCase("internetexplorer")) {

			System.setProperty("webdriver.ie.driver",
					"F:\\selenium files\\DriverFiles\\IEDriverServer_x64_3.141.59\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();

		} else

		{
			throw new Exception("INVALID BROWSER ");

		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();

	}

	@BeforeSuite
	public void environmentVerify() {
		System.out.println("Environment setup done");
	}

	@AfterSuite
	public void environmentClose() {
		System.out.println("Environment close");
	}

	@BeforeTest
	public void dbConnection() {
		System.out.println("connected to DB");
	}

	@AfterTest
	public void dBClose() {
		System.out.println("DB closed");
	}

	@BeforeMethod
	@Parameters({ "browser", "url" })
	public void browserLaunch(String browserName, String URL) throws Exception {
		System.out.println("Launching Browser");
		testInizalise(browserName);
		driver.get(URL);
	}

	@AfterMethod
	public void browserClose(ITestResult result) throws IOException {
		if (ITestResult.FAILURE == result.getStatus()) {
			TakesScreenshot takeScreenshot = (TakesScreenshot) driver;
			File Screenshot = takeScreenshot.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(Screenshot, new File("./Screenshots/" + result.getName() + ".png"));
		}
		System.out.println("Closing browser.....");
		driver.close();
		System.out.println("Test Completed");

	}

	@Test(priority = 1, enabled = false)
	public void verifyTitle() {
		String actualTitle = driver.getTitle();
		String expectedTitle = "Welcome: Mercury Tours";
		Assert.assertEquals(actualTitle, expectedTitle, "Invalid title");

	}

	@Test(priority = 2, enabled = true, dataProvider = "userCredentials")
	public void verifyLogin(String userNAME, String password) {
		WebElement username = driver.findElement(By.name("userName"));
		username.sendKeys("Ajin");
		WebElement pass = driver.findElement(By.name("password"));
		pass.sendKeys("123456");
		WebElement submit = driver.findElement(By.name("submit"));
		submit.click();
	}

	@Test(priority = 3, enabled = false)
	public void verifyRegister() {
		WebElement register1 = driver.findElement(By.xpath("//a[text()='REGISTER']"));
		register1.click();
	}

	@DataProvider(name = "userCredentials")
	public Object[][] dataForVerifyLogin() {
		Object data[][] = new Object[2][2];
		data[0][0] = "Ajin";
		data[0][1] = "123456";
		data[1][0] = "asd";
		data[1][1] = "cds";
		return data;
	}

	@Test(priority = 4, enabled = false)
	public void verifyContact() {
		WebElement contact = driver.findElement(By.xpath("//a[text()='CONTACT']"));
		contact.click();
	}
}
