package com.odde.atddv2.page;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;

// 封裝不同平台的瀏覽器的使用者視角操作
// 隔離依賴 web driver 第三方工具 ex. selenium, appium
@Component
public class Browser {
    WebDriver webDriver = null;

    public Browser() {
    }

    public void open() {
        getWebDriver().get("http://host.docker.internal:10081");
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

    void clickByText(String text) {
        Awaitility.await().ignoreExceptions().until(() -> getWebDriver().findElement(By.xpath("//*[text()='" + text + "']")), Objects::nonNull).click();
    }

    void inputByPlaceholder(String placeholder, String text) {
        Awaitility.await().ignoreExceptions().until(() -> getWebDriver()
                        // // 代表 root，* 代表任意元素，[] 代表元素的属性，@ 代表 element 屬性
                        // 用戶視角的元素定位相對變化小，因為這比較貼近需求，就不容易改動
                        .findElement(By.xpath("//*[@placeholder='" + placeholder + "']")), Objects::nonNull)
                .sendKeys(text);
    }

    void shouldHaveText(String text) {
        Awaitility.await().ignoreExceptions().untilAsserted(() -> Assertions.assertThat(getWebDriver().findElements(By.xpath("//*[text()='" + text + "']"))).isNotEmpty());
    }
}