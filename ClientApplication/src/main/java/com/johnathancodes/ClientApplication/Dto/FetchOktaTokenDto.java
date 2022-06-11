package com.johnathancodes.ClientApplication.Dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FetchOktaTokenDto {

    private final WebClient webClient = WebClient.builder().build();

    private final WebClient secureWebClient;

    public FetchOktaTokenDto(WebClient secureWebClient) {
        this.secureWebClient = secureWebClient;
    }

    public Mono<JsonNode> fetchOktaToken() {
        return webClient.post()
                .uri("https://dev-75166832.okta.com/oauth2/default/v1/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .headers(headers -> headers.setBasicAuth("0oa5a815y0AtNAPNI5d7", "IZjsoXulj146gd-1gDe3bOdS6t67Z-E4I--M_uwP"))
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                .with("scope", "Custom_Scope"))
                .retrieve()
                .bodyToMono(JsonNode.class);
    }

    public Mono<ObjectNode> fetchSecureData() {
        return webClient.get()
                .uri("http://localhost:8080/testData")
                .retrieve()
                .bodyToMono(ObjectNode.class);
    }

}
