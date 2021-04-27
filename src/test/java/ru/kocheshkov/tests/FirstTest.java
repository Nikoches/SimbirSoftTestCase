package ru.kocheshkov.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.*;
import ru.kocheshkov.pages.LoginPage;
import ru.kocheshkov.pages.MailboxPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class FirstTest {
    private final Properties properties = new Properties();
    private Process seleniumServer;

    @BeforeClass
    public void setUp() throws IOException {
        //load and get properties
        properties.load(ClassLoader.getSystemResourceAsStream("testapp.properties"));
        //start selenium Server Grid
        seleniumServer = Runtime.getRuntime().exec(new String[]{"java", "-jar", properties.getProperty("server_jar"), properties.getProperty("server_type")});
        java.io.InputStream is = seleniumServer.getInputStream();
        byte[] b = new byte[is.available()];
        is.read(b, 0, b.length);
        System.out.println(new String(b) + Arrays.toString(b));
    }

    @Test
    public void simpleTest() {
        String messageBody;
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(properties.getProperty("server_url")), firefoxOptions);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(12));
        } catch (MalformedURLException e) {
            driver.quit();
            killServer();
            e.printStackTrace();
        }
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));
        driver.get((properties.getProperty("target_url")));
        //Creating loggingPage and authorization
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLoginButton();
        loginPage.setLoginInputAndClick(properties.getProperty("email_username"));
        loginPage.setPasswordInput(properties.getProperty("email_pass"));
        loginPage.ClickMailButton();
        //Getting all tabs and closes first
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0)).close();
        driver.switchTo().window(tabs.get(1));
        /*
            Found messages by input string subject and
            get message counter
         */
        MailboxPage mailboxPage = new MailboxPage(driver);
        messageBody = mailboxPage.getMailCounter(properties.getProperty("message_subject"));
        mailboxPage.ClickWriteMessageButton();
        /*
            Send email params, then send email message.
            Set email params:
            [1] - email address
            [2] - message body
            [3] - subject
        */
        mailboxPage.setEmailParams(properties.getProperty("email_address"),
                messageBody,
                properties.getProperty("message_subject"));
        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }

    @AfterClass
    public void killServer() {
        if (seleniumServer.isAlive()) {
            seleniumServer.destroy();
        }
    }
}
