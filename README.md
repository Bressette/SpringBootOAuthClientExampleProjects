# SpringBootOAuthClientExampleProjects

This repository contains two projects that contains the accompanying example code for a blog post on calling OAuth2 secured resource servers from within a Spring Boot back end application.  

## ClientApplication
The ClientApplication project contains the configuration and code to retrieve an access token using the client credentials flow using the Spring Boot Starter OAuth2 Client. There are examples for fetching the token explicitly with a POST request, adding a WebClient filter that automatically adds the token to requests, and configuring a ClientManager bean that lets you access the token in code. 

## ResourceServer
The ResourceServer project is a simple Spring Boot resource server that uses the Spring Boot Starter Oauth2 Resource Server and a single configuration property to secure a simple REST endpoint with OAuth2. 
