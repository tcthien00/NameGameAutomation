package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;



public class WebTestJ {

    public WebDriver driver;

    @Before
    public void setup() {
        // Change the prop if you are on Windows or Linux to the corresponding file
        System.setProperty("webdriver.chrome.driver", "chromedriver.mac");
        Capabilities capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        driver.navigate().to("http://www.ericrochester.com/name-game/");
    }

    @Test
    public void test_validate_title() {
        new HomePageJ(driver)
                .validateTitle();
    }

    @Test
    public void test_click_photo(){
        new HomePageJ(driver)
                .validateClickOfPhoto();
    }

    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

}
