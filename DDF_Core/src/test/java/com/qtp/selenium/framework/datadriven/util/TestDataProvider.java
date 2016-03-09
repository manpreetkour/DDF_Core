package com.qtp.selenium.framework.datadriven.util;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.qtp.selenium.framework.datadriven.TestBase;

public class TestDataProvider {
	
	@DataProvider(name="PortfolioDataProvider")
	public static Object[][] getDataSuiteA(Method m){
		TestBase.init();
		System.out.println(TestBase.prop.getProperty("xlsFileLocation"));
		Xls_Reader xls1 = new Xls_Reader(TestBase.prop.getProperty("xlsFileLocation")+Constants.PORTFOLIO_SUITE+".xlsx");

		return Utility.getData(m.getName(), xls1);
	}
	
	@DataProvider(name="StockDataProvider")
	public static Object[][] getDataSuiteB(Method m){
		TestBase.init();
		Xls_Reader xls1 = new Xls_Reader(TestBase.prop.getProperty("xlsFileLocation")+Constants.STOCK_SUITE+".xlsx");

		return Utility.getData(m.getName(), xls1);
	}
	

}
