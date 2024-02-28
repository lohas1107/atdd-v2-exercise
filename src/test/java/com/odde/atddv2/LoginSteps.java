package com.odde.atddv2;

import com.odde.atddv2.entity.User;
import com.odde.atddv2.page.HomePage;
import com.odde.atddv2.repo.UserRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginSteps {
    @Autowired
    private HomePage homePage;

    @Autowired
    private Browser browser;

    @Autowired
    private UserRepo userRepo;

    @假如("存在用户名为{string}和密码为{string}的用户")
    public void 存在用户名为和密码为的用户(String userName, String password) {
        userRepo.save(new User().setUserName(userName).setPassword(password));
    }

    @当("以用户名为{string}和密码为{string}登录时")
    public void 以用户名为和密码为登录时(String userName, String password) {
        homePage.open();
        homePage.login(userName, password);
    }

    @那么("{string}登录成功")
    public void 登录成功(String userName) {
        browser.shouldHaveText("Welcome " + userName);
    }

    @那么("登录失败的错误信息是{string}")
    public void 登录失败的错误信息是(String message) {
        browser.shouldHaveText(message);
    }

    @Before("@ui-login")
    public void uiLogin() {
        homePage.open();
        homePage.login("joseph", "123");
    }

    @当("用如下数据录入订单:")
    public void 用如下数据录入订单(DataTable table) {
        browser.clickByText("订单");
//        table.asLists().forEach(row -> {
//            browser.inputTextByPlaceholder("订单号", row.get(0));
//            browser.inputTextByPlaceholder("商品名称", row.get(1));
//            browser.inputTextByPlaceholder("金额", row.get(2));
//            browser.inputTextByPlaceholder("收件人", row.get(3));
//            browser.inputTextByPlaceholder("电话", row.get(4));
//            browser.inputTextByPlaceholder("地址", row.get(5));
//            browser.clickDropDownByPlaceholder("状态");
//            browser.clickDropDownItemByText(row.get(6));
//        });
//        browser.clickByText("录入");
    }

    @那么("显示如下订单")
    public void 显示如下订单() {

    }
}
