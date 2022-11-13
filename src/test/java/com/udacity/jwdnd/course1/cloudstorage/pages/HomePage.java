package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    @FindBy(id = "welcomeText")
    private WebElement welcomeText;
    
    @FindBy(id = "logOutButton")
    private WebElement logOutButton;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public String getWelcomeText() {
        return welcomeText.getText();
    }

    public void performLogOut() {
        logOutButton.click();
    }
}
