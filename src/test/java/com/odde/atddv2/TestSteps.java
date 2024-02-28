package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.java.After;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
import okhttp3.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.openqa.selenium.By.xpath;

public class TestSteps {
    @Autowired
    UserRepo userRepo;
    private WebDriver webDriver = null;
    private Response response;

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @当("测试环境")
    public void 测试环境() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='登录']"))).isNotEmpty();
    }

    @After
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }

    @那么("打印Token")
    public void 打印_token() {
        System.out.println("response.header(\"token\") = " + response.header("token"));
    }

    @SneakyThrows
    @那么("打印百度为您找到的相关结果数")
    public void 打印百度为您找到的相关结果数() {
        TimeUnit.SECONDS.sleep(2);
        String text = webDriver.findElement(xpath("//*[@id='container']/div[2]/div/div[2]/span")).getText();
        System.out.println("text = " + text);
    }

    @假如("存在用户名为{string}和密码为{string}的用户")
    public void 存在用户名为和密码为的用户(String userName, String password) {
        userRepo.deleteAll();
        userRepo.save(new User().setUserName(userName).setPassword(password));
    }

    @SneakyThrows
    @当("通过API以用户名为{string}和密码为{string}登录时")
    public void 通过api以用户名为和密码为登录时(String userName, String password) {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(new User().setUserName(userName).setPassword(password)));
        Request request = new Request.Builder().url("http://localhost:10081/users/login").post(requestBody).build();
        response = okHttpClient.newCall(request).execute();
    }

    @SneakyThrows
    @当("在百度搜索关键字{string}")
    public void 在百度搜索关键字(String keyword) {
        getWebDriver().get("http://www.baidu.com");
        webDriver.findElement(By.xpath("//*[@id='kw']")).sendKeys(keyword);
        webDriver.findElement(By.xpath("//*[@id='su']")).click();
    }

    public WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }

    @当("以用户名为{string}和密码为{string}登录时")
    public void 以用户名为和密码为登录时(String username, String password) {
        getWebDriver().get("http://host.docker.internal:10081/");
        getWebDriver().findElement(By.xpath("//*[@id=\"app\"]/div/form/div[2]/div/div/input"))
                .sendKeys(username);
        getWebDriver().findElement(By.xpath("//*[@id=\"app\"]/div/form/div[3]/div/div/input"))
                .sendKeys(password);
        getWebDriver().findElement(By.xpath("//*[@id=\"app\"]/div/form/button"))
                .click();

        // 暫停畫面以利驗證測試程式
        // TimeUnit.SECONDS.sleep(10);
    }

    @那么("{string}登录成功")
    public void 登录成功(String username) {
        // 用 await().ignoreExceptions().untilAsserted() 的 retry 機制
        // 避免畫面還沒渲染完成就進行驗證
        await().ignoreExceptions().untilAsserted(
                () -> assertThat(getWebDriver()
                        // 這裡的 xpath 是透過瀏覽器的開發者工具取得
                        // 用 xpath 抓取元素的方式，畫面一改變就會失效，而且改很多地方
                        .findElement(By.xpath("//*[@id=\"app\"]/div/div[2]/section/div"))
                        .getText())
                        .isEqualTo("Welcome " + username)
                        // 嚴格驗證：能用 equal 就用 equal 驗證 --> 避免 false positive
        );
    }

    @那么("登录失败的错误信息是{string}")
    public void 登录失败的错误信息是(String errorMessage) {
        await().ignoreExceptions().untilAsserted(
                () -> assertThat(getWebDriver()
                        .findElement(By.xpath("//*[@id=\"app\"]/div/form/div[4]"))
                        .getText())
                        .isEqualTo(errorMessage)
        );
    }
}
