import io.restassured.response.Response;
import io.restassured.response.Validatable;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.*;


public class CourierLoginTest {

    public CourierClient courierClient;
    public Courier courier;
    private int courierId;
    Response response;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = Courier.getRandom();
        //Создаем курьера
        courierClient.create(courier);
        //Авторизация
        response = courierClient.login(new Courier(courier.getLogin(), courier.getPassword()));
    }

    @After
    public void tearDown() {
        courierId = response.then().extract().path("id");
        courierClient.delete(courierId);
    }

    //Проверка, что курьер может авторизоваться с обязательными полями, код ошибки и тело ответа
    @Test
    public void testCourierLoginWithRequiredFields() {
        response.then().assertThat().body("id", is(notNullValue())).and().statusCode(200);
    }

    //Проверка авторизации без указания логина, код ошибки и тело ответа
    @Test
    public void testCourierLoginWithoutLogin() {
        courier.setLogin("");
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    //Проверка авторизации без пароля, код ошибки и тело ответа
    @Test
    public void testCourierLoginWithoutPassword() {

        courier.setPassword("");
        Response response = courierClient.login(new Courier(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    //Проверка с указанием не правильного логина
    @Test
    public void testCourierLoginWithIncorrectLogin() {
        courier.setLogin("Test");
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    //Проверка c указанием не правильного пароля
    @Test
    public void testCourierLoginWithIncorrectPassword() {
        courier.setPassword("Test");
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    //Проверка авторизовации под несуществующим пользователем
    @Test
    public void testCourierLoginWithoutCreate() {

        Courier courier = Courier.getRandom();
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(), courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    //Проверка что нельзя авторизовать курьера с пустым паролем;
    @Test
    public void testCreatedCouriersWithNullPassword() {

        courier.setPassword(null);
        Response response = courierClient.login(courier);
        response.then().log().all().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);

    }
}
