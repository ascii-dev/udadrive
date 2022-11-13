package com.udacity.jwdnd.course1.cloudstorage;

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

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignUpPage;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpFlowTests {
    @LocalServerPort
    private int port;
    
    private String baseUrl;
    private WebDriver driver;
    private LoginPage loginPage;
    private SignUpPage signUpPage;
    private HomePage homePage;
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
        this.homePage = new HomePage(driver);
        this.webDriverWait = new WebDriverWait(driver, 4);
        this.baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getHomePageNotLoggedIn() {
        driver.get(baseUrl);
        Assertions.assertEquals(
            baseUrl + "/login",
            driver.getCurrentUrl()
        );
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

    @Test
    public void testSignUpAndLogin() {
        doMockSignUp("Home", "Page", "homepage", "homepage");
        doLogIn("homepage", "homepage");

        // Access home page
        driver.get(baseUrl);
        Assertions.assertTrue(driver.getPageSource().contains("Welcome Home Page"));

        // Logout
        homePage.performLogOut();
        Assertions.assertEquals(baseUrl + "/login", driver.getCurrentUrl());

        // Access home page again
        driver.get(baseUrl);
        Assertions.assertEquals(baseUrl + "/login", driver.getCurrentUrl());
    }
}
