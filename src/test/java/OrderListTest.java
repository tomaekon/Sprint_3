import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    public OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    //Проверка что список заказов не пустой
    @Test
    public void oderListIsNotNull() {

        Response response = orderClient.takeOrderList();
        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());
    }
}

