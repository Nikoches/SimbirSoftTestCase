package ru.kocheshkov.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.nio.charset.StandardCharsets;

public class MailboxPage {
    private final WebDriver driver;
    @FindBy(xpath = "//div[contains(@placeholder,\"Напишите что-нибудь\")]")
    WebElement messageBody;
    @FindBy(xpath = "//div[contains(@class,\"MultipleAddressesDesktop-Field ComposeYabblesField\")]/child::div")
    WebElement emailAddressField;
    @FindBy(xpath = "//span[text()=\"Отправить\"]/../../..")
    WebElement sendButton;
    @FindBy(xpath = "//span [contains(text(),\"Все результаты\")]/span")
    private WebElement mailCounter;
    @FindBy(xpath = "//input[contains(@placeholder,\"Поиск\")] ")
    private WebElement searchInput;
    @FindBy(xpath = "//a[contains(@title,\"Написать\")]")
    private WebElement writeMessageButton;
    @FindBy(xpath = "//input[contains(@class,\"composeTextField ComposeSubject-TextField\")]")
    private WebElement subjectField;

    public MailboxPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public String getMailCounter(String searchQuery) {
        searchInput.sendKeys("folder:входящие subject:Simbirsoft Testcase.Kocheshkov");
        searchInput.sendKeys(Keys.RETURN);
        return mailCounter.getText();
    }

    public void clickWriteMessageButton() {
        writeMessageButton.click();
    }

    public void setEmailParams(String... emailParams) {
        emailAddressField.sendKeys(emailParams[0]);
        messageBody.clear();
        messageBody.sendKeys(emailParams[1]);
        subjectField.sendKeys(emailParams[2]);

    }

    public void clickSendButton() {
        sendButton.click();
    }
}
