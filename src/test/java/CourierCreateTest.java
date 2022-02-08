import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    public CourierClient courierClient;
    public Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        try {
            courierId = courierClient.login(new Courier(courier.getLogin(), courier.getPassword())).then().extract().path("id");
            if (courierId != 0) courierClient.delete(courierId);
        } catch (NullPointerException e) {
            System.out.println("Passed with Exception");
        }
    }

    //Проверка, что курьера можно создать, код ошибки и тело ответа
    @Test
    public void testCourierIsCreatedWithRequiredFields() {

        courier = Courier.getRandom();
        Response response = courierClient.create(courier);
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    //Проверка что нельзя создать двух одинаковых курьеров, код ошибки и тело ответа
    @Test
    public void testNotCreatedTwoEqualsCouriers() {

        courier = Courier.getRandom();
        //Создаем первого курьера
        courierClient.create(courier);
        //Создаем второго курьера
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).and().statusCode(409);
    }

    //Проверка что нельзя создать курьеров с одинаковыми логинами, код ошибки и тело ответа
    @Test
    public void testNotCreatedCouriersWithEqualsLogin() {

        courier = Courier.getRandom();
        //Создаем первого курьера
        String login = courier.getLogin();
        courierClient.create(courier);

        //Создаем второго курьера
        Courier courier = Courier.getRandom();
        courier.setLogin(login);
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).and().statusCode(409);
    }

    //Проверка что нельзя создать курьера без логина, код ошибки и тело ответа
    @Test
    public void testCreatedCouriersWithoutLogin() {

        courier = Courier.getRandom();
        courier.setLogin("");
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    //Проверка что нельзя создать курьера без пароля, код ошибки и тело ответа
    @Test
    public void testCreatedCouriersWithoutPassword() {

        courier = Courier.getRandom();
        courier.setPassword("");
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    //Проверка чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @Test
    public void testCreatedCouriersWithoutFirstName() {

        courier = Courier.getRandom();
        courier.setFirstName("");
        Response response = courierClient.create(courier);
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

}


