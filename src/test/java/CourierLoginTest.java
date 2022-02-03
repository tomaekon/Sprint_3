import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.*;


public class CourierLoginTest {

    public CourierClient courierClient;
    private int courierId;

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

    //Проверка, что курьер может авторизоваться с обязательными полями, код ошибки и тело ответа
    @Test
    public void testCourierLoginWithRequiredFields() {

        Courier courier = Courier.getRandom();
        //Создаем курьера
        courierClient.create(courier);

        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(),courier.getPassword()));
        response.then().assertThat().body("id", is(notNullValue())).and().statusCode(200);

        //Получаем ID для удаления курьера
        courierId = response.then().extract().path("id");
    }

    //Проверка авторизации без указания логина, код ошибки и тело ответа
    @Test
    public void testCourierLoginWithoutLogin() {

        Courier courier = Courier.getRandom();
        //Создаем курьера
        courierClient.create(courier);
        courier.setLogin("");

        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(),courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    //Проверка авторизации без пароля, код ошибки и тело ответа
    @Test
    public void testCourierLoginWithoutPassword() {

        Courier courier = Courier.getRandom();
        //Создаем курьера
        courierClient.create(courier);
        courier.setPassword("");

        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(),courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);
    }

    //Проверка с указанием не правильного логина
    @Test
    public void testCourierLoginWithIncorrectLogin() {

        Courier courier = Courier.getRandom();
        //Создаем курьера
        courierClient.create(courier);
        courier.setLogin("Test");
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(),courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    //Проверка c указанием не правильного пароля
    @Test
    public void testCourierLoginWithIncorrectPassword() {

        Courier courier = Courier.getRandom();
        //Создаем курьера
        courierClient.create(courier);
        courier.setPassword("Test");
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(),courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    //Проверка авторизовации под несуществующим пользователем
    @Test
    public void testCourierLoginWithoutCreate() {

        Courier courier = Courier.getRandom();
        //Авторизация
        Response response = courierClient.login(new Courier(courier.getLogin(),courier.getPassword()));
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }

    //Проверка что нельзя авторизовать курьера с пустым паролем;
    @Test
    public void testCreatedCouriersWithNullPassword() {

        Courier courier = Courier.getRandom();
        courier.setPassword(null);
        Response response = courierClient.login(courier);
        response.then().log().all().assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);

    }
}
