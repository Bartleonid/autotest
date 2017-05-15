package com.adsy.pages;

import org.openqa.selenium.WebDriver;

import com.adsy.methods.WebDriverUtility;

public class ThankYouPage extends WebDriverUtility {

	WebDriver driver;
	public String expectedTitle = "Thank You!";

	public ThankYouPage(WebDriver driver) {
		this.driver = driver;
	}

}
