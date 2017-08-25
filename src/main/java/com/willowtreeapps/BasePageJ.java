package com.willowtreeapps;

import org.assertj.swing.assertions.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 5/23/17.
 */
public class BasePageJ {

    public WebDriver driver;

    public BasePageJ(WebDriver driver) {
        this.driver = driver;
    }

    public BasePageJ validateAttribute(String css, String attr, String regex) {
        return validateAttribute(By.cssSelector(css), attr, regex);
    }

    public BasePageJ validateAttribute(By by, String attr, String regex) {
        return validateAttribute(driver.findElement(by), attr, regex);
    }

    public BasePageJ validateAttribute(WebElement element, String attr, String regex) {
        String actual = null;
        try {
            actual = element.getAttribute(attr);
            if (actual.equals(regex)) {
                return this; // test passes
            }
        } catch (Exception e) {
            Assertions.fail(String.format("Attribute not fount! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s] [Message: %s]",
                    attr,
                    regex,
                    actual,
                    element.toString(),
                    e.getMessage()), e);
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(actual);

        Assertions.assertThat(m.find())
                .withFailMessage("Attribute doesn't match! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s]",
                        attr,
                        regex,
                        actual,
                        element.toString())
                .isTrue();
        return this;
    }

    public BasePageJ validateText(String css, String text) {
        return validateText(By.cssSelector(css), text);
    }

    /**
     * Validate Text ignores white spaces
     */
    public BasePageJ validateText(By by, String text) {
        Assertions.assertThat(text).isEqualToIgnoringWhitespace(getText(by));
        return this;
    }

    public String getText(By by) {
        WebElement e = driver.findElement(by);
        return e.getTagName().equalsIgnoreCase("input")
                || e.getTagName().equalsIgnoreCase("select")
                || e.getTagName().equalsIgnoreCase("textarea")
                ? e.getAttribute("value")
                : e.getText();
    }

    public BasePageJ validatePresent(String css) {
        return validatePresent(By.cssSelector(css));
    }

    public BasePageJ validatePresent(By by) {
        Assertions.assertThat(driver.findElements(by).size())
                .withFailMessage("Element not present: [Element: %s]", by.toString())
                .isGreaterThan(0);
        return this;
    }

}
