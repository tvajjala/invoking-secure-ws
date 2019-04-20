package com.tvajjala.secure.ws.config;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {


    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        LOG.debug("Request with headers {} and  \n{} ", request.getHeaders(), new String(body, Charsets.UTF_8));


        ClientHttpResponse clientHttpResponse = execution.execute(request, body);


        // LOG.debug("Received response {}", StreamUtils.copyToString(clientHttpResponse.getBody(), Charsets.UTF_8));

        return clientHttpResponse;
    }
}
