package com.acq.mail;

import java.util.HashMap;

public interface AcqEmailServiceHandler {
	public void sendEmail(final HashMap<String,Object> model, final String templateLocation, String toEmail);

	public void sendVirtualEmail(HashMap<String, Object> pageData,
			String string, String custEmail, String fromEmail);
}
