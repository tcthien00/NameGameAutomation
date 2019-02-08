package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/*
*Test cases are designed to run independently.  This mean each test case can be executed in random order.
* This would help detecting more potential bugs that could exist.  The test suite should invoke those test cases randomly.
 */
public class WebTest {

    private WebDriver driver;

    /**
     * Change the prop if you are on Windows or Linux to the corresponding file type
     * The chrome WebDrivers are included on the root of this project, to get the
     * latest versions go to https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.mac");
        Capabilities capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        driver.navigate().to("http://www.ericrochester.com/name-game/");
    }

    @Test
    public void test_validate_title_is_present() {
        System.out.println(methodName());
        new HomePage(driver).validateTitleIsPresent();
    }

    @Test
    public void test_clicking_photo_increases_tries_counter() {
        System.out.println(methodName());
        new HomePage(driver).validateClickingFirstPhotoIncreasesTriesCounter();
    }

    @Test
    public void test_correct_selection_increases_streak_counter() {
        System.out.println(methodName());
        new HomePage(driver).validateCorrectSelectionIncreasesStreakCounter();
    }

    @Test
    public void validateIncorrectSelectionResetsMultiStreakCounter() {
        System.out.println(methodName());
        new HomePage(driver).validateIncorrectSelectionResetsMultiStreakCounter();
    }

    @Test
    public void validateTenRandomSelectionsIncreasesTriesCorrectsCounters() {
        System.out.println(methodName());
        new HomePage(driver).validateTenRandomSelectionsIncreasesTriesCorrectsCounters();
    }

    @Test
    public void verifyNamesPhotosChangedAfterCorrectSelection() {
        System.out.println(methodName());
        new HomePage(driver).verifyNamesPhotosChangedAfterCorrectSelection();
    }

//    @Ignore
    @Test
    public void verifyFailSelectionAppearMoreFrequentlyThanCorrectSelections() {
        System.out.println(methodName());
        new HomePage(driver).verifyFailSelectionAppearMoreFrequentlyThanCorrectSelections();
    }

    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
