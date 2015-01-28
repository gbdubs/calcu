package calculus.models;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;

public class Report {

	public String reportUuid;
	public long reportedOn;
	public String reportType;
	
	public String contentUuid;
	public String contentUrl;
	public String contentTitle;
	
	public String reporterUsername;
	public String reporterEmail;
	public String reporterUserId;
	
	public Report(String uuid, long reportTime, String reportType, Entity reporterPublicInfo, Content content){	
		this.reportUuid = uuid;
		this.reportedOn = reportTime;
		this.reportType = reportType;
		
		this.contentUuid = content.getUuid();
		this.contentUrl = content.getUrl();
		this.contentTitle = content.getTitle();
		
		this.reporterUsername = (String) reporterPublicInfo.getProperty("username");
		this.reporterUserId = (String) reporterPublicInfo.getProperty("userId");
		this.reporterEmail = (String) reporterPublicInfo.getProperty("email");
	}
	
	public String getReportUuid(){
		return this.reportUuid;
	}

	public String getReportedOn(){
		return this.getReadableReportDate();
	}
	
	public String getReportType(){
		return this.reportType;
	}

	public String getContentUuid(){
		return this.contentUuid;
	}
	
	public String getContentUrl(){
		return this.contentUrl;
	}

	public String getContentTitle(){
		return this.contentTitle;
	}

	public String getReporterUsername(){
		return this.reporterUsername;
	}
	
	public String getReporterEmail(){
		return this.reporterEmail;
	}
	
	public String getReporterUserId(){
		return this.reporterUserId;
	}
	
	public String getReadableReportDate(){
		Date d = new Date(this.reportedOn);
		SimpleDateFormat df = new SimpleDateFormat("MM/DD/YY HH:mm");
		return df.format(d);
	}
}
