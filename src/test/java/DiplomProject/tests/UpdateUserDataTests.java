package DiplomProject.tests;

import DiplomProject.restClients.apiClients.ResponseChecks;
import DiplomProject.restClients.apiClients.UserApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.fail;

public class UpdateUserDataTests {
    private String email, password, name, token;
    private final ResponseChecks checks = new ResponseChecks();
    private final UserApiClient userApi = new UserApiClient();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass";
        name = "name";

        //Создание пользователя
        Response response = userApi.createUser(email, password, name);
        checks.checkStatusCode(response, 200);

        // Получение токена
        if (response.getStatusCode() == 200) {
            token = userApi.getToken(response);
        }
        if(token == null)
            fail("Тестовый пользователь не создан");

    }

    @After
    @Step("Удаление тестовых пользователей")
    public void cleanTestData() {
        if(token == null)
            return;

        checks.checkStatusCode(userApi.deleteUser(token), 202);
    }

    @Test
    @DisplayName("Изменение данных пользователя: с авторизацией")
    public void changeUserDataWithAuthIsSuccess() {
        String newEmail = "new_" + email;
        String newPassword = "new_" + password;
        String newName = "new_" + name;

        Response response = userApi.updateUser(newEmail, newPassword, newName, token);

        checks.checkStatusCode(response, 200);
        checks.checkLabelSuccess(response, "true");
        userApi.checkUser(response, newEmail, newPassword, newName);
    }

    @Test
    @DisplayName("Изменение данных пользователя: с авторизацией, поля без изменений")
    public void changeUserDataWithAuthWhenSendSameDataIsFailed() {
        Response response = userApi.updateUser("test-data@yandex.ru", password, name, token);

        checks.checkStatusCode(response, 403);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "User with such email already exists");
    }

    @Test
    @DisplayName("Попытка изменения данных пользователя: без авторизации")
    public void changeUserDataWithoutAuthIsFailed() {
        String newEmail = "new_" + email;
        String newPassword = "new_" + password;
        String newName = "new_" + name;

        Response response = userApi.updateUser(newEmail, newPassword, newName, "");

        checks.checkStatusCode(response, 401);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "You should be authorised");
    }
}

