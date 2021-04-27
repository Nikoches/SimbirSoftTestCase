package ru.kocheshkov.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private final WebDriver driver;

    @FindBy(xpath = "//*[contains(text(), 'Войти')]")
    private WebElement loginButton;
    @FindBy(id = "passp-field-passwd")
    private WebElement passwordInput;
    @FindBy(xpath = "//div[@class=\"passp-button passp-sign-in-button\"]/button")
    private WebElement passwordInputButton;
    @FindBy(id = "passp-field-login")
    private WebElement loginInput;
    @FindBy(xpath = "//a[@class=\"home-link desk-notif-card__domik-mail-line home-link_black_yes\"]")
    private WebElement mailButton;
    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public void setLoginInputAndClick(String loginInput) {
        this.loginInput.sendKeys(loginInput);
        this.passwordInputButton.click();
    }

    public void setPasswordInput(String passwordInput) {
        this.passwordInput.sendKeys(passwordInput);
        this.passwordInputButton.click();
    }
    public void ClickMailButton(){
        this.mailButton.click();
    }
}
