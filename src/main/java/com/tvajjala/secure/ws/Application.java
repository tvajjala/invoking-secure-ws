package com.tvajjala.secure.ws;

import com.tvajjala.secure.ws.client.SecureWebServiceClient;
import com.tvajjala.secure.ws.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ThirupathiReddy Vajjala
 */
@SpringBootApplication
public class Application implements CommandLineRunner {


    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    SecureWebServiceClient secureWebServiceClient;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {

        Pet pet = secureWebServiceClient.getPetById(1);

        LOG.info("Received response {} ", pet);

    }


}
