package com.odde.atddv2.page;

import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

// Page Object
// 在 Home page 的登入操作集合都要重構到 Home page 類別中
public class HomePage {
    private final Browser browser = new Browser();

    public HomePage() {
    }

    public void open() {
        browser.open();
    }

    public void login(String userName, String password) {
        browser.inputByPlaceholder("用户名", userName);
        browser.inputByPlaceholder("密码", password);
        browser.clickByText("登录");
    }

    public void shouldHaveText(String text) {
        browser.shouldHaveText2(text);
    }

    @SneakyThrows
    public WebDriver createWebDriver() {
        return browser.createWebDriver();
    }

    public WebDriver getWebDriver() {
        return browser.getWebDriver();
    }

    public void quitWebDriver() {
        // 確保環境獨立乾淨
        browser.quitWebDriver();
    }

    private void clickByText(String text) {
        browser.clickByText(text);
    }

    private void inputByPlaceholder(String placeholder, String text) {
        browser.inputByPlaceholder(placeholder, text);
    }

    private void shouldHaveText2(String text) {
        browser.shouldHaveText2(text);
    }


}