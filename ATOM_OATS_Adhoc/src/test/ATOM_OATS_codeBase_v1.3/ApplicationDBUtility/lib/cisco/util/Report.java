package lib.cisco.util;


import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class Report {
	
	static int value=1;
	static Properties commonProperties = PropertiesFileReader.getInstance().readProperties("common.properties");
	public static String projectPath = commonProperties.getProperty("projectPath").trim();
	
	public static String needPassedScreenshot=commonProperties.getProperty("needPassedScreenshot").trim();
	String path12 = projectPath;
	//public static String path12 = System.getProperty("user.dir");
	String projectName = path12.substring(path12.lastIndexOf("\\") + 1);
	String fileName = null;
	private static String reportsDirectoryPath = null;
	static String errorMsg = "";
	static String logMsg = "";
	protected static int count1 = 1;
	static int imageCounter=1;
	static String dateTime="";
	//New variables
	static String errorSummary=null;
	static String errorMsgCode=null;
	static String errorStepToRep=null;
	static double totalTimeTaken = 0.00;
	static String imageName;
	static int imgcount=1;
	private static int count = 1;
	static int passedcount=0;
	static int failedcount=0;
	public static String scrshotPath="No Screenshot Captured";
	static String reportDir="";
	private static final Logger LOGGER = Logger.getLogger(Report.class.getName());
	public Report() {
		
	}
	public void setReportDirPath(String path){
		reportsDirectoryPath = path;
	}
	
	public static void clearErrorSumm(){
		errorMsgCode="";
		errorStepToRep="";
		errorMsgCode="";
	}
	public String getReportDirPath(){
		return reportsDirectoryPath;
	}
	
	public static void clearErrorMsg(){
		errorMsg = "";
		logMsg = "";
	}
	public static String getErrorMsgCode() {
		return errorMsgCode;
	}
	public static void setErrorMsgCode(String errMsgcode) {
		errorMsgCode = errMsgcode;
	}
	public static String getErrorStepToRep() {
		return errorStepToRep;
	}
	public static void setErrorStepToRep(String errStepToRep) {
		errorStepToRep = errStepToRep;
	}
	public static String getErrorSummary() {
		return errorSummary;
	}
	public static  void setErrorSummary(String errSummary) {
		errorSummary = errSummary;
	}
	public static String getErrorMsg(){
		return errorMsg;
	}
	
	public static void setErrorMsg(String error) {
		errorMsg = error;
	}
	
	public static String getDateTime() {
		return dateTime;
	}
	public static void setDateTime(String dateTime) {
		Report.dateTime = dateTime;
	}
	
	public void createReportFolders(){
		createFolders();
	}
	
	public BufferedWriter createlogfile(String Filename,String dataId,String reportDir) {
		System.out.println("In create log file");
		BufferedWriter writer = null;
		String fileName="";
		int count=1;
		value=1;
		
		reportsDirectoryPath=reportDir;
		System.out.println("Report Dir path in create log file:"+reportsDirectoryPath+":"+reportDir);
		boolean flag = false;
		String DateTime=getCurrentTime(System.currentTimeMillis());
		try {
			File dir = new File(reportsDirectoryPath);
			if (!dir.exists()) {
				dir.mkdir();
			}

			String logsDirectoryPath = reportsDirectoryPath+ System.getProperty("file.separator") + "logs";
			System.out.println("Logs Path:"+logsDirectoryPath);
			dir = new File(logsDirectoryPath);
			if (!dir.exists()) {
				dir.mkdir();
			}
			System.out.println("Logs path created");
			String path=reportsDirectoryPath
					+ System.getProperty("file.separator") + "logs"
					+ System.getProperty("file.separator") + Filename + "_"
					+ "DataId" + "_" + dataId +"_"+count+ ".html";
			System.out.println("HTML path:"+path);
			//fileName = Filename + "_" + "DataId" + "_" + dataId +"_"+count+ ".html";
			File file = new File(path);
			if (file.exists()) {
				//file.delete();
				count=count+1;
				path=reportsDirectoryPath
						+ System.getProperty("file.separator") + "logs"
						+ System.getProperty("file.separator") + Filename + "_"
						+ "DataId" + "_" + dataId +"_"+count+ ".html";
				System.out.println("Path in create Log File:"+ path);
				file=new File(path);
				//fileName = Filename + "_" + "DataId" + "_" + dataId +"_"+count+ ".html";
			}
			
			//this.fileName=fileName;
			System.out.println("File path=="+path);
			flag = file.createNewFile();
			writer = new BufferedWriter(new FileWriter(file));

			writer.write("<html><head><link href='..\\..\\css\\style.css' rel='stylesheet' type='text/css' /></head><hr class='divline'>"
					+ "<table class='reportheader' width=100%><TR>"
					+ "<td height=50px align=left>"
					+ "<Table class='developer'>"
					+ "<TR><td class='desc1'><Center><table>"
					+ "<TR><TD class='desccpy'>Automation Test Execution Report</TD></TR><TR>"
					+ "<TD class='dev'>Tool Used : OATS </TD></TR></Table>"
					+ "</TD></TR></Table></TD><BR><td height=50px align=right><img src = '..\\..\\images\\cisco_logo.jpg'></td><BR></tr></table>"
					+ "<hr class='divline'><BR><table class='subheader' width=100%><tr><tr><td width=100% class='subheader'>Test Case Description - "+Filename+"</td></tr><tr><td width=100% class='subcontents'></td></tr></tr></table> <hr class='divline'><BR>");
			writer.write("<table class='teststeps' width=100%><tr><td class='tsheader' width=75px>Step #</td><TD class='tsheader' width=155px>Step Description</TD><TD class='tsheader' width=285px>Expected Result</TD><TD class='tsheader' width=285px>Actual Result</TD><TD class='tsheader' width=50px>Status</TD><TD class='tsheader' width=50px>Screen shot</TD></tr>");
			} catch (Exception e) {
				//ioe.printStackTrace();
				LOGGER.log(Level.SEVERE, "Exception :",e);
			}
		imageCounter = 1;
		return writer;
	}
	public String createFolders(){
		String reportDir="";
		try{
			Date Now = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss-a-zzz");
			//Returns Current Data and Time
			String currentDateTime = ft.format(Now);
			System.out.println("Execution Date & Time :" + currentDateTime);
			setDateTime(currentDateTime);
			String path = projectPath+ System.getProperty("file.separator") + "Reports";
			File file = new File(path);
			if (file.exists()) {
				setReportDirPath(path);
			} else {
				file.mkdir();	
				setReportDirPath(path);
			}
			reportDir=countExecution(currentDateTime);
		}catch(Exception e){
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}
		
		return reportDir;
	}
	public String countExecution(String currentDateTime) {
		try {
			count = 1;
			String reportsDirectoryPath = getReportDirPath();
			File dir = new File(reportsDirectoryPath+System.getProperty("file.separator")+"Reports("+currentDateTime+")");
			if (!dir.exists()) {
				//create reports folder
				dir.mkdir();
				//Create logs folder
				String directoryPath = reportsDirectoryPath+ System.getProperty("file.separator")+"Reports("+currentDateTime+")"+ System.getProperty("file.separator")+"logs";
				dir = new File(directoryPath);
				dir.mkdir();
				
				//Create images folder
				directoryPath = reportsDirectoryPath+ System.getProperty("file.separator")+"Reports("+currentDateTime+")" + System.getProperty("file.separator")+"images";
				dir = new File(directoryPath);
				dir.mkdir();
				
				directoryPath = reportsDirectoryPath+ System.getProperty("file.separator") + "images";
				dir = new File(directoryPath);
				dir.mkdir();
				//copy logo
				String surFile =  projectPath+ System.getProperty("file.separator")
									+ "images"+System.getProperty("file.separator")+"cisco_logo.jpg";
				String desFile = reportsDirectoryPath+ System.getProperty("file.separator")+ "images"+System.getProperty("file.separator")+"cisco_logo.jpg";
				copyFile(surFile,desFile);
				
				surFile =  projectPath + System.getProperty("file.separator")+ "images"+System.getProperty("file.separator")+"cross.png";
				desFile = reportsDirectoryPath+ System.getProperty("file.separator") + "images"+System.getProperty("file.separator")+"cross.png";
				copyFile(surFile,desFile);
				
				surFile =  projectPath + System.getProperty("file.separator")+ "images"+System.getProperty("file.separator")+"tick.png";
				desFile = reportsDirectoryPath+ System.getProperty("file.separator") + "images"+System.getProperty("file.separator")+"tick.png";
				copyFile(surFile,desFile);
				
				surFile =  projectPath + System.getProperty("file.separator")+ "images"+System.getProperty("file.separator")+"Screenshot.jpg";
				desFile = reportsDirectoryPath+ System.getProperty("file.separator") + "images"+System.getProperty("file.separator")+"Screenshot.jpg";
				copyFile(surFile,desFile);
				
				//Create css folder
				directoryPath = reportsDirectoryPath+ System.getProperty("file.separator") + "css";
				dir = new File(directoryPath);
				dir.mkdir();
				//copy css template
				surFile =  projectPath + System.getProperty("file.separator")+ "css"+System.getProperty("file.separator")+"style.css";
				desFile = reportsDirectoryPath+ System.getProperty("file.separator") + "css"+System.getProperty("file.separator")+"style.css";;
				copyFile(surFile,desFile);
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception :",e);
			
		}
		reportDir=reportsDirectoryPath+System.getProperty("file.separator")+"Reports("+currentDateTime+")";
		return reportDir;
	}
	
	public void copyFile(String sourceFile, String targetFile) {
		File f = new File(sourceFile);
		try {
			if(f.exists()){
				InputStream in = new FileInputStream(f);
				OutputStream out = new FileOutputStream(targetFile);
				int c;

				while ((c = in.read()) != -1)
					out.write(c);

				in.close();
				out.close();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}
	}
	
	public void logMessage(BufferedWriter writer,String ScreenshotName,String stepDesc,String expectedMsg,String actualMsg,String status){
        if(status.equalsIgnoreCase("Passed")){
              log(writer,ScreenshotName,stepDesc+";"+expectedMsg+";"+actualMsg,"Passed");
        }else{
              log(writer,ScreenshotName,stepDesc+";"+expectedMsg+";"+actualMsg,"Failed");
        }
        System.out.println("Status : "+status+" :: "+actualMsg);
    }
	
	public void log(BufferedWriter writer,String ScreenshotName,String msg, String status){
		//Get the 2 messages
		int counter=1;
		if(writer !=null){
			counter=value;
		}
		String[] messages = msg.split(";");
		String stepDesc = ".";
		String expectedMsg = ".";
		String actualMsg = ".";

		stepDesc= messages[0];
		if(messages.length > 1){
			expectedMsg= messages[1];
			actualMsg= messages[2];
		}
		logMsg = actualMsg;
		addLogToFile(writer,ScreenshotName,counter, stepDesc, expectedMsg,actualMsg,status);
		value=counter+1;
	}
	public void addLogToFile(BufferedWriter writer,String ScreenshotName,int stepNum,String stepDesc,String expectedMsg,String actualMsg,String status){
		try {
			writer.write("<tr>");
			writer.write("<TD class='tsindlevel1' width=75px>" + stepNum);
			writer.write("</TD>");
			writer.write("<TD class='tsgen' width=155px>"+stepDesc+"</TD>");
			writer.write("<TD class='tsgen' width=285px>");
			writer.write(expectedMsg);
			writer.write("</td>");
			writer.write("<TD class='tsgen' width=285px>");
			writer.write(actualMsg);
			writer.write("</td>");
			if ("Passed".equalsIgnoreCase(status)) {
				if(writer !=null)
					passedcount++;
				writer.write("<TD class='tsgen' width=50px align=center><img class='screen' src = '..\\..\\images\\tick.png'></TD>");
			} else {
				writer.write("<TD class='tsgen' width=50px align=center><img class='screen' src = '..\\..\\images\\cross.png'></TD>");
				setErrorMsg(actualMsg);
				failedcount++;
			}
			if("Passed".equalsIgnoreCase(status)){
				if(needPassedScreenshot.equalsIgnoreCase("yes"))
				{
					System.out.println("Passed and taking Screenshot");
					passed_takeScreenShot(stepDesc,ScreenshotName);
					screenShotHyperLink(writer);
				}
				else
				writer.write("<TD class='tsgen' width=50px></TD>");
			}else{
				System.out.println("Failed and taking Screenshot");
				takeScreenShot(stepDesc,ScreenshotName);
				screenShotHyperLink(writer);
			}
			writer.write("</tr>");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}
	}
	public void takeScreenShot(String stepDesc,String ScreenshotName) {
		System.out.println("in take screenshot method");
		/*String imgName="";
		String imgpath="";
		 File scrFile=null;
		 int imgcount=1;
		 failedcount++;
        try {
	       // if (driver != null) {
	        	//System.out.println("ScreenshotName=="+ScreenshotName);
	        	//scrFile  = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        	 
	        	 */
		String imgName="";
		String imgpath="";
		 File scrFile=null;
		// int imgcount=1;
		 failedcount++;
		//Create images folder
			String directoryPath = reportsDirectoryPath + System.getProperty("file.separator") + "images";
			File dir = new File(directoryPath);
			if(!dir.exists())
			{
			dir.mkdir();
			}
        try {
	        	System.out.println("ScreenshotName=="+ScreenshotName);
	              //File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	              BufferedImage screenShot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	             scrFile= new File("screenShot.png");
	              System.out.println("screenshot.png created");
	      		  ImageIO.write(screenShot, "png",scrFile );
              if(!"".equalsIgnoreCase(ScreenshotName))
            	  imgName=ScreenshotName;
              else
            	  imgName=stepDesc;
              imgpath=reportsDirectoryPath+System.getProperty("file.separator")+"images"+ System.getProperty("file.separator");
            	  //imgpath=reportDir + System.getProperty("file.separator")+"images"+System.getProperty("file.separator");
             // imgpath=getReportDirPath()+System.getProperty("file.separator")+"Reports("+getDateTime()+")" + System.getProperty("file.separator")+"images"+System.getProperty("file.separator");
              System.out.println("in take Screen shot , imgpath :"+imgpath);
              
              imageName =imgName + imgcount + ".png";
              System.out.println("imageName in take screenshot:"+ imageName);
              if(new File(imgpath+imageName) .exists()){
            	  //imageCounter=imageCounter+1;
            	  imgcount=imgcount+1;
              }
              //imageName =getReportDirPath()+System.getProperty("file.separator")+"images"+ System.getProperty("file.separator")+ imgName + imgcount + ".png";
              imageName =imgName + imgcount + ".png";
              copyFileUsingStream(scrFile,   new File(imgpath+imageName));
              imageCounter++;
	        
	      
	  } catch (Exception e) {
	        LOGGER.log(Level.SEVERE, "Exception :",e);
	  }
	  scrshotPath= imgpath+imageName;
 }
	public void passed_takeScreenShot(String stepDesc,String ScreenshotName) {
		
		String imgName="";
		String imgpath="";
		 File scrFile=null;
		 
		passedcount++;
		//Create images folder
			String directoryPath = reportsDirectoryPath + System.getProperty("file.separator") + "images";
			File dir = new File(directoryPath);
			if(!dir.exists())
			{
			dir.mkdir();
			}
        try {
	        	System.out.println("ScreenshotName=="+ScreenshotName);
	              //File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	              BufferedImage screenShot = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	             scrFile= new File("screenShot.png");
	              System.out.println("screenshot.png created");
	      		  ImageIO.write(screenShot, "png",scrFile );
              if(!"".equalsIgnoreCase(ScreenshotName))
            	  imgName=ScreenshotName;
              else
            	  imgName=stepDesc;
              imgpath=reportsDirectoryPath+System.getProperty("file.separator")+"images"+ System.getProperty("file.separator");
            	  //imgpath=reportDir + System.getProperty("file.separator")+"images"+System.getProperty("file.separator");
             // imgpath=getReportDirPath()+System.getProperty("file.separator")+"Reports("+getDateTime()+")" + System.getProperty("file.separator")+"images"+System.getProperty("file.separator");
              System.out.println("in take Screen shot , imgpath :"+imgpath);
              
              imageName =imgName + imgcount + ".png";
              System.out.println("imageName in take screenshot:"+ imageName);
              if(new File(imgpath+imageName) .exists()){
            	  //imageCounter=imageCounter+1;
            	  imgcount=imgcount+1;
              }
              //imageName =getReportDirPath()+System.getProperty("file.separator")+"images"+ System.getProperty("file.separator")+ imgName + imgcount + ".png";
              imageName =imgName + imgcount + ".png";
              copyFileUsingStream(scrFile,   new File(imgpath+imageName));
              imageCounter++;
	        
	      
	  } catch (Exception e) {
	        LOGGER.log(Level.SEVERE, "Exception :",e);
	  }
	  scrshotPath= imgpath+imageName;
 }
	/*
	 * Method : copyFileUsingStream
	 */
	private  void copyFileUsingStream(File source, File dest) throws IOException {
		System.out.println("Copy File");
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	        System.out.println("Screenshot File copied");
	    } finally {
	    
	        is.close();
	        os.close();
	    }
	}
	public void screenShotHyperLink(BufferedWriter writer) {
		try {
			//String name = fileName;
			//name = name.substring(0, name.lastIndexOf("."));
			writer.write("<TD class='tsgen' width=50px align=center>");
			writer.write("<a href='..\\images\\" + imageName + "' ><img class='screen' src = '..\\..\\images\\Screenshot.jpg'></a>");
			writer.write("</td>");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}
		scrshotPath=reportsDirectoryPath+"\\images\\"+imageName;
		System.out.println("screen shot hyper link:"+scrshotPath);
	}
	
	public void after(BufferedWriter writer,long tcStartTime,long tcEndTime,String osType,String browserType,String browserVersion) {
		try{
			addTestCaseDetailsToLogfile(writer,tcStartTime,tcEndTime,osType,browserType,browserVersion);
		}catch(Exception e){
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}
	}
	
	
	public void closewriter(BufferedWriter writer) {
		try {
			writer.close();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}
	}
	public void addTestCaseDetailsToLogfile(BufferedWriter writer,long tcStartTime,long tcEndTime,String osType,String browserType,String browserVersion){
		try {
			writer.write("</table>");
			Date d1=new Date(tcStartTime);
			Date d2=new Date(tcEndTime);
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss a");
	        String date = DATE_FORMAT.format(d1);
	        String date1 = DATE_FORMAT.format(d2);
	        System.out.println("Start time==="+date);
	        System.out.println("endTime==="+date1);
	        Date today = new Date();
	        SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("dd-MM-yyyy");
	        String todayDate = DATE_FORMAT1.format(today);
	        long timeMillis = tcEndTime - tcStartTime;
			long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMillis);
			
			String status="Passed";
			if(failedcount >0){
				status ="Failed";
			}
			writer.write("<hr class='divline'><br><table class='subheader' width=100%><tr><tr><td width=100% class='subheader'>Test Execution Summary</td></tr><tr><td width=100% class='subcontents'></td></tr></tr></table>"
					+ "<hr class='divline'><br><table><tr><td><table class = 'releasesummary'><tr><td class='summaryheader' colspan=2>Execution Time Summary</td></tr><tr><td class ='summaryelement' width=100>Test Executed On</td><td class ='summarybody' width=450>"+todayDate+"</td></tr><tr><td class ='summaryelement' width=100>Test Execution Start Time</td><td class ='summarybody' width=450>"+date+"</td></tr><tr><td class ='summaryelement'>Test Execution End Time</td><td class ='summarybody'>"+date1+"</td></tr><tr><td class ='summaryelement'>Total Execution Time</td><td class ='summarybody'>"+timeSeconds+" secs</td></tr></table></td><td></td>"
					+ "<td><table class = 'releasesummary'><tr><td class='summaryheader' colspan=2>Execution Details Summary</td></tr><tr><td class ='summaryelement' width=100>Operating System</td><td class ='summarybody'width=450>"+osType+"</td></tr><tr><td class ='summaryelement'>Browser Type</td><td class ='summarybody'>"+browserType+"</td></tr><tr><td class ='summaryelement'>Browser Version</td><td class ='summarybody'>"+browserVersion+"</td></tr><tr><td class ='summaryelement'>System User</td><td class ='summarybody'>"+System.getProperty("user.name")+"</td></tr></table></td></tr></table>"
					+ "<hr class='divline'><hr class='divline'><br><table class='subheader' width=100%><tr><tr><td width=100% class='subheader' align='center'>Overall Test Status : ");
			if("Passed".equalsIgnoreCase(status)){
				writer.write("<span class = 'testpassed'>PASSED</span>");
			}else{
				writer.write("<span class = 'testfailed'>FAILED</span>");
			}
			writer.write("</td></tr></tr></table><hr class='divline'><br><center><span>CISCO CONFIDENTIAL - Copyright &copy; www.cisco.com </span></center></body></html>");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception :",e);
		}finally{
			value=0;
			failedcount=0;
		}
	}
	
	public static String getCurrentTime(long tcStartTime){
		String todayTime="";
		SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("dd-MM-yyyy");
        String todayDate = DATE_FORMAT1.format(new Date());
        Date d1=new Date(tcStartTime);
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh-mm-ss");
        String time = DATE_FORMAT.format(d1);
        todayTime=todayDate+"-"+time;
        
        return todayTime;
	}

}
