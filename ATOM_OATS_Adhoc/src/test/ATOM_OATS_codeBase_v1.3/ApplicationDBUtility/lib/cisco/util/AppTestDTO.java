package lib.cisco.util;

import java.io.Serializable;

public class AppTestDTO implements Serializable{
	  public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getTestScriptId() {
		return testScriptId;
	}
	public void setTestScriptId(String testScriptId) {
		this.testScriptId = testScriptId;
	}
	public String getBrowserType() {
		return browserType;
	}
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	public String getTestClassName() {
		return testClassName;
	}
	public void setTestClassName(String testClassName) {
		this.testClassName = testClassName;
	}
	public String getTestCaseName() {
		return testCaseName;
	}
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}
	public String getTestMethodName() {
		return testMethodName;
	}
	public void setTestMethodName(String testMethodName) {
		this.testMethodName = testMethodName;
	}
	public String getInvocationMethod() {
		return invocationMethod;
	}
	public void setInvocationMethod(String invocationMethod) {
		this.invocationMethod = invocationMethod;
	}
	public String getBrowserVersion() {
		return browserVersion;
	}
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	public String getOsType() {
		return osType;
	}
	public void setOsType(String osType) {
		this.osType = osType;
	}
	public String getGridUrl() {
		return gridUrl;
	}
	public void setGridUrl(String gridUrl) {
		this.gridUrl = gridUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private static final long serialVersionUID = 1L;
	  /*-------------------Mandatory columns-------------------*/
      private String dataId = null;
      private String testId = null;
      private String testScriptId = null;
      
      private String browserType = null;
      private String testClassName=null;	
      private String testCaseName=null;
      private String testMethodName=null;
      private String invocationMethod=null;
      private String browserVersion=null;
      private String osType=null;
      private String gridUrl=null;
      /*---------------Add your Application specific input columns here-------------*/
      private String url=null;
      private String userid=null;
      private String password=null;
      private String errorMsg=null;
      private String userFullName=null;
      private String search=null;

     

}
