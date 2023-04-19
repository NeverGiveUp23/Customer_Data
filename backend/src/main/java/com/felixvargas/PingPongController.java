package com.felixvargas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    record PingPong(String message) {}

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("Pong");
    }
}