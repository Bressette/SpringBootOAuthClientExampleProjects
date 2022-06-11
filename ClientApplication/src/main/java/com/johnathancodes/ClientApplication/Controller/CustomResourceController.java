package com.johnathancodes.ClientApplication.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.johnathancodes.ClientApplication.Dto.FetchOktaTokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CustomResourceController {

    @Autowired
    FetchOktaTokenDto fetchOktaTokenDto;

    @GetMapping("fetchOktaToken")
    public ResponseEntity<Mono<JsonNode>> fetchOktaToken() {
        return new ResponseEntity<>(fetchOktaTokenDto.fetchOktaToken(), HttpStatus.OK);
    }

    @Autowired
    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    @GetMapping("fetchTokenDirectly")
    public ResponseEntity<String> fetchTokenDirectly() {
        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("okta")
                .principal("TestResourceServer")
                .build();
        OAuth2AuthorizedClient auth2AuthorizedClient = this.authorizedClientManager.authorize(oAuth2AuthorizeRequest).block();
        return new ResponseEntity<>(auth2AuthorizedClient.getAccessToken().getTokenValue(), HttpStatus.OK);
    }

    @GetMapping("fetchSecureResource")
    public ResponseEntity<Mono<ObjectNode>> fetchSecureResource() {
        return new ResponseEntity<>(fetchOktaTokenDto.fetchSecureData(), HttpStatus.OK);
    }
}
