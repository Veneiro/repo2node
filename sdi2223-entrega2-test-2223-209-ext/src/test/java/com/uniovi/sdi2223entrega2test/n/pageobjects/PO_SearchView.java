package com.uniovi.sdi2223entrega2test.n.pageobjects;

import com.uniovi.sdi2223entrega2test.n.util.SeleniumUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_SearchView extends PO_NavView {
    public static void searchForSale(WebDriver driver, String title, String xpath) {
        WebElement inputSearch = driver.findElement(By.name("search"));
        inputSearch.click();
        inputSearch.clear();
        inputSearch.sendKeys(title);
        List<WebElement> btnSearch = SeleniumUtils.waitLoadElementsByXpath(driver,
                xpath,  getTimeout());
        btnSearch.get(0).click();
    }
}
