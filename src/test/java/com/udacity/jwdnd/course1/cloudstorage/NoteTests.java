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

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.NotePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignUpPage;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NoteTests {
    @LocalServerPort
    private int port;

    private String baseUrl;
    private WebDriver driver;
    private LoginPage loginPage;
    private SignUpPage signUpPage;
    private NotePage notePage;
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
        this.notePage = new NotePage(driver);
        this.webDriverWait = new WebDriverWait(driver, 4);
        this.baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        this.notePage.performLogOut();
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

    private void createNote(String noteTitle, String noteDescription) {
        // Access notes page
        driver.get(baseUrl + "/notes");
        webDriverWait.until(ExpectedConditions.titleContains("Notes"));

        this.notePage.createNote(noteTitle, noteDescription);
    }

    @Test
    public void testCreateNote() {
        doMockSignUp("Note", "Test", "notetest", "notetest");
        doLogIn("notetest", "notetest");

        // Create new note
        String noteTitle = "This is a test title";
        String noteDescription = "A very long description for the note";
        this.createNote(noteTitle, noteDescription);

        // Assert that the note exists
        List<Note> notes = this.notePage.getNotes();
        Assertions.assertEquals(notes.size(), 1);
        
        // Confirm contents of the note just created
        Note note = notes.get(0);
        Assertions.assertNotNull(note.getNoteid());
        Assertions.assertEquals(note.getNotetitle(), noteTitle);
        Assertions.assertEquals(note.getNotedescription(), noteDescription);
    }

    @Test
    public void testEditNote() {
        doMockSignUp("Edit", "Test", "editnote", "editnote");
        doLogIn("editnote", "editnote");

        // Create note
        String noteTitle = "This is a test title";
        String noteDescription = "A very long description for the note";
        this.createNote(noteTitle, noteDescription);

        // New note parameters
        String newNoteTitle = "New test title";
        String newNoteDescription = "A very long updated description for the note";

        // Edit the just created note
        List<Note> notes = this.notePage.getNotes();
        Note note = notes.get(0);
        this.notePage.editNote(note.getNoteid(), newNoteTitle, newNoteDescription);

        // Confirm contents of the note has changed
        notes = this.notePage.getNotes();
        note = notes.get(0);
        Assertions.assertNotNull(note.getNoteid());
        Assertions.assertEquals(note.getNotetitle(), newNoteTitle);
        Assertions.assertEquals(note.getNotedescription(), newNoteDescription);
    }

    @Test
    public void testDeleteNote() {
        doMockSignUp("Delete", "Test", "deletenote", "deletenote");
        doLogIn("deletenote", "deletenote");

        // Create note
        String noteTitle = "This is a test title";
        String noteDescription = "A very long description for the note";
        this.createNote(noteTitle, noteDescription);

        // Edit the just created note
        List<Note> notes = this.notePage.getNotes();
        Note note = notes.get(0);
        this.notePage.deleteNote(note.getNoteid());

        // Confirm contents of the note has changed
        notes = this.notePage.getNotes();
        Assertions.assertEquals(notes.size(), 0);
    }
}
