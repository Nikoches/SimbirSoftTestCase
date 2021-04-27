package ru.kocheshkov;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

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
    public void simpleTest() throws MalformedURLException {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        WebDriver driver = new RemoteWebDriver(new URL(properties.getProperty("server_url")), firefoxOptions);
        driver.get((properties.getProperty("target_url")));
        WebElement webElement = driver.findElement(By.xpath("//*[contains(text(), 'Войти')]"));
        webElement.click();
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
