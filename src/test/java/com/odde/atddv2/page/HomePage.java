package com.odde.atddv2.page;

import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.openqa.selenium.By.xpath;

// Page Object
// 在 Home page 的登入操作集合都要重構到 Home page 類別中
public class HomePage {
    private WebDriver webDriver = null;

    public HomePage() {
    }

    public void open() {
        getWebDriver().get("http://host.docker.internal:10081");
    }

    public void login(String userName, String password) {
        inputByPlaceholder("用户名", userName);
        inputByPlaceholder("密码", password);
        clickByText("登录");
    }

    public void shouldHaveText(String text) {
        shouldHaveText2(text);
    }

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    public WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }

    public void quitWebDriver() {
        // 確保環境獨立乾淨
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    private void clickByText(String text) {
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[text()='" + text + "']")), Objects::nonNull).click();
    }

    private void inputByPlaceholder(String placeholder, String text) {
        await().ignoreExceptions().until(() -> getWebDriver()
                        // // 代表 root，* 代表任意元素，[] 代表元素的属性，@ 代表 element 屬性
                        // 用戶視角的元素定位相對變化小，因為這比較貼近需求，就不容易改動
                        .findElement(xpath("//*[@placeholder='" + placeholder + "']")), Objects::nonNull)
                .sendKeys(text);
    }

    private void shouldHaveText2(String text) {
        await().ignoreExceptions().untilAsserted(() -> assertThat(getWebDriver().findElements(xpath("//*[text()='" + text + "']"))).isNotEmpty());
    }


}