package test;

import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.cleanDatabase;


public class BankLoginTest {
    @AfterAll
    static void wipeOut() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from SUT test data")
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authData = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authData);
        verificationPage.verificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should shows error notification if user not exist in database")
    void shouldNotLoginRandomUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotificationVisibility();
    }

    @Test
    @DisplayName("Should shows error notification if existing user from data with random verification code")
    void shouldShowsErrorNotificationIlLoginWithRandomVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authData = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authData);
        verificationPage.verificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.errorNotificationVisibility();
    }
}