package DiplomProject.tests;


import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import DiplomProject.restClients.apiClients.ResponseChecks;
import DiplomProject.restClients.apiClients.UserApiClient;

import java.util.ArrayList;
import java.util.UUID;

@DisplayName("1. Тесты на создание пользователя")

public class CreateUserTests {
    private String email, password, name;
    private ArrayList<String> tokens = new ArrayList<>();
    private final UserApiClient userApi = new UserApiClient();
    private final ResponseChecks checks = new ResponseChecks();

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        email = "e-mail_" + UUID.randomUUID() + "@mail.com";
        password = "pass_" + UUID.randomUUID();
        name = "name";
    }
    @After
    @Step("Удаление тестовых пользователей")
    public void deletingUser() {
        if(tokens.isEmpty())
            return;
        for (String token: tokens) {
            checks.checkStatusCode(userApi.deleteUser(token), 202);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createNewUserIsSuccess() {
        Response response = userApi.createUser(email, password, name);
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 200);
        checks.checkLabelSuccess(response, "true");
    }
    @Test
    @DisplayName("Создание пользователя который уже зарегистрирован")
    public void createNewSimilarUsersIsFailed() {
        Response responseFirstUser = userApi.createUser(email, password, name);
        Response responseSecondUser = userApi.createUser(email, password, name);
        if (responseFirstUser.getStatusCode() == 200) {
            tokens.add(userApi.getToken(responseFirstUser));
        }
        if (responseSecondUser.getStatusCode() == 200) {
            tokens.add(userApi.getToken(responseSecondUser));
        }

        checks.checkStatusCode(responseSecondUser, 403);
        checks.checkLabelSuccess(responseSecondUser, "false");
        checks.checkLabelMessage(responseSecondUser, "User already exists");
    }
    @Test
    @DisplayName("Создание пользователя без email")
    @Description ("Проверка возможности  создания пользователя, без обязательного поля")
    public void createNewUserMissedEmailIsFailed() {
        Response response = userApi.createUser("", password, name);
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "Email, password and name are required fields");
    }
    @Test
    @DisplayName("Создание пользователя без password")
    @Description ("Проверка возможности  создания пользователя, без обязательного поля")
    public void createNewUserMissedPasswordIsFailed() {
        Response response = userApi.createUser(email, "", name);
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "Email, password and name are required fields");
    }
    @Test
    @DisplayName("Создание пользователя без name")
    @Description ("Проверка возможности  создания пользователя, без обязательного поля")
    public void createNewUserMissedNameIsFailed() {
        Response response = userApi.createUser(email, password, "");
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "Email, password and name are required fields");
    }
    @Test
    @DisplayName("Создание пользователя без всех заполненных полей")
    @Description ("Проверка возможности  создания пользователя, без обязательного поля")
    public void createNewUserMissedAllParamsIsFailed() {
        Response response = userApi.createUser("", "", "");
        if (response.getStatusCode() == 200) {
            tokens.add(userApi.getToken(response));
        }

        checks.checkStatusCode(response, 403);
        checks.checkLabelSuccess(response, "false");
        checks.checkLabelMessage(response, "Email, password and name are required fields");
    }
}
