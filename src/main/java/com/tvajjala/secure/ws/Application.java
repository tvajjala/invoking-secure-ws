package com.tvajjala.secure.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tvajjala.secure.ws.client.PetStoreClient;
import com.tvajjala.secure.ws.model.Order;
import com.tvajjala.secure.ws.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.TimeZone;

/**
 * @author ThirupathiReddy Vajjala
 */
@RestController
@SpringBootApplication
public class Application implements CommandLineRunner {


    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    PetStoreClient petStoreClient;

    public static void main(String[] args) throws JsonProcessingException {

        /** This will helps you to run your application in specific timezone irrespective of your system time */
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));

        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        Pet pet = petStoreClient.getPetById(22);
        LOG.info("Received response {} ", pet);

    }


    @GetMapping("/order")
    public Order getOrder() {
        Order order = new Order();
        order.setShipDate(Instant.now());
        order.setPetId(22);
        order = petStoreClient.placeOrder(order);

        LOG.info("Order status {} ", order);
        return order;
    }

}
