package ru.nikoches.wp.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;

public class NewsPage {
    private final WebDriver driver;

    public NewsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
    @FindBy(xpath = "//div[@class=\"entry-content\"]/p")
    private WebElement newsText;

    public WebElement getNewsText() {
        return newsText;
    }
}
