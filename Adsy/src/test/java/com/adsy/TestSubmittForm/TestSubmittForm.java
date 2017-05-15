package com.adsy.TestSubmittForm;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.adsy.items.User;
import com.adsy.methods.CheckEmail;
import com.adsy.methods.WebDriverUtility;
import com.adsy.pages.ContactPage;
import com.adsy.pages.ThankYouPage;

public class TestSubmittForm extends WebDriverUtility {

	User admin = new User("Leonid", "lenjatest@gmail.com", "Help me please!");

	@DataProvider
	public Object[][] getData() {
		return new Object[][] { { admin } };
	}

	@Test(dataProvider = "getData")
	public void verifyContactSupport(User user) throws InterruptedException {
		start();
		ContactPage contactPage = new ContactPage(driver);
		compareTitle(contactPage.expectedTitleContact);
		contactPage.login(user);
		System.out.println("Before login   " + driver.getTitle());
		ThankYouPage thankYouPage = new ThankYouPage(driver);
		compareTitle(thankYouPage.expectedTitle);
		// driver.wait();
		driver.quit();
	}

	@Test(dependsOnMethods = { "verifyContactSupport" })
	public void emailConfirmation() throws InterruptedException {

		try {
			CheckEmail.checkEmail("Help me please!",
					"test/automation-qa message");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}