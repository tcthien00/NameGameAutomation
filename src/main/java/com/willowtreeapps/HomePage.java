package com.willowtreeapps;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;



import java.util.List;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {

    //delay default to 6000 ms using before each counter read.
    //best practice is to wait until all Photos are loaded:  Cannot do it in limited time.
    private final int DELAY = 6000;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void validateTitleIsPresent() {
        WebElement title = driver.findElement(By.cssSelector("h1"));
        Assert.assertTrue(title != null);
    }


    private int readCounter(String counterName){
        return readCounter(counterName, DELAY);
    }
    private int readCounter(String counterName, int delay){
        if (delay > 0)
            sleep(delay);
        return Integer.parseInt(driver.findElement(By.className(counterName)).getText());
    }


    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        int count = readCounter("attempts");

        driver.findElement(By.className("photo")).click();

        int countAfter = readCounter("attempts");

        Assert.assertTrue(countAfter == count+1);

    }

    /*
     * To validate that correctly select a image will increase the Streak counter.
     * In this test, the "tries" counter should be increased (+1), the "corrects" counter should be increased (+1).
     * And the "streaks" counter should be increased (+1)
     */
    public void validateCorrectSelectionIncreasesStreakCounter() {
        int tries = readCounter("attempts");
        int corrects = readCounter("correct", 0);
        int streaks = readCounter("streak", 0);

        //to obtain a random multi-streak counter (streaks > 1)
        int min = 1;
        int max = 10;
        int random = (int)(Math.random() * (max - min) + min);
        //System.out.println("To get streaks = " + random);
        for (int i=0; i<random; i++){
            sleep(6000);
            imageSelection(true);
        }
        //validate multi-streak
        Assert.assertTrue(readCounter("attempts") == tries+random);
        Assert.assertTrue(readCounter("correct", 0) == corrects+random);
        Assert.assertTrue(readCounter("streak", 0) == streaks+random);

    }

    //to perform a correct or incorrect selection on an image in the list
    private WebElement imageSelection(boolean correctSelection){
        String nameToSelect = driver.findElement(By.id("name")).getText();
        //System.out.println("nameToSelect="+nameToSelect);

        WebElement imageClicked = null;
        List<WebElement> imageList = driver.findElements(By.className("photo"));
        for (WebElement image : imageList){
            if (correctSelection){
                if (image.getText().contains(nameToSelect)) {
                    image.click();
                    imageClicked = image;
                    break;
                }
            }else{
                if (!image.getText().contains(nameToSelect)) {
                    image.click();
                    imageClicked = image;
                    break;
                }
            }
        }
        return imageClicked;
    }

    /*
    * To validate that incorrectly select an image will reset the multi-Streak counter.
    * Multi-streak counter is a streak counter that has value > 1, which is a random number
    * The "streaks" counter should be reset to 0 after a wrong selection
     */
    public void validateIncorrectSelectionResetsMultiStreakCounter() {

        int tries = readCounter("attempts");
        int corrects = readCounter("correct", 0);
        int streaks = readCounter("streak", 0);

        //to obtain a random multi-streak counter (streaks > 1)
        int min = 1;
        int max = 10;
        int random = (int)(Math.random() * (max - min) + min);
        //System.out.println("To get streaks = " + random);
        for (int i=0; i<random; i++){
            imageSelection(true);
            sleep(DELAY);
        }
        //validate multi-streak
        Assert.assertTrue(readCounter("attempts") == tries+random);
        Assert.assertTrue(readCounter("correct", 0) == corrects+random);
        Assert.assertTrue(readCounter("streak", 0) == streaks+random);

        //wrong selection to reset the streak counter
        imageSelection(false);
        Assert.assertTrue(readCounter("attempts") == tries+random+1);
        Assert.assertTrue(readCounter("correct", 0) == corrects+random);
        Assert.assertTrue(readCounter("streak", 0) == 0);

   }

    public void validateTenRandomSelectionsIncreasesTriesCorrectsCounters() {
        int tries = readCounter("attempts");
        int corrects = readCounter("correct", 0);

        //to obtain a random multi-streak counter (streaks > 1)
        Random r = new Random();

        for (int i=0; i<10; i++){
            boolean randomSelection = r.nextBoolean();
            corrects += randomSelection? 1:0;
            tries++;
            sleep(DELAY);
            imageSelection(randomSelection);
        }
        //validate multi-streak
        Assert.assertTrue(readCounter("attempts") == tries);
        Assert.assertTrue(readCounter("correct", 0) == corrects);

    }

    //to verify that after correct selection, the name and photos changes
    public void verifyNamesPhotosChangedAfterCorrectSelection() {

        //collect old names
        int corrects = readCounter("correct");

        List<WebElement> oldImageList = driver.findElements(By.className("photo"));
        //use name list because oldImageList is not accessible at the end.  TODO: photo check
        List<String> oldNameList = new ArrayList();

        for (WebElement oldImage : oldImageList) {
            String oldName = oldImage.findElement(By.className("name")).getText();
            oldNameList.add(oldName);
            //System.out.println("\t\t\toldName=" + oldName);
        }
        //select the correct photo
        imageSelection(true);
        Assert.assertTrue(readCounter("correct") == corrects+1);

        //verify the new image list does not contain any image in the old list
        List<WebElement> newImageList = driver.findElements(By.className("photo"));
        for (WebElement newImage : newImageList) {
            String newName = newImage.findElement(By.className("name")).getText();
            //System.out.println("newName=" + newName);
            for (String oldName : oldNameList) {
                //System.out.println("\t\t\toldName=" + oldName);
                Assert.assertTrue(newName != oldName);
            }
        }
    }

    /*
    *For Bonus test case
    *To correctly and wrongly select a picture, and refresh the page 100 times (the higher the better, but higher execution time)
    * Then verify that failing to select one person’s name correctly makes
     * that person appear more frequently than other “correctly selected” people.
     */
    public void verifyFailSelectionAppearMoreFrequentlyThanCorrectSelections() {
        //make wrong selection
        sleep(DELAY);
        String failSelection = imageSelection(false).findElement(By.className("name")).getText();
        System.out.println("failSelection=" + failSelection);

        //make correct selection
        sleep(DELAY);
        String correctSelection = imageSelection(true).findElement(By.className("name")).getText();
        System.out.println("correctSelection=" + correctSelection);

        sleep(DELAY);
        int failCount = 0;
        int correctCount = 0;
        List<WebElement> newImageList;
        for (int i=0; i<100; i++){
            sleep(DELAY);
            newImageList = driver.findElements(By.className("photo"));
            for (WebElement newImage : newImageList) {
                System.out.println("newName=" + newImage.findElement(By.className("name")).getText());

                failCount += (newImage.findElement(By.className("name")).getText() == failSelection)? 1:0;
                correctCount += (newImage.findElement(By.className("name")).getText() == correctSelection)? 1:0;
            }
            newImageList.clear();
            driver.navigate().refresh();
        }

        Assert.assertTrue("failCount("+failCount+ ") >? correctCount("+ correctCount+")",failCount > correctCount);
    }
}
