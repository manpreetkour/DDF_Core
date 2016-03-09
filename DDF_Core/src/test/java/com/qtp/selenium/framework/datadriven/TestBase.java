package com.qtp.selenium.framework.datadriven;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.SkipException;

import com.qtp.selenium.framework.datadriven.util.Constants;
import com.qtp.selenium.framework.datadriven.util.Utility;
import com.qtp.selenium.framework.datadriven.util.Xls_Reader;

public class TestBase {
	public static Properties prop;
	public static Logger APPLICATION_LOG = Logger.getLogger("devpinoyLogger");
	public static WebDriver driver=null; 
	
	/*
	public void initLogs(Class<?> class1){

		FileAppender appender = new FileAppender();
		// configure the appender here, with file location, etc
		appender.setFile(System.getProperty("user.dir")+"//target//reports//"+CustomListener.resultFolderName+"//"+class1.getName()+".log");
		appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		appender.setAppend(false);
		appender.activateOptions();

		APPLICATION_LOG = Logger.getLogger(class1);
		APPLICATION_LOG.setLevel(Level.DEBUG);
		APPLICATION_LOG.addAppender(appender);
	}
	*/
	
	public static void init(){		
		if(prop == null){
			APPLICATION_LOG.debug("Inside init() .. Initializing properties file");
			String path=System.getProperty("user.dir")+"\\src\\test\\resources\\project.properties";
			
			 prop = new Properties();
			try {
				FileInputStream fs = new FileInputStream(path);
				prop.load(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}					
	
	public void validateRunmodes(String testName,String suiteName,String dataRunmode){
	    APPLICATION_LOG.debug("Validating runmode for "+testName+" in suite "+ suiteName);
		init();
		//suite runmode
		boolean suiteRunmode=Utility.isSuiteRunnable(suiteName, new Xls_Reader(prop.getProperty("xlsFileLocation")+"Suite.xlsx"));
		boolean testRunmode=Utility.isTestCaseRunnable(testName, new Xls_Reader(prop.getProperty("xlsFileLocation")+suiteName+".xlsx"));
		boolean dataSetRunmode=false;
		if(dataRunmode.equals(Constants.RUNMODE_YES))
			dataSetRunmode=true;
		
		if(!(suiteRunmode && testRunmode && dataSetRunmode)){
			APPLICATION_LOG.debug("Skipping the test "+testName+" inside the suite "+ suiteName);
			throw new SkipException("Skipping the test "+testName+" inside the suite "+ suiteName);
		}
		
	}

	/****************Generic functions*********************/
	public void openBrowser(String browserType){
	 
		if(browserType.equals("Mozilla"))
			driver= new FirefoxDriver();
		else if(browserType.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriverexe"));
			driver= new ChromeDriver();
		}		 
		
		// FOR GRID -----------------------------------------------------------------------------------
/*		DesiredCapabilities cap = new DesiredCapabilities();	// capability/configuration of particular node/browser/run
		cap.setJavascriptEnabled(true);
		if(browserType.equals("Mozilla"))
			cap.setBrowserName("firefox");
		else if(browserType.equals("Chrome"))
			cap.setBrowserName("chrome");
		cap.setPlatform(Platform.ANY);
		
		// Start remote webdriver: required for GRID
		try {
			driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
*/		
		// FOR GRID -----------------------------------------------------------------------------------
		
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
	}
	
	public void navigate(String URLKey){
		driver.get(prop.getProperty(URLKey));
	}
	
	public void click(String identifier){
		try{
		if(identifier.endsWith("_xpath"))
			driver.findElement(By.xpath(prop.getProperty(identifier))).click();
		else if(identifier.endsWith("_id"))
			driver.findElement(By.id(prop.getProperty(identifier))).click();
		else if(identifier.endsWith("_name"))
			driver.findElement(By.name(prop.getProperty(identifier))).click();
		}catch(NoSuchElementException e){
			Assert.fail("Element not found - "+identifier);
		}

	}
	
	public void input(String identifier,String data){
		try{
		if(identifier.endsWith("_xpath"))
			driver.findElement(By.xpath(prop.getProperty(identifier))).sendKeys(data);
		else if(identifier.endsWith("_id"))
			driver.findElement(By.id(prop.getProperty(identifier))).sendKeys(data);
		else if(identifier.endsWith("_name"))
			driver.findElement(By.name(prop.getProperty(identifier))).sendKeys(data);
		}catch(NoSuchElementException e){
			Assert.fail("Element not found - "+identifier);
		}
	}
	
	public boolean verifyTitle(String expectedTitleKey){
		String expectedTitle=prop.getProperty(expectedTitleKey);
		String actualTitle=driver.getTitle();
		if(expectedTitle.equals(actualTitle))
			return true;
		else
			return false;
	}
	
	public boolean isElementPresent(String identifier){
		int size=0;
		if(identifier.endsWith("_xpath"))
			size = driver.findElements(By.xpath(prop.getProperty(identifier))).size();
		else if(identifier.endsWith("_id"))
			size = driver.findElements(By.id(prop.getProperty(identifier))).size();
		else if(identifier.endsWith("_name"))
			size = driver.findElements(By.name(prop.getProperty(identifier))).size();
		else // not in prop file
			size=driver.findElements(By.xpath(identifier)).size();
		
		if(size>0)
			return true;
		else
			return false;
	}
	
	public String getText(String identifier){
		String  text="";
		if(identifier.endsWith("_xpath"))
			text = driver.findElement(By.xpath(prop.getProperty(identifier))).getText();
		else if(identifier.endsWith("_id"))
			text = driver.findElement(By.id(prop.getProperty(identifier))).getText();
		else if(identifier.endsWith("_name"))
			text = driver.findElement(By.name(prop.getProperty(identifier))).getText();
		
		return text;
		
	}
	
	public void quit(){
		if(driver!=null){
			driver.quit();
			driver=null;
		}
	}

	/*****************Application specific functions*******************/
	public void doLogin(String browser,String username,String password){
		openBrowser(browser);
		navigate("testSiteURL");
		Assert.assertTrue(isElementPresent("moneyLink_xpath"), "Element not found - moneyLink_xpath");
		click("moneyLink_xpath");
		click("myPortfolio_xpath");
		Assert.assertTrue(verifyTitle("portfolioPage"), "Titles do not match. Got title as - "+driver.getTitle());
		input("loginUsername_xpath", username);
		click("continueLogin_xpath");
		input("loginPassword_xpath", password);
		click("loginButton_xpath");
	}
	
	public void doDefaultLogin(String browser){
		doLogin(browser, prop.getProperty("defaultUsername"), prop.getProperty("defaultPassword"));
	}
	
}