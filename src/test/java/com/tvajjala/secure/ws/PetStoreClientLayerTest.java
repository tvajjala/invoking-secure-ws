package com.tvajjala.secure.ws;

import com.tvajjala.secure.ws.client.BadRequestException;
import com.tvajjala.secure.ws.client.PetStoreClient;
import com.tvajjala.secure.ws.model.Order;
import org.junit.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestGatewaySupport;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * This test cases will test complete {@link PetStoreClient} by mocking underlying service without using Mock framework.
 *
 * @author ThirupathiReddy Vajjala
 */
public class PetStoreClientLayerTest {


    static PetStoreClient petStoreClient;


    static MockRestServiceServer mockServer;

    @BeforeClass// helps you to create server once for all tests
    public static void setup() {
        RestTemplate restTemplate = new RestTemplate();
        RestGatewaySupport restGatewaySupport = new RestGatewaySupport();
        restGatewaySupport.setRestTemplate(restTemplate);
        mockServer = MockRestServiceServer.createServer(restGatewaySupport);
        petStoreClient = new PetStoreClient(restTemplate, "http://fake.petstore.com");

    }

    @Before
    public void init() {
        mockServer.reset();//reset server behaviour after each test
    }


    /**
     * Scenario:  When Server returns success response, placeOrder should return {@link Order} instance as response
     */
    @Test
    public void placeOrderTest() {

        //given:
        mockServer.expect(anything())
                .andRespond(withSuccess(new ClassPathResource("success_response.json"), APPLICATION_JSON));

        //when: invoke placeOrder
        Order order = petStoreClient.placeOrder(new Order());

        //then: expect positive response
        Assert.assertEquals("placed", order.getStatus());
    }


    /**
     * Scenario:  When Server returns bad request, placeOrder should throw BadRequestException
     */
    @Test(expected = BadRequestException.class)
    public void testWhenServerReturnsException() {

        //given:
        mockServer.expect(anything())
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.BAD_REQUEST));

        //when: invoke placeOrder
        petStoreClient.placeOrder(new Order());

        //then: expect BadRequestException
    }


    @After
    public void tearDown() {
        mockServer.verify();// Verify contract
    }

}
