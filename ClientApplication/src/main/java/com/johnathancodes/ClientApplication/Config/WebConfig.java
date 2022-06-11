package com.johnathancodes.ClientApplication.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
public class WebConfig {

    @Bean
    ReactiveClientRegistrationRepository clientRegistrations(@Value("${spring.security.oauth2.client.provider.okta.token-uri}") String tokenUri,
                                                             @Value("${spring.security.oauth2.client.registration.okta.client-id}") String clientId,
                                                             @Value("${spring.security.oauth2.client.registration.okta.client-secret}") String clientSecret,
                                                             @Value("${spring.security.oauth2.client.registration.okta.scope}") String scope,
                                                             @Value("${spring.security.oauth2.client.registration.okta.authorization-grant-type}") String authGrantType) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("okta")
                .tokenUri(tokenUri)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope(scope)
                .authorizationGrantType(new AuthorizationGrantType(authGrantType))
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    private void configureHttpProxy(AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        WebClientReactiveClientCredentialsTokenResponseClient tokenResponseClient = new WebClientReactiveClientCredentialsTokenResponseClient();
        tokenResponseClient.setWebClient(WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(proxyHttpClient()))
            .build());

        ClientCredentialsReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = new ClientCredentialsReactiveOAuth2AuthorizedClientProvider();
        authorizedClientProvider.setAccessTokenResponseClient(tokenResponseClient);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
    }

    public HttpClient proxyHttpClient() {
        String proxyHost = System.getProperty("https.proxyHost");
        String proxyPort = System.getProperty("https.proxyPort");

        if (proxyHost == null && proxyPort == null) {
            return HttpClient.create();
        }

        return HttpClient.create()
                .proxy(proxy -> proxy.type(ProxyProvider.Proxy.HTTP).host(proxyHost).port(Integer.parseInt(proxyPort)));
    }

    @Bean
    AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
        return new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
    }

    @Bean
    WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, clientService);
        configureHttpProxy(authorizedClientManager);
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth.setDefaultClientRegistrationId("okta");
        return WebClient.builder()
                .filter(oauth)
                .build();
    }

}
