package com.adsy.methods;

import static org.testng.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SubjectTerm;
import com.sun.mail.util.MailSSLSocketFactory;

public class CheckEmail {

	public static void checkEmail(String expectedSubject, String expectedText)
			throws Exception {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		props.put("mail.imap.ssl.trust", "*");
		props.put("mail.imap.ssl.socketFactory", sf);

		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", "lenjatest@gmail.com", "lenjatest33");

		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);

		System.out.println("Total Message:" + folder.getMessageCount());
		System.out.println("Unread Message:" + folder.getUnreadMessageCount());

		Message[] messages = null;
		boolean isMailFound = false;
		Message expectedEmail = null;

		for (int i = 0; i < 5; i++) {
			messages = folder.search(new SubjectTerm(expectedSubject),
					folder.getMessages());

			if (messages.length == 0) {
				Thread.sleep(10000);
			}
		}

		for (Message mail : messages) {
			if (!mail.isSet(Flags.Flag.SEEN)) {
				expectedEmail = mail;
				System.out.println("Message Count is: "
						+ expectedEmail.getMessageNumber());
				isMailFound = true;
				folder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
			}
		}

		if (!isMailFound) {
			throw new Exception("Can't find email!");

		} else {
			String line;
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					expectedEmail.getInputStream()));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			System.out.println(buffer);
			String actualText = buffer.toString();
			assertEquals(actualText, expectedText);
			System.out.println("actualText:   " + actualText);
			System.out.println("expectedText:   " + expectedText);

		}
	}
}