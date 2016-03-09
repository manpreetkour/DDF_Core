package com.qtp.selenium.framework.datadriven.portfolio;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.qtp.selenium.framework.datadriven.TestBase;
import com.qtp.selenium.framework.datadriven.util.Constants;
import com.qtp.selenium.framework.datadriven.util.TestDataProvider;

public class LeastPerAssetTest extends TestBase{
	@Test(dataProviderClass=TestDataProvider.class,dataProvider = "PortfolioDataProvider")
	public void leastPerAssetTest(Hashtable<String, String> table)
	{
		APPLICATION_LOG.debug("Executing LeastPerAssetTest");
		validateRunmodes("LeastPerAssetTest", Constants.PORTFOLIO_SUITE, table.get(Constants.RUNMODE_COL));
		System.out.println("Run Mode validated for LeastPerAssetTest");
	}	
}