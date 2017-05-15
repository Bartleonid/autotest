package com.adsy.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.adsy.items.User;
import com.adsy.methods.WebDriverUtility;

public class ContactPage extends WebDriverUtility {

	WebDriver driver;
	// Interface:
	By firstName = By.cssSelector("#contactform-name");
	By supportEmail = By.cssSelector("#contactform-email");
	By subject = By.cssSelector("#contactform-subject");
	By submitBtn = By.cssSelector(".btn.btn-primary");

	public String expectedTitleContact = "Contact";

	// Constructor - driver initialisation.
	public ContactPage(WebDriver driver) {
		this.driver = driver;
	}

	// Method Login to adsy.com:
	public ContactPage login(User user) throws InterruptedException {
		type(this.driver, firstName, user.firstname);
		type(this.driver, supportEmail, user.supportemail);
		type(this.driver, subject, user.subject);
		click(this.driver, submitBtn);
		return new ContactPage(driver);
	}

}