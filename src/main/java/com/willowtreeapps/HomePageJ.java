package com.willowtreeapps;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created on 5/23/17.
 */
public class HomePageJ extends BasePageJ {


    public HomePageJ(WebDriver driver) {
        super(driver);
    }

    public void validateTitle() {
        Assert.assertTrue(By.cssSelector("h1") != null);
    }


    public void validateClickOfPhoto() {
        //Wait for page to load
        sleep(6000);

        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        driver.findElement(By.className("photo")).click();

        sleep(6000);

        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        Assert.assertTrue(countAfter > count);

    }

    private void sleep(int timetosleep) {
        try {
            Thread.sleep(timetosleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
