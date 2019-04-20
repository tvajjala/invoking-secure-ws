package com.tvajjala.secure.ws.config;

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
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;

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

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient()));
    }


    /**
     * trusted RestTemplate
     *
     * @return RestTemplate {@link RestTemplate}
     * @throws Exception
     */
    @Bean
    RestTemplate trustedRestTemplate() throws Exception {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(trustedHttpClient()));
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


}
