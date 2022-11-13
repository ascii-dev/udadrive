package com.udacity.jwdnd.course1.cloudstorage;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.pages.CredentialPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignUpPage;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CredentialTests {
    @LocalServerPort
    private int port;

    private String baseUrl;
    private WebDriver driver;
    private LoginPage loginPage;
    private SignUpPage signUpPage;
    private CredentialPage credentialPage;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.loginPage = new LoginPage(driver);
        this.signUpPage = new SignUpPage(driver);
        this.credentialPage = new CredentialPage(driver);
        this.webDriverWait = new WebDriverWait(driver, 4);
        this.baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        this.credentialPage.performLogOut();
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void doLogIn(String username, String password) {
		// Log in to our dummy account.
		driver.get(baseUrl + "/login");
		loginPage.performLogin(username, password);
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

    private void doMockSignUp(String firstName, String lastName, String username, String password){
		driver.get(baseUrl + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
        signUpPage.performSignup(
            firstName,
            lastName,
            username,
            password
        );

        Assertions.assertEquals(
            baseUrl + "/login",
            driver.getCurrentUrl()
        );
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

    private void createCredential(String url, String username, String password) {
        // Access credentials page
        driver.get(baseUrl + "/credentials");
        webDriverWait.until(ExpectedConditions.titleContains("Credentials"));

        this.credentialPage.createCredential(url, username, password);
    }

    @Test
    public void testCreateCredential() {
        doMockSignUp("Credential", "Test", "credential", "credential");
        doLogIn("credential", "credential");

        // Create new credential
        String url = "http://localhost:8080";
        String username = "udacloud";
        String password = "udacloud";
        this.createCredential(url, username, password);

        // Assert that the credential exists
        List<Credential> credentials = this.credentialPage.getCredentials();
        Assertions.assertEquals(credentials.size(), 1);
        
        // Confirm contents of the credential just created
        Credential credential = credentials.get(0);
        Assertions.assertNotNull(credential.getCredentialid());
        Assertions.assertEquals(credential.getUsername(), username);
        Assertions.assertEquals(credential.getUrl(), url);
        Assertions.assertNotEquals(credential.getPassword(), password);
    }

    @Test
    public void testEditCredential() {
        doMockSignUp("Edit", "Test", "editcredential", "editcredential");
        doLogIn("editcredential", "editcredential");

        // Create new credential
        String url = "http://localhost:8080";
        String username = "udacloud";
        String password = "udacloud";
        this.createCredential(url, username, password);

        // New credential parameters
        String newPassword = "A£1davr13(";

        // Edit the just created credential
        List<Credential> credentials = this.credentialPage.getCredentials();
        Credential credential = credentials.get(0);
        this.credentialPage.editCredential(
            credential.getCredentialid(), 
            credential.getUrl(), 
            credential.getUsername(),
            newPassword
        );

        // Confirm contents of the credential has changed
        credentials = this.credentialPage.getCredentials();
        credential = credentials.get(0);
        Assertions.assertNotNull(credential.getCredentialid());
        Assertions.assertNotEquals(credential.getPassword(), newPassword);
    }

    @Test
    public void testDeleteCredential() {
        doMockSignUp("Delete", "Test", "deletecredential", "deletecredential");
        doLogIn("deletecredential", "deletecredential");

        // Create new credential
        String url = "http://localhost:8080";
        String username = "udacloud";
        String password = "udacloud";
        this.createCredential(url, username, password);

        // Delete the just created credential
        List<Credential> credentials = this.credentialPage.getCredentials();
        Credential credential = credentials.get(0);
        this.credentialPage.deleteCredential(credential.getCredentialid());

        // Confirm contents of the credential has changed
        credentials = this.credentialPage.getCredentials();
        Assertions.assertEquals(credentials.size(), 0);
    }
}
