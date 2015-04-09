import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import lib.cisco.util.AppTestDTO;
import lib.cisco.util.ApplicationDB;
import lib.cisco.util.DBUtill;
import lib.cisco.util.OutputBean;

import lib.cisco.util.Report;
import lib.cisco.util.variables;
import static lib.cisco.util.DBUtill.*;
import static lib.cisco.util.Report.projectPath;
import static lib.cisco.util.Report.scrshotPath;


//import static lib.cisco.util.Report.projectPath;

import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.basic.api.internal.TestType;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.browser.api.BrowserSettings.BrowserType;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import lib.*;


public class script extends IteratingVUserScript {
	@ScriptService oracle.oats.scripting.modules.utilities.api.UtilitiesService utilities;
	@ScriptService oracle.oats.scripting.modules.browser.api.BrowserService browser;
	@ScriptService oracle.oats.scripting.modules.functionalTest.api.FunctionalTestService ft;
	@ScriptService oracle.oats.scripting.modules.webdom.api.WebDomService web;
	
	
	
	Calendar startTime;
	Calendar endTime;	
	String strtDateTime;
	String endDateTime;
	BufferedWriter writer=null;
	public static long tcStartTime;
	public static long tcendTime;
	
	String ScreenshotName="";
	
	private Report report = new Report();
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	@FunctionLibrary("ApplicationDBUtility") lib.cisco.util.ApplicationDBUtility applicationDBUtility;
	@FunctionLibrary("ApplicationUtility") lib.cisco.util.ApplicationUtility applicationUtility;
	@FunctionLibrary("CommonUtility") lib.cisco.util.CommonUtility commonUtility;
	
	
	public void initialize() throws Exception {
		System.out.println("Started");
		
	}
		
	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws Exception {

	}
	/*
	 * Scenario:Scenario_1
	 * TestCase:Login
	 * This test case test the Login Functionality of the application with valid and invalid credentials and updates the execution status in the Execution Table.
	 */
	public void EBS_SCM_Scenario1_Login() throws Exception{
		
		/*---------------------ATOM CODE-----------------------------------*/
		System.out.println("variables in EBS_SCM_Scenario1_Login:"+variables.getTableName()+":"+variables.getReleaseId()+":"+variables.getAppId());
	 
		AppTestDTO appdata=null;
		ApplicationDB AppDBobj=new ApplicationDB();
		appdata=AppDBobj.getTestDataForDataId();
		OutputBean ob=new OutputBean();
		
		
		
			try
			{
				ScreenshotName=(appdata.getTestClassName()+"_"+appdata.getTestMethodName()+"-"+appdata.getDataId());
				System.out.println("Reports Dir in EBS:"+variables.getReportDir());
				writer=report.createlogfile(appdata.getTestMethodName(),appdata.getDataId(),variables.getReportDir());
				
			tcStartTime=System.currentTimeMillis();
			startTime= Calendar.getInstance();			
			strtDateTime= formatter.format(startTime.getTime());
			
			System.out.println("start time:"+tcStartTime);
			
			
			if(appdata.getBrowserType().equalsIgnoreCase("iexplorer") || appdata.getBrowserType().equalsIgnoreCase("IE") || appdata.getBrowserType().equalsIgnoreCase("InternetExplorer"))
			{
				browser.setBrowserType(BrowserType.InternetExplorer);
			}
			else if(appdata.getBrowserType().equalsIgnoreCase("ff") || appdata.getBrowserType().equalsIgnoreCase("firefox"))
			{
				browser.setBrowserType(BrowserType.Firefox);
			}
			browser.launch();
			
			
			System.out.println("BrowserId:"+browser.getBrowserProcessID());
			
			/*---------------------ATOM CODE-----------------------------------*/
			
			//Place your code here 
			//web.textBox("").setText(appdata.getKey());
			ob=applicationUtility.userLogin(ob,writer,appdata);
			
			//Place your code here 
			/*---------------------ATOM CODE-----------------------------------*/
			
			}
			catch(Exception e)
			{
				System.out.println("catch");
				
				String errmsg=e.getMessage();
				if(errmsg.length() > 500){
					errmsg=errmsg.substring(0,500);
					
					
			}
		ob.setErrorMsg(errmsg);
			ob.setStatus("Failed");
				
				report.logMessage(writer,ScreenshotName,"Test Execution Failed","Exception Occurred", "Exception Occurred "+errmsg, "Failed");
			}
			
			
				finally
				{
					browser.close();
					System.out.println("finally");
					
					tcendTime=System.currentTimeMillis();
					endTime = Calendar.getInstance();				
					endDateTime = formatter.format(endTime.getTime());	
					System.out.println(endDateTime);
					System.out.println("Screenshot path in tc:"+scrshotPath);
					ob.setScreenshotPath(scrshotPath);
					System.out.println("check to update or not");
					if("".equalsIgnoreCase(variables.getTestingType())|| variables.getTestingType().equalsIgnoreCase("release")){
						System.out.println("updating Execution table");
				AppDBobj.ResultMapAndDBUpdate(ob, variables.getTrackId(), Integer.parseInt(variables.getReleaseId()),Integer.parseInt(variables.getAppId()), strtDateTime, endDateTime, appdata);
					}
					System.out.println("Closing reports");
					report.after(writer,tcStartTime,tcendTime,appdata.getOsType(),appdata.getBrowserType(),appdata.getBrowserVersion());		
					report.closewriter(writer);
					System.out.println("End");
					
				}
				/*---------------------ATOM CODE-----------------------------------*/
	
		}

	public void finish() throws Exception {
	}
}
