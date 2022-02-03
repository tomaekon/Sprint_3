import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    public CourierClient courierClient;
    public int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    //Проверка, что курьера можно создать с обязательными полями, код ошибки и тело ответа
    @Test
    public void testCourierIsCreatedWithRequiredFields() {

        Courier courier = Courier.getRandom();
        Response response = courierClient.create(courier);
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);

        //Получаем ID для удаления курьера
        courierId = courierClient.login(new Courier(courier.getLogin(),courier.getPassword())).then().extract().path("id");
    }

    //Проверка что нельзя создать двух одинаковых курьеров, код ошибки и тело ответа
    @Test
    public void testNotCreatedTwoEqualsCouriers() {

        Courier courier = Courier.getRandom();
        //Создаем первого курьера
        courierClient.create(courier);
        //Создаем второго курьера
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).and().statusCode(409);
    }

    //Проверка что нельзя создать курьеров с одинаковыми логинами, код ошибки и тело ответа
    @Test
    public void testNotCreatedCouriersWithEqualsLogin() {

        Courier courier = Courier.getRandom();
        //Создаем первого курьера
        courierClient.create(courier);
        //Создаем второго курьера
        courier.setPassword("123");
        courier.setFirstName("Test");
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется")).and().statusCode(409);
    }

    //Проверка что нельзя создать курьера без логина, код ошибки и тело ответа
    @Test
    public void testCreatedCouriersWithoutLogin() {

        Courier courier = Courier.getRandom();
        courier.setLogin("");
        Response response = courierClient.create(courier);
        response.then().log().all().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    //Проверка что нельзя создать курьера без пароля, код ошибки и тело ответа
    @Test
    public void testCreatedCouriersWithoutPassword() {

        Courier courier = Courier.getRandom();
        courier.setPassword("");
        Response response = courierClient.create(courier);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);

    }

    //Проверка чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @Test
    public void testCreatedCouriersWithoutFirstName() {

        Courier courier = Courier.getRandom();
        courier.setFirstName("");
        Response response = courierClient.create(courier);
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);

        //Получаем ID для удаления курьера
        courierId = courierClient.login(new Courier(courier.getLogin(),courier.getPassword())).then().extract().path("id");
    }

}


