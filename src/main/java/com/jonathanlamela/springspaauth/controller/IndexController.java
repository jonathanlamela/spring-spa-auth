package com.jonathanlamela.springspaauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonathanlamela.springspaauth.model.response.GenericResponse;

@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<GenericResponse> index() {
        return ResponseEntity.ok(new GenericResponse("success", "api server works"));
    }

}
