package ru.kocheshkov.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import ru.kocheshkov.pages.LoginPage;
import ru.kocheshkov.pages.MailboxPage;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;


public class FirstTest {
    private final Properties properties = new Properties();
    private Process seleniumServer;
    private WebDriver webDriver = null;

    @BeforeClass
    public void setUp() throws IOException {
        //load and get properties.
        properties.load(ClassLoader.getSystemResourceAsStream("test.properties"));
        //start selenium Server Grid.
        seleniumServer = Runtime.getRuntime().exec(new String[]{"java", "-jar", properties.getProperty("server.jar"), properties.getProperty("server.type")});
        java.io.InputStream is = seleniumServer.getInputStream();
        byte[] b = new byte[is.available()];
        is.read(b, 0, b.length);
        System.out.println(new String(b) + Arrays.toString(b));
        //Creating Webdriver with firefoxOptions.
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        try {
            webDriver = new RemoteWebDriver(new URL(properties.getProperty("server.url")), firefoxOptions);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(12));
        } catch (MalformedURLException e) {
            webDriver.quit();
            killServer();
            e.printStackTrace();
        }
    }

    @Test
    public void simpleTest() throws UnsupportedEncodingException {
        String messageBody;
        webDriver.get((properties.getProperty("target.url")));
        //Creating loggingPage and authorization
        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.clickLoginButton();
        loginPage.setLoginInputAndClick(System.getProperty("username"));
        loginPage.setPasswordInput(System.getProperty("password"));
        loginPage.clickMailButton();
        //Getting all tabs and closes first
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(0)).close();
        webDriver.switchTo().window(tabs.get(1));
        /*
            Encoding input string from ISO_8859_1 to UTF-8
         */
        String searchQ=new String(properties.getProperty("search.query").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        String messageSubject=new String(properties.getProperty("message.subject").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        /*
            Found messages by input string subject and
            get message counter
         */
        MailboxPage mailboxPage = new MailboxPage(webDriver);
        messageBody = mailboxPage.getMailCounter(searchQ);
        mailboxPage.clickWriteMessageButton();
        /*
            Send email params, then send email message.
            Set email params:
            [1] - email address
            [2] - message body
            [3] - subject
        */
        mailboxPage.setEmailParams(properties.getProperty("email.address"),
                messageBody,
                messageSubject);
        mailboxPage.clickSendButton();
        System.out.println(webDriver.getCurrentUrl());
    }

    @AfterClass
    public void killServer() {
        webDriver.quit();
        if (seleniumServer.isAlive()) {
            seleniumServer.destroy();
        }
    }
}
