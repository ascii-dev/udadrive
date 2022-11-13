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

import com.udacity.jwdnd.course1.cloudstorage.models.Note;

public class NotePage {
    private final WebDriverWait webDriverWait;

    @FindBy(id = "createNewNoteModal")
    private WebElement createNewNoteModal;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "note-id")
    private WebElement noteId;

    @FindBy(id = "submitNote")
    private WebElement submitNote;

    @FindBy(className = "notes")
    private List<WebElement> notes;

    @FindBy(id = "logOutButton")
    private WebElement logOutButton;

    public NotePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.webDriverWait = new WebDriverWait(driver, 20);
    }

    public void createNote(String noteTitle, String noteDescription) {
        this.createNewNoteModal.click();

        webDriverWait.until(
            ExpectedConditions.elementToBeClickable(this.noteTitle)
        ).sendKeys(noteTitle);
        this.noteDescription.sendKeys(noteDescription);
        this.submitNote.click();
    }

    public List<Note> getNotes() {
        List<Note> output = new ArrayList<>();

        for (WebElement note : this.notes) {
            Note newNote = new Note();
            newNote.setNoteid(
                Integer.parseInt(
                    note.findElement(
                        By.className("noteId")
                    ).getAttribute("value")
                )
            );
            newNote.setNotetitle(note.findElement(By.className("noteTitle")).getText());
            newNote.setNotedescription(note.findElement(By.className("noteDescription")).getText());

            output.add(newNote);
        }
        
        return output;
    }

    public void editNote(int noteId, String noteTitle, String noteDescription) {
        for (WebElement note : this.notes) {
            int id = Integer.parseInt(
                note.findElement(
                    By.className("noteId")
                ).getAttribute("value")
            );
            if (id == noteId) {
                WebElement editModalButton = note.findElement(By.className("editNote"));
                editModalButton.click();

                webDriverWait.until(ExpectedConditions.elementToBeClickable(this.noteTitle)).clear();
                this.noteTitle.sendKeys(noteTitle);
                this.noteDescription.clear();
                this.noteDescription.sendKeys(noteDescription);
                this.submitNote.click();
            }
        }
    }

    public void deleteNote(int noteId) {
        for (WebElement note : this.notes) {
            int id = Integer.parseInt(
                note.findElement(
                    By.className("noteId")
                ).getAttribute("value")
            );
            if (id == noteId) {
                WebElement deleteNoteButton = note.findElement(
                    By.className("submitDeleteForm")
                );
                deleteNoteButton.click();
            }
        }
    }

    public void performLogOut() {
        logOutButton.click();
    }
}
