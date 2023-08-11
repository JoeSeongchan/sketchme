package com.dutaduta.sketchme.common.controller;

import com.dutaduta.sketchme.global.ResponseFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @GetMapping("/")
    public ResponseEntity<ResponseFormat<String>> home(){
        return ResponseFormat.success("success").toEntity();
    }
}
