import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private static final String COURIER_PATH = "/api/v1/orders/";

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Получить список заказов")
    public Response takeOrderList() {
        return given()
                .spec(getBaseSpec())
                .get(COURIER_PATH);
    }


}
