import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class CreateOrderParametrizedTest {
    private final List<String> color;
    private final Matcher expected;
    public OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    public CreateOrderParametrizedTest(List<String> color, Matcher expected) {
        this.color = color;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {List.of("BLACK"), notNullValue()},
                {List.of("BLACK", "GREY"), notNullValue()},
                {List.of("GREY"), notNullValue()},
                {List.of(" "), notNullValue()},
        };
    }

    //Проверка оформления заказа с различными значениями параметра color
    @Test
    public void createOderTest() {
        Order order = new Order(color);
        order.setColor(color);
        Response response = orderClient.createOrder(order);
        response.then().assertThat().statusCode(201).and().body("track", expected);
    }
}
