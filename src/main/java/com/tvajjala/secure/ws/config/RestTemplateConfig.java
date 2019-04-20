package com.tvajjala.secure.ws.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

import static java.util.Collections.singletonList;

/**
 * Customized RestTemplate configuration to invoke secured webServices
 *
 * @author ThirupathiReddy Vajjala
 */
@Configuration
public class RestTemplateConfig {


    /**
     * default restTemplates since we are skipping sslVerification
     *
     * @return RestTemplate {@link RestTemplate}
     * @throws Exception {@link Exception}
     */
    @Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient()));
        registerMessageConverters(restTemplate);

        return restTemplate;
    }


    /**
     * trusted RestTemplate
     *
     * @return RestTemplate {@link RestTemplate}
     * @throws Exception
     */
    @Bean
    RestTemplate trustedRestTemplate() throws Exception {

        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(trustedHttpClient()));
        registerMessageConverters(restTemplate);

        return restTemplate;
    }


    private void registerMessageConverters(RestTemplate restTemplate) {
        restTemplate.getMessageConverters().removeIf(m -> m.getClass().getName().equals(MappingJackson2HttpMessageConverter.class.getName()));
        restTemplate.getMessageConverters().add(mappingJacksonHttpMessageConverter());
        restTemplate.setInterceptors(singletonList(new RestTemplateInterceptor()));
    }


    /**
     * keyStore based httpClient trusted
     *
     * @return HttpClient  {@link HttpClient}
     * @throws Exception exception
     */
    HttpClient trustedHttpClient() throws Exception {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setSSLContext(getSSLContext(true));
        return builder.build();
    }

    /**
     * default httpClient
     *
     * @return httpClient
     * @throws Exception exception
     */
    HttpClient httpClient() throws Exception {
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(getSSLContext(false));
        return HttpClients.custom().setSSLSocketFactory(csf).build();
    }


    /**
     * Create sslContext in a two different ways
     *
     * @param trusted IsTrusted
     * @return sslContext
     * @throws Exception exception
     */
    SSLContext getSSLContext(boolean trusted) throws Exception {

        if (trusted) {
            return new SSLContextBuilder().loadTrustMaterial(new ClassPathResource("swagger.jks").getFile(), "swagger".toCharArray()).build();
        }

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        return SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

    }


    public MappingJackson2HttpMessageConverter mappingJacksonHttpMessageConverter() {
        final MappingJackson2HttpMessageConverter jaksonMessageConverter = new MappingJackson2HttpMessageConverter();
        jaksonMessageConverter.setObjectMapper(objectMapper());
        return jaksonMessageConverter;
    }


    @Bean
    public ObjectMapper objectMapper() {

        final ObjectMapper objectMapper = new ObjectMapper();

        // print timestamps as readable string in ISO format
        //objectMapper.registerModule(new JavaTimeModule());
        //objectMapper.registerModule(new Jdk8Module());

        //alternatively , this will finds and registers available modules
        objectMapper.findAndRegisterModules();

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);

        //pretty printing
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        return objectMapper;
    }


}
