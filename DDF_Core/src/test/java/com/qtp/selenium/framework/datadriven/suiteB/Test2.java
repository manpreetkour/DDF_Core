package com.qtp.selenium.framework.datadriven.suiteB;

import java.util.Hashtable;

import org.testng.annotations.Test;

import com.qtp.selenium.framework.datadriven.TestBase;
import com.qtp.selenium.framework.datadriven.util.Constants;
import com.qtp.selenium.framework.datadriven.util.TestDataProvider;

public class Test2 extends TestBase{
	@Test(dataProviderClass=TestDataProvider.class,dataProvider = "StockDataProvider")
	public void test2(Hashtable<String, String> table)
	{
		validateRunmodes("Test2", Constants.STOCK_SUITE, table.get(Constants.RUNMODE_COL));
		System.out.println("Run Mode validated for test2");
	}		
}