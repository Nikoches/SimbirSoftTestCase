package ru.nikoches.wp;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ResourceBundle;

public class TestContactsPage {
    private final ResourceBundle properties = ResourceBundle.getBundle("testWpNikoches");
    private Process seleniumServer;
    private WebDriver webDriver = null;
    @BeforeClass
    public void setUp() throws IOException {
        seleniumServer = Runtime.getRuntime().exec(new String[]{"java", "-jar", properties.getString("server.jar"), properties.getString("server.type")});
        //String seleniumLog = new String(seleniumServer.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        ChromeOptions chromeOptions = new ChromeOptions();
        try {
            webDriver = new RemoteWebDriver(new URL(properties.getString("server.url")), chromeOptions);
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(12));
           // System.out.println(seleniumLog);
        } catch (MalformedURLException e) {
            webDriver.quit();
            killServer();
            e.printStackTrace();
        }
    }

    @Test
    public void textCheck() {


    }


    @AfterClass
    public void killServer() {
        webDriver.quit();
        if (seleniumServer.isAlive()) {
            seleniumServer.destroy();
        }
    }
}
