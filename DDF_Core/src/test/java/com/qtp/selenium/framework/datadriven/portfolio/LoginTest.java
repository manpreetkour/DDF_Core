package com.qtp.selenium.framework.datadriven.portfolio;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qtp.selenium.framework.datadriven.TestBase;
import com.qtp.selenium.framework.datadriven.util.Constants;
import com.qtp.selenium.framework.datadriven.util.TestDataProvider;

public class LoginTest extends TestBase {

	@Test(dataProviderClass=TestDataProvider.class,dataProvider = "PortfolioDataProvider")
	public void loginTest(Hashtable<String, String> table) {
		APPLICATION_LOG.debug("Executing LoginTest");
		validateRunmodes("LoginTest", Constants.PORTFOLIO_SUITE,table.get(Constants.RUNMODE_COL));
		System.out.println("Run Mode validated for LoginTest");
		
		/*openBrowser(table.get(Constants.BROWSER_COL));
		navigate("testSiteURL");
		click("moneyLink_xpath");
		click("myPortfolio_xpath");
		Assert.assertTrue(verifyTitle("portfolioPage"), "Titles dont match !! -- Got:  " + driver.getTitle());
		input("loginUsername_xpath", table.get(Constants.USERNAME_COL));
		click("continueLogin_xpath");
		input("loginPassword_xpath", table.get(Constants.PASSWORD_COL));
		click("loginButton_xpath");
		*/
		
		doLogin(table.get(Constants.BROWSER_COL), table.get(Constants.USERNAME_COL), table.get(Constants.PASSWORD_COL));
		
		boolean signoutLink=isElementPresent("signout_xpath");
		if(!(((table.get(Constants.EXPECTEDRESULT_COL).equalsIgnoreCase("SUCCESS")) && signoutLink))){
			Assert.fail("Not able to login with correct credentials");
		} else if ((table.get(Constants.EXPECTEDRESULT_COL).equalsIgnoreCase("FAIL")) && signoutLink)
			Assert.fail("Logged in with correct credentials");
	}
}