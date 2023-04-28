package com.felixvargas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    private static int COUNTER = 0;

    public String counterNumber(){
        String message = "counter is less than 5 Counter is currently: %s".formatted(COUNTER);
        String message2 = "counter is greater than 5 Counter is currently: %s".formatted(COUNTER);

        if(COUNTER < 5){
            ++COUNTER;
            return message;
        }
        else{
            ++COUNTER;
            return message2;
        }
    }





    public String counterDivideNumber(){
        Integer counter = COUNTER;
        if(COUNTER == 20){
            return counter.toString();
        }
        else{
            return "Counter is currently %s".formatted(counter / 2) ;
        }
    }
    record PingPong(String result, String result2) {}


    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong(counterNumber(), counterDivideNumber());
    }
}
