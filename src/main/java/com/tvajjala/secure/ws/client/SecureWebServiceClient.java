package com.tvajjala.secure.ws.client;

import com.tvajjala.secure.ws.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * WebService client invokes external webservices using RestTemplate
 *
 * @author ThirupathiReddy Vajjala
 */
@Component
public class SecureWebServiceClient {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private RestTemplate restTemplate;


    private String URL = "https://petstore.swagger.io/v2/pet/12";

    @Autowired
    public SecureWebServiceClient(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;

    }


    /**
     * This Api call returns Pet information based on petId
     *
     * @param petId petId
     * @return Pet {@link Pet}
     */
    public Pet getPetById(Integer petId) {

        LOG.info("Invoking petService  by petId {} ", petId);
        ResponseEntity<Pet> responseEntity = restTemplate.getForEntity(URL, Pet.class, 1);

        if (responseEntity.getStatusCode().is4xxClientError()) {
            LOG.info("Error {} ", responseEntity.getStatusCode().getReasonPhrase());
            throw new RuntimeException("Pet not found");
        }

        return responseEntity.getBody();
    }


}
