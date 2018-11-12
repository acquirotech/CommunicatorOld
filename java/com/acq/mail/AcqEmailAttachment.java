package com.acq.mail;

import java.io.File;
import java.io.Serializable;

public class AcqEmailAttachment implements Serializable{
	
	private String attachmentName;
	private File attchmentFile;
	public String getAttachmentName() {
		return attachmentName;
	}
	
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public File getAttchmentFile() {
		return attchmentFile;
	}
	public void setAttchmentFile(File attchmentFile) {
		this.attchmentFile = attchmentFile;
	}
	
}

