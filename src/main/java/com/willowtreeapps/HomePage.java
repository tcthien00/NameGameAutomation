package com.willowtreeapps;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {

    private final String TRIES_COUNTER = "attempts";
    private final String CORRECT_COUNTER = "correct";
    private final String STREAK_COUNTER = "streak";
    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        super(driver);
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 6);
    }

    private void waitUntilClickable(By by){
        //explicit wait until the web element is clickable
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private void waitUntilVisible(By by){
        //explicit wait until the web element is visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private void waitUntilStaleness(WebElement we){
        //explicit wait until the web element is detached from DOM
        wait.until(ExpectedConditions.stalenessOf(we));
    }

    public void validateTitleIsPresent() {
//        System.out.println("Title="+driver.findElement(By.className("text-muted")).getText());
        Assert.assertTrue(driver.findElement(By.className("text-muted")).getText().equals("name game"));
    }

    private int readCounter(String counterName){
        //explicit wait until the web element is visible
        waitUntilVisible(By.className(counterName));
        WebElement we = driver.findElement(By.className(counterName));
        return Integer.parseInt(we.getText());
    }


    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        int count = readCounter(TRIES_COUNTER);

        driver.findElement(By.className("photo")).click();

        int countAfter = readCounter(TRIES_COUNTER);

        Assert.assertTrue(countAfter == count+1);

    }

    /*
     * To validate that correctly select a image will increase the Streak counter.
     * In this test, the "tries" counter should be increased (+1), the "corrects" counter should be increased (+1).
     * And the "streaks" counter should be increased (+1)
     */
    public void validateCorrectSelectionIncreasesStreakCounter() {
        int tries = readCounter(TRIES_COUNTER);
        int corrects = readCounter(CORRECT_COUNTER);
        int streaks = readCounter(STREAK_COUNTER);

        //to obtain a random multi-streak counter (streaks > 1)
        int min = 1, max = 10;
        int random = (int)(Math.random() * (max - min) + min);
//        System.out.println("To get streaks = " + random);
        for (int i=0; i<random; i++){
            imageSelection(true);
        }
        //validate multi-streak
        Assert.assertTrue("FAILED: Tries=" + readCounter(TRIES_COUNTER) + ", Expect=" + (tries+random),readCounter(TRIES_COUNTER) == tries+random);
        Assert.assertTrue(readCounter(CORRECT_COUNTER) == corrects+random);
        Assert.assertTrue(readCounter(STREAK_COUNTER) == streaks+random);

    }

    //to perform a correct or incorrect selection on an image in the list
    private String imageSelection(boolean correctSelection){
        waitUntilVisible(By.id("name"));//wait until the name to select appears

        String nameToSelect = driver.findElement(By.id("name")).getText();
//        System.out.println("\n" + nameToSelect + " -> " + correctSelection);

        String clickedName = null;
        waitUntilVisible(By.id("gallery"));
        waitUntilClickable(By.id("gallery"));
//        waitUntilClickable(By.className("photo"));
        //list of photos that are not wrongly selected
        List<WebElement> imageList = driver.findElements(By.className("photo"));
//        System.out.println("----------");
//        for (WebElement image : imageList) {
//            System.out.println(image.findElement(By.className("name")).getText() + "\t\t" + image.getAttribute("class"));
//        }
//        System.out.println("----------");
        for (WebElement image : imageList){
            if (correctSelection && image.findElement(By.className("name")).getText().equals(nameToSelect)) {
                image.click();
                clickedName = nameToSelect;
                waitUntilStaleness(image);
                break;
            }else if (!correctSelection){
                if (!image.findElement(By.className("name")).getText().equals(nameToSelect) && !image.getAttribute("class").equals("photo wrong")) {
                    image.click();
                    clickedName = image.findElement(By.className("name")).getText();
                    break;
                }
            }
        }
//        System.out.println("\tClicked " + clickedImage.findElement(By.className("name")).getText());
        return clickedName;
    }

    /*
    * To validate that incorrectly select an image will reset the multi-Streak counter.
    * Multi-streak counter is a streak counter that has value > 1, which is a random number
    * The "streaks" counter should be reset to 0 after a wrong selection
     */
    public void validateIncorrectSelectionResetsMultiStreakCounter() {

        int tries = readCounter(TRIES_COUNTER);
        int corrects = readCounter(CORRECT_COUNTER);
        int streaks = readCounter(STREAK_COUNTER);

        //to obtain a random multi-streak counter (streaks > 1)
        int min = 1;
        int max = 10;
        int random = (int)(Math.random() * (max - min) + min);
        //System.out.println("To get streaks = " + random);
        for (int i=0; i<random; i++){
            imageSelection(true);
        }
        //validate multi-streak
        Assert.assertTrue(readCounter(TRIES_COUNTER) == tries+random);
        Assert.assertTrue(readCounter(CORRECT_COUNTER) == corrects+random);
        Assert.assertTrue(readCounter(STREAK_COUNTER) == streaks+random);

        //wrong selection to reset the streak counter
        imageSelection(false);
        Assert.assertTrue(readCounter(TRIES_COUNTER) == tries+random+1);
        Assert.assertTrue(readCounter(CORRECT_COUNTER) == corrects+random);
        Assert.assertTrue(readCounter(STREAK_COUNTER) == 0);

   }

    public void validateTenRandomSelectionsIncreasesTriesCorrectsCounters() {
        int tries = readCounter(TRIES_COUNTER);
        int corrects = readCounter(CORRECT_COUNTER);
        //System.out.println("tries0="+tries);
        //System.out.println("corrects0="+corrects);

        //to obtain a random multi-streak counter (streaks > 1)
        Random r = new Random();

        for (int i=0; i<10; i++){
            boolean randomSelection = r.nextBoolean();
            corrects += randomSelection? 1:0;
            tries++;
            imageSelection(randomSelection);
        }
        sleep(1000);
        //validate multi-streak
        Assert.assertTrue(readCounter(TRIES_COUNTER) + "=" + tries, readCounter(TRIES_COUNTER) == tries);
        Assert.assertTrue(readCounter(CORRECT_COUNTER) + "=" + corrects, readCounter(CORRECT_COUNTER) == corrects);
    }

    //to verify that after correct selection, the name and photos changes
    public void verifyNamesPhotosChangedAfterCorrectSelection() {

        //collect old names
        int corrects = readCounter(CORRECT_COUNTER);

        waitUntilClickable(By.id("gallery"));
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
        Assert.assertTrue(readCounter(CORRECT_COUNTER) == corrects+1);

        waitUntilClickable(By.id("gallery"));
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
        String failSelection = imageSelection(false);
        System.out.println("failSelection=" + failSelection);

        //make correct selection
        String correctSelection = imageSelection(true);
        System.out.println("correctSelection=" + correctSelection);

        int failCount = 0, correctCount = 0;
        for (int i=0; i<100; i++){
            System.out.print(i + " ");
            waitUntilClickable(By.id("gallery"));
            List<WebElement> newImageList = driver.findElements(By.className("photo"));
            for (WebElement newImage : newImageList) {
//                System.out.println("newName=" + newImage.findElement(By.className("name")).getText());
                failCount += newImage.findElement(By.className("name")).getText().equals(failSelection)? 1:0;
                correctCount += newImage.findElement(By.className("name")).getText().equals(correctSelection)? 1:0;
            }
            newImageList.clear();
            driver.navigate().refresh();
        }
        System.out.println();
        System.out.println(failSelection + " appeared " + failCount);
        System.out.println(correctSelection + " appeared " + correctCount);

        Assert.assertTrue("failCount("+failCount+ ") >? correctCount("+ correctCount+")",failCount > correctCount);
    }
}
