package com.udacity.jwdnd.course1.cloudstorage.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;

public class CredentialPage {
    private final WebDriverWait webDriverWait;

    @FindBy(id = "createNewCredentialModal")
    private WebElement createNewCredentialModal;

    @FindBy(id = "credential-url")
    private WebElement url;

    @FindBy(id = "credential-username")
    private WebElement username;

    @FindBy(id = "credential-password")
    private WebElement password;

    @FindBy(id = "credential--id")
    private WebElement credentialId;

    @FindBy(id = "submitCredential")
    private WebElement submitCredential;

    @FindBy(className = "credentials")
    private List<WebElement> credentials;

    @FindBy(id = "logOutButton")
    private WebElement logOutButton;

    public CredentialPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = new WebDriverWait(driver, 20);
    }

    public void createCredential(
        String url,
        String username,
        String password
    ) {
        this.createNewCredentialModal.click();

        webDriverWait.until(
            ExpectedConditions.elementToBeClickable(this.url)
        ).sendKeys(url);
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        this.submitCredential.click();
    }

    public List<Credential> getCredentials() {
        List<Credential> output = new ArrayList<>();

        for (WebElement credential : this.credentials) {
            Credential newCredential = new Credential();
            newCredential.setCredentialid(
                Integer.parseInt(
                    credential.findElement(
                        By.className("credentialId")
                    ).getAttribute("value")
                )
            );
            newCredential.setUrl(credential.findElement(By.className("url")).getText());
            newCredential.setUsername(credential.findElement(By.className("username")).getText());
            newCredential.setPassword(credential.findElement(By.className("password")).getText());

            output.add(newCredential);
        }
        
        return output;
    }

    public void editCredential(
        int credentialId,
        String url,
        String username,
        String password
    ) {
        for (WebElement credential : this.credentials) {
            int id = Integer.parseInt(
                credential.findElement(
                    By.className("credentialId")
                ).getAttribute("value")
            );
            if (id == credentialId) {
                WebElement editModalButton = credential.findElement(By.className("editCredential"));
                editModalButton.click();

                webDriverWait.until(ExpectedConditions.elementToBeClickable(this.url)).clear();
                this.url.sendKeys(url);
                this.username.clear();
                this.username.sendKeys(username);
                this.password.clear();
                this.password.sendKeys(password);
                this.submitCredential.click();
            }
        }
    }

    public void deleteCredential(int credentialId) {
        for (WebElement credential : this.credentials) {
            int id = Integer.parseInt(
                credential.findElement(
                    By.className("credentialId")
                ).getAttribute("value")
            );
            if (id == credentialId) {
                WebElement deleteCredentialButton = credential.findElement(
                    By.className("submitDeleteForm")
                );
                deleteCredentialButton.click();
            }
        }
    }

    public void performLogOut() {
        logOutButton.click();
    }
}
