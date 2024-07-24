package DiplomProject.tests;

import DiplomProject.restClients.apiClients.ResponseChecks;
import DiplomProject.restClients.apiClients.UserApiClient;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.UUID;

import static org.junit.Assert.fail;

@DisplayName("2. Логин пользователя")
public class LoginUserTests {
    private String email, password, name, token;
    private final UserApiClient userApi = new UserApiClient();
    private final ResponseChecks checks = new ResponseChecks();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass";
        name = "name";

        // Создание пользователя
        Response response = userApi.createUser(email, password, name);
        checks.checkStatusCode(response, 200);

        // Получение токена авторизации
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }
        if (token == null)
            fail("Пользователь не создан");

    }
    @After
    @Step("Удаление тестовых пользователей")
    public void cleanTestData() {
        if(token.isEmpty())
            return;

        checks.checkStatusCode(userApi.deleteUser(token), 202);
    }

    @Test
    @DisplayName("Логин существующим пользователем")
    public void loginUserIsSuccess() {
        Response response = userApi.loginUser(email, password);

        checks.checkStatusCode(response, 200);
        checks.checkLabelSuccess(response, "true");
    }
    @Test
    @DisplayName("Логин с некорректным e-mail")
    public void loginUserIncorrectEmailIsFailed() {
        Response response = userApi.loginUser("newE-mail_" + UUID.randomUUID() + "@mail.com", password);

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Логин с некорректным паролем")
    public void loginUserIncorrectPasswordIsFailed() {
        Response response = userApi.loginUser(email, password  + UUID.randomUUID());

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя без email-а")
    public void loginUserMissedEmailIsFailed() {
        Response response = userApi.loginUser("", password);

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }
    @Test
    @DisplayName("Авторизация пользователя без пароля")
    public void loginUserMissedPasswordIsFailed() {
        Response response = userApi.loginUser(email, "");

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "email or password are incorrect");
    }

}
