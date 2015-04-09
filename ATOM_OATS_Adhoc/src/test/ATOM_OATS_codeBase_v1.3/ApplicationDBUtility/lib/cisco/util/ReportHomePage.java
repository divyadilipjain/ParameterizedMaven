package lib.cisco.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportHomePage {
		private static String mainReportLoaction = ""; 
		private static String reportLogLoaction  = "";
		private static final Logger  LOGGER= Logger.getLogger(ReportHomePage.class.getName());
		
	private ReportHomePage(){
		
			
	}

	public static void createReportHomePage(String startTime, String endTime, long executionTime,String reportFolderDateTime) {
		mainReportLoaction = Report.projectPath+"\\Reports\\Reports("+reportFolderDateTime+")\\mainReport.html";
		LOGGER.log(Level.INFO, "mainReportLoaction=="+mainReportLoaction);
		reportLogLoaction  = Report.projectPath+"\\Reports\\Reports("+reportFolderDateTime+")\\logs";
		try{
			File report = new File(mainReportLoaction);
			if(report.exists()){
				report.delete();
			}else{
				report.createNewFile();
			}
			
			FileWriter fw = new FileWriter(mainReportLoaction,true); 
			fw.write(formatTheData(getRequiredDataFromDetailedLog(getTheRequiredDetailedReportName()),startTime, endTime, executionTime));
			fw.close();
			LOGGER.log(Level.INFO, "Sucess: Main report created sucessfully");
		}catch(Exception e){
			LOGGER.log(Level.SEVERE, "Error: Main report creation failed :",e);
			
		}
		
	}

	public static String formatTheData(List<ArrayList<String>> allData, String TestExecutionStartTime,String TestExecutionEndTime, long executionTime){
		List<String> reportHeader = new ArrayList<String>();
		List<ArrayList<String>> reportBody = new ArrayList<ArrayList<String>>();
		String finalReport = "";
		//int NumberOfPASSED=0;
		//int numberOfFailed=0;

		// **********************create the Header***************************//

		List<Date> testExecutedList = new ArrayList<Date>();
		String totalExecutionTime = "";
		for(int i=0; i<allData.size(); i++){
			try{
				Date date=new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				date = dateFormat.parse(allData.get(i).get(0));
				testExecutedList.add(date);
			}catch(Exception e){
				LOGGER.log(Level.SEVERE, "Exception :",e);
			}
		}
		
		//reportHeader.add(todayDate);
		//Start and end time we will get from runner
		reportHeader.add(TestExecutionStartTime);
		reportHeader.add(TestExecutionEndTime);

		//count the number of pass in the arraylist
		for(int i=0; i<allData.size(); i++){
			try{
				if("Passed".equalsIgnoreCase(allData.get(i).get(3))){
					//NumberOfPASSED++;
				}else{
					//numberOfFailed++;
				}
			}catch(Exception e){
				LOGGER.log(Level.SEVERE, "Exception :",e);
			}
		}
		
		long seconds, minutes,secRemaining;
		seconds = executionTime / 1000;
		seconds = seconds % 3600;
		minutes = seconds / 60;
		minutes = minutes % 60;
		secRemaining=(seconds-(minutes*60));
		
		totalExecutionTime = String.valueOf(minutes+" mins :"+secRemaining+" secs");
		reportHeader.add(totalExecutionTime);

		// **********************create the body***************************//

		for(int i=0; i<allData.size(); i++){
			ArrayList<String> reportBodyLineLevel = new ArrayList<String>();
			reportBodyLineLevel.add(allData.get(i).get(7));
			reportBodyLineLevel.add(allData.get(i).get(4));
			reportBodyLineLevel.add(allData.get(i).get(5));
			reportBodyLineLevel.add(allData.get(i).get(6));
			if("Passed".equalsIgnoreCase(allData.get(i).get(3))){
				reportBodyLineLevel.add("green");
			}else{
				reportBodyLineLevel.add("red");
			}
			if("Passed".equalsIgnoreCase(allData.get(i).get(3))){
				reportBodyLineLevel.add("Passed");
			}else{
				reportBodyLineLevel.add("Failed");
			}
			reportBody.add(reportBodyLineLevel);
		}
		finalReport = createMainReport(reportHeader,reportBody);
		return finalReport;
	}

	
	public static List<ArrayList<String>> getRequiredDataFromDetailedLog(LinkedHashSet<String> ListOfFiles){
		Iterator<String> itr = ListOfFiles.iterator();
		String report = "";
		List<ArrayList<String>> dataSetPerTestReport = new ArrayList<ArrayList<String>>();

		do{
			String file = itr.next();
			ArrayList<String> dataSet = new ArrayList<String>();
			report = getReport(file);
			try{
				dataSet.add(getStringFromReport(report,file, "Test Executed On</td><td class ='summarybody' width=450>", "<"));
				dataSet.add(getStringFromReport(report,file, "Test Execution Start Time</td><td class ='summarybody' width=450>", "<"));
				dataSet.add(getStringFromReport(report,file, "Test Execution End Time</td><td class ='summarybody'>", "<"));
				if(report.contains("tick.png") && !report.contains("cross.png")){
					dataSet.add("Passed");
				}else{
					dataSet.add("Failed");
				}
				dataSet.add(getStringFromReport(report,file, "<TD class='tsgen' width=155px>", "<"));
				dataSet.add(getStringFromReport(report,file, "Browser Type</td><td class ='summarybody'>", "<"));
				dataSet.add(getStringFromReport(report,file, "Total Execution Time</td><td class ='summarybody'>", "<"));
				//dataSet.add(getStringFromReport(report, "com.cisco.testscripts.", ".png"));
				dataSet.add(file);
			}catch(Exception e){
				LOGGER.log(Level.SEVERE, "Error occured : While fetching data from log.",e);
			}
			dataSetPerTestReport.add(dataSet);
		}while(itr.hasNext());

		return dataSetPerTestReport;
	}


	//===== Get The Required detailed report.
	/**
	 * Get The Required detailed report names
	 * @return created reports name in the log folder 
	 */
	public static LinkedHashSet<String> getTheRequiredDetailedReportName(){
		List<String> reportNameList = new ArrayList<String>();
		List<String> ReportList = new ArrayList<String>();

		//==== get the list of file present
		File folder = new File(reportLogLoaction);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				reportNameList.add(listOfFiles[i].getName());
			} 
		}
		
		
		//==== Get the recent report log file
		for(int i=0; i < reportNameList.size(); i++){
			String dataId = "";
			int noOfReport = 0;
			String[] reportName = reportNameList.get(i).split("_DataId_");
			for(int j=0; j<reportNameList.size(); j++){
				if(reportNameList.get(j).contains(reportName[0])){
					noOfReport++;
					String[] getDataId = reportName[1].split("_");
					dataId = getDataId[0];
				}
			}

			if(noOfReport==2){
				ReportList.add(reportName[0]+"_DataId_"+dataId+"_2");
			}else{
				ReportList.add(reportName[0]+"_DataId_"+dataId+"_1");
			}
		}

		//==== remove the duplicate
		LinkedHashSet<String> finalReportList = new LinkedHashSet<String>();
		//finalReportList.addAll(ReportList);
		finalReportList.addAll(reportNameList);
		ReportList.clear();
		ReportList.addAll(reportNameList);

		return finalReportList;
	}

	//==== fetch the HTML report
	public static String getReport(String fileName){
		String filePath = reportLogLoaction+"\\"+fileName;
		String report = "";
		String line = "";
		try{
			FileReader inputFile = new FileReader(filePath);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			while ((line = bufferReader.readLine()) != null)   {
				report = report+line.trim();
			}
			bufferReader.close();
		}catch(Exception e){
			LOGGER.log(Level.SEVERE,"Error while reading file line by line:" + e.getMessage());
		}
		return report;
	}
	//==== fetch the data from report's HTML Tags
	public static String getStringFromReport(String report,String fileName, String firstString, String secondString){
		String middleString = "";
		try{
			String[] splitReport = report.split(firstString);
			String[] splitAgainReport = splitReport[1].split(secondString);
			middleString = splitAgainReport[0];
		}catch(Exception e){
			LOGGER.log(Level.INFO, "Please provide at least one log message in the log file >> "+fileName);
			//LOGGER.log(Level.SEVERE, "Exception Occured : >> ",e);
		}
			
		return middleString;
	}
	//===== Creating the final Report
	public static String createMainReport(List<String> reportHeader, List<ArrayList<String>> reportBody){
		int totalTcs=0;
		int noOfPassed=0;
		int noOfFailed=0;
		String createReportBody = "";
		String newFileName="";
		for(int i=0; i<reportBody.size(); i++){
			String reportbody = ReportBody;
			String fileName=reportBody.get(i).get(0);
			if(fileName.contains("_1.")){
				newFileName=fileName.replace("_1.", "_PrimaryExecution.");
				totalTcs++;
			}else if(fileName.contains("_2.")){
				newFileName=fileName.replace("_2.", "_SecondayExecution.");
			}
			if("Passed".equalsIgnoreCase(reportBody.get(i).get(5))){
				noOfPassed++;
			}
			
			createReportBody =  createReportBody 
					+ reportbody.replace("$$SNo$$", String.valueOf(i+1))
					.replace("$$DeatiledExecutionReportFileLink$$", reportBody.get(i).get(0))
					.replace("$$FileName$$", newFileName)
					.replace("$$TestCaseName$$", reportBody.get(i).get(1))
					.replace("$$BrowserType$$", reportBody.get(i).get(2))
					.replace("$$ExecutionTime$$", reportBody.get(i).get(3))
					.replace("$$StatusColor$$", reportBody.get(i).get(4)) // green, red
					.replace("$$Status$$", reportBody.get(i).get(5));     // Pass,fail
		}
		noOfFailed=totalTcs-noOfPassed;
		LOGGER.log(Level.INFO, "totalTcs=="+totalTcs);
		LOGGER.log(Level.INFO, "noOfPassed=="+noOfPassed);
		LOGGER.log(Level.INFO, "noOfFailed=="+noOfFailed);
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		
		float passPercentage=(noOfPassed*100.0f)/totalTcs ;
		Double passPer = Double.parseDouble(decimalFormat.format(passPercentage));
		float failPercentage=(noOfFailed*100.0f)/totalTcs ;
		Double failedPer = Double.parseDouble(decimalFormat.format(failPercentage));
		
		ReportHeader = ReportHeader.replace("$$TestExecutionStartTime$$", reportHeader.get(0))
				.replace("$$TestExecutionEndTime$$", reportHeader.get(1))
				.replace("$$TotalExecutionTime$$", reportHeader.get(2))
				.replace("$$NumberOfTestScript$$", String.valueOf(totalTcs))
				.replace("$$NumberOfPASSED$$", noOfPassed+"("+passPer+"%)")
				.replace("$$NumberOfFAILED$$", noOfFailed+"("+failedPer+"%)");
		String mainReport = ReportHeader+createReportBody+ReportFooter;

		return mainReport;

	}

	
	//HTML
	static String ReportHeader = "<html>" +
			"<head>" +
			"<link href='..\\css\\style.css' rel='stylesheet' type='text/css' />" +
			"</head>" +
			"<hr class='divline'>" +
			"<table class='reportheader' width=100%>" +
			"<TR>" +
			"<td height=50px align=left>" +
			"<Table class='developer'>" +
			"<TR>" +
			"<td class='desc1'>" +
			"<Center>" +
			"<table>" +
			"<TR>" +
			"<TD class='desccpy'>Automation TestExecution Report</TD>" +
			"</TR>" +
			"<TR>" +
			"<TD class='dev'>Tool Used : OATS </TD>" +
			"</TR>" +
			"</Table>" +
			"</TD>" +
			"</TR>" +
			"</Table>" +
			"</TD>" +
			"<BR>" +
			"<td height=50pxalign=right><img src = '..\\Images\\cisco_logo.jpg'>" +
			"</td>" +
			"<BR>" +
			"</tr>" +
			"</table>" +
			"<hr class='divline'>" +
			"<BR>" +
			"<table class='subheader' width=100%>" +
			"<tr>" +
			"<td width=100% class='subheader'>Test Execution Summary</td>" +
			"</tr>" +
			"<tr>" +
			"<td width=100% class='subcontents'>" +
			"</td>" +
			"</tr>" +
			"</tr>" +
			"</table>" +
			"<hr class='divline'>" +
			"<br>" +
			"<table>" +
			"<tr>" +
			"<td>" +
			"<table class = 'releasesummary'>" +
			"<tr>" +
			"<td class='summaryheader'colspan=2>Execution Time Summary</td>" +
			"</tr>" +
			"<tr>" +
			"<td class ='summaryelement' width=100>Test Execution Start Time</td>" +
			"<td class ='summarybody'width=450>$$TestExecutionStartTime$$</td>" +
			"</tr>" +
			"<tr>" +
			"<td class ='summaryelement' width=100>Test Execution End Time</td>" +
			"<td class ='summarybody'width=450>$$TestExecutionEndTime$$</td>" +
			"</tr>" +
			"<tr>" +
			"<td class ='summaryelement'>Total Execution Time</td>" +
			"<td class ='summarybody'>$$TotalExecutionTime$$</td>" +
			"</tr>" +
			"</table>" +
			"</td>" +
			"<td>" +
			"</td>" +
			"<td>" +
			"<table class = 'releasesummary'>" +
			"<tr>" +
			"<td class='summaryheader'colspan=2>Execution Details Summary</td>" +
			"</tr>" +
			"<tr>" +
			"<td class ='summaryelement' width=100> # Total Test Scripts</td>" +
			"<td class ='summarybody'width=450>$$NumberOfTestScript$$</td>" +
			"</tr>" +
			"<tr>" +
			"<td class ='summaryelement'>TCs PASSED#(%)</td>" +
			"<td class ='summarybody'>$$NumberOfPASSED$$</td>" +
			"</tr>" +
			"<tr>" +
			"<td class ='summaryelement'>TCs Failed#(%)</td>" +
			"<td class ='summarybody'>$$NumberOfFAILED$$</td></tr></table>" +
			"</td>" +
			"</tr>" +
			"</table>" +
			"<br/><br/>"+
			"<hr class='divline'>" +
			"<BR>" +
			"<table class='subheader'>" +
			"<tr>" +
			"<td class='subheader' align=center >Total Scripts Execution</td>" +
			"</td>" +
			"<tr>" +
			"<td class='subcontents'>" +
			"</td>" +
			"</tr>" +
			"</table>" +
			"" +
			"<table class='teststeps'>" +
			"<tr>" +
			"<td class='tsheader' width=75px>S.No</td>" +
			"<td class='tsheader' width=100px>Test Case</td>" +
			"<td class='tsheader' width=100px>Browser Type</td>" +
			"<td class='tsheader' width=100px>Execution Time</td>" +
			"<td class='tsheader' width=100px>Status</td>" +			
			"</tr>" ;

	static String ReportBody =
			"<tr>" +
			"<td class='summarybody' >$$SNo$$</td>" +
			"<td class='summarybody' ><a href='logs\\$$DeatiledExecutionReportFileLink$$'>$$FileName$$</a></td>" +
			"<td class='summarybody' >$$BrowserType$$</td>" +
			"<td class='summarybody' >$$ExecutionTime$$</td>" +
			"<td class='summarybody' ><p style=font-family:verdana;type:bold;color:$$StatusColor$$;><b>$$Status$$</b></p></td>" +
			"</tr>";

	static String ReportFooter = "</table>" +
			"</br>" +
			"</br>" +
			"<hr class='divline'>" +
			"<br>" +
			"<center>" +
			"<span>CISCO CONFIDENTIAL - Copyright &copy; www.cisco.com </span></center></body></html>" ;
	
	
	
	
}