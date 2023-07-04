package com.uniovi.sdi2223entrega2test.n.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PO_ChatView {

    static public void sendMessage(WebDriver driver, String message) {
        WebElement write = driver.findElement(By.id("message"));
        write.click();
        write.clear();
        write.sendKeys(message);
        //Pulsar el boton de enviar.
        By boton = By.id("send-button");
        driver.findElement(boton).click();
    }
}
