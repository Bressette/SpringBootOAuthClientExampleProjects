package com.johnathancodes.ResourceServer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("testData")
    public ResponseEntity<ObjectNode> testData() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode testData = objectMapper.createObjectNode();
        testData.put("name", "Johnathan");
        testData.put("email", "johnathan@jommer.chat");
        testData.put("id", "123456");
        testData.put("display_name", "HaZe");
        return new ResponseEntity<>(testData, HttpStatus.OK);
    }

}
