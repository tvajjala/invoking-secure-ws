package com.tvajjala.secure.ws.client;

import com.tvajjala.secure.ws.model.Order;
import com.tvajjala.secure.ws.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * WebService client invokes external webservices using RestTemplate
 *
 * @author ThirupathiReddy Vajjala
 */
@Component
public class PetStoreClient {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private RestTemplate trustedRestTemplate;

    private String baseUrl;


    @Autowired
    public PetStoreClient(RestTemplate trustedRestTemplate, @Value("${store.base.url}") String baseUrl) {

        this.trustedRestTemplate = trustedRestTemplate;

        this.baseUrl = baseUrl;
    }


    /**
     * This Api call returns Pet information based on petId
     *
     * @param petId petId
     * @return Pet {@link Pet}
     */
    public Pet getPetById(Integer petId) {

        LOG.info("Invoking petService  with etId {} ", petId);
        ResponseEntity<Pet> responseEntity = trustedRestTemplate.getForEntity(baseUrl + "/pet/{petId}", Pet.class, petId);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            LOG.info("Returning success response ");
            return responseEntity.getBody();
        }

        LOG.error("Error while reading response {}", responseEntity.getStatusCode());
        throw new RuntimeException(responseEntity.getStatusCode().getReasonPhrase());

    }


    /**
     * Place Order online
     *
     * @param order order
     * @return status
     */
    public Order placeOrder(Order order) {

        try {
            ResponseEntity<Order> responseEntity = trustedRestTemplate.postForEntity(baseUrl + "/store/order", order, Order.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new BadRequestException(e);
        }

    }
}
