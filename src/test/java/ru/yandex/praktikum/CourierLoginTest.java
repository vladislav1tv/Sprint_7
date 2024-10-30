package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.steps.CourierSteps;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.*;

public class CourierLoginTest {

    private CourierSteps courierSteps;
    private String loginCourier;
    private String passwordCourier;
    private Integer courierId;


    @Before
    public void setUp() {
        courierSteps = new CourierSteps();
    }

    @Test
    @DisplayName("Успешная авторизация курьером")
    @Step("Создание курьера и авторизация им")
    public void successfulCourierLogin() {
        createAndLoginCourier();
        // Дополнительная проверка, чтобы убедиться, что ID действительно получен
        assertThat(courierId, is(notNullValue()));
    }

    @Test
    @DisplayName("Попытка авторизации с несуществующим логином")
    @Step("Попытка авторизации с несуществующим логином")
    public void loginWithNonExistingLogin() {
        generateCredentials();
        courierSteps.loginCourier(randomAlphabetic(12), passwordCourier)
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Попытка авторизации с неверным паролем")
    @Step("Попытка авторизации с неверным паролем")
    public void loginWithWrongPassword() {
        createAndLoginCourier();
        courierSteps.loginCourier(loginCourier, randomAlphabetic(10))
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

    }

    @Test
    @DisplayName("Попытка авторизации без логина")
    @Step("Попытка авторизации без логина")
    public void loginWithoutLogin() {
        generateCredentials();
        courierSteps.loginCourier("", passwordCourier)
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Попытка авторизации без пароля")
    @Step("Попытка авторизации без пароля")
    public void loginWithoutPassword() {
        generateCredentials();
        courierSteps.loginCourier(loginCourier, "")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId.toString());
        }
    }


    private void generateCredentials(){
        loginCourier = randomAlphabetic(12);
        passwordCourier = randomAlphabetic(10);
    }

    private void createAndLoginCourier(){
        generateCredentials();
        courierSteps.createCourier(loginCourier, passwordCourier, "")
                .then()
                .statusCode(201)
                .body("ok", is(true));

        courierId = courierSteps.loginCourier(loginCourier, passwordCourier)
                .then()
                .statusCode(200)
                .extract().path("id");
    }
}
