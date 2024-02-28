package com.odde.atddv2.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Page Object
// 在 Home page 的登入操作集合都要重構到 Home page 類別中
@Component
public class HomePage {
    @Autowired
    Browser browser;

    public void login(String userName, String password) {
        browser.inputByPlaceholder("用户名", userName);
        browser.inputByPlaceholder("密码", password);
        browser.clickByText("登录");
    }

    public void shouldHaveText(String text) {
        browser.shouldHaveText(text);
    }

}