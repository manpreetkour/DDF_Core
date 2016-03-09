package com.qtp.selenium.framework.datadriven;

import java.io.File;

import org.testng.internal.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.qtp.selenium.framework.datadriven.util.Constants;
import com.qtp.selenium.framework.datadriven.util.ErrorUtil;
import com.qtp.selenium.framework.datadriven.util.Xls_Reader;


public class CustomListener extends TestListenerAdapter implements IInvokedMethodListener,ISuiteListener{
	public static Hashtable<String,String> resultTable ;
	public static ArrayList<String> keys;
	public static String resultFolderName;
	public static String resultFilePath;
	
	public void onTestFailure(ITestResult tr){
	//	report(tr.getName(), tr.getThrowable().getMessage());
		
		List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
		String errMsg="";
		for(int i=0;i<verificationFailures.size();i++){
			errMsg=errMsg+"["+verificationFailures.get(i).getMessage()+"]-";
		}
		report(tr.getName(), errMsg);

	}
	
	public void onTestSkipped(ITestResult tr) {
		report(tr.getName(), tr.getThrowable().getMessage());
		

	}
	
	public void onTestSuccess(ITestResult tr){
		report(tr.getName(), "PASS");
		
	}
	
	public void afterInvocation(IInvokedMethod method, ITestResult result) {
		
		Reporter.setCurrentTestResult(result);

		if (method.isTestMethod()) {
			List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
			//if there are verification failures...
			if (verificationFailures.size() != 0) {
				//set the test to failed
				result.setStatus(ITestResult.FAILURE);
				
				//if there is an assertion failure add it to verificationFailures
				if (result.getThrowable() != null) {
					verificationFailures.add(result.getThrowable());
				}
 
				int size = verificationFailures.size();
				//if there's only one failure just set that
				if (size == 1) {
					result.setThrowable(verificationFailures.get(0));
				} else {
					//create a failure message with all failures and stack traces (except last failure)
					StringBuffer failureMessage = new StringBuffer("Multiple failures (").append(size).append("):nn");
					for (int i = 0; i < size-1; i++) {
						failureMessage.append("Failure ").append(i+1).append(" of ").append(size).append(":n");
						Throwable t = verificationFailures.get(i);
						String fullStackTrace = Utils.stackTrace(t, false)[1];
						failureMessage.append(fullStackTrace).append("nn");
					}
 
					//final failure
					Throwable last = verificationFailures.get(size-1);
					failureMessage.append("Failure ").append(size).append(" of ").append(size).append(":n");
					failureMessage.append(last.toString());
 
					//set merged throwable
					Throwable merged = new Throwable(failureMessage.toString());
					merged.setStackTrace(last.getStackTrace());
 
					result.setThrowable(merged);
					
				}
			}
		
		}
		
		
	}
 
	public void beforeInvocation(IInvokedMethod arg0, ITestResult test) {
		
	}

	@Override
	public void onStart(ISuite suite) {
		System.out.println("Starting suite "+ suite.getName());
		if(resultTable==null){
			keys = new ArrayList<String>();
			resultTable  = new Hashtable<String,String>();
		}
		
		if(resultFolderName==null){// first Suite
			Date d = new Date();
			resultFolderName=d.toString().replace(":", "_");
			File f = new File(System.getProperty("user.dir")+"//target//reports//"+resultFolderName);
			f.mkdir();
			// change
			resultFilePath=System.getProperty("user.dir")+"//target//reports//"+resultFolderName+"//Report.xlsx";
			File src = new File(System.getProperty("user.dir")+"//target//reports//ReportTemplate.xlsx");
			File dest = new File(resultFilePath);
			try {
				FileUtils.copyFile(src, dest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFinish(ISuite suite) {
		if(resultTable !=null){
			System.out.println("Finishing suite "+ suite.getName());
			System.out.println(resultTable);
			System.out.println(keys);
			
			// write results in xlsx
			if(!suite.getName().equals(Constants.ROOT_SUITE)){
				Xls_Reader xls = new Xls_Reader(resultFilePath);
				xls.addSheet(suite.getName());
				// add the results in the sheet
				
				// create col Names
				xls.setCellData(suite.getName(), 0, 1, "Test Case");
				xls.setCellData(suite.getName(), 1, 1, "Result");
				
				for(int i=0;i<keys.size();i++){
					String key = keys.get(i);
					String result = resultTable.get(key);
					xls.setCellData(suite.getName(), 0, i+2, key);
					xls.setCellData(suite.getName(), 1, i+2, result);

				}
				
			}
			
			resultTable=null;
			keys=null;
		}
		
	}
	
	public void report(String name,String result){
		// test Iteration 1
		// test Iteration 2
		int iteration_number=1;
		while(resultTable.containsKey(name+" Iteration "+iteration_number)){
			iteration_number++;
		}
		keys.add(name+" Iteration "+iteration_number);
		resultTable.put(name+" Iteration "+iteration_number, result);

	}
			
	

}
