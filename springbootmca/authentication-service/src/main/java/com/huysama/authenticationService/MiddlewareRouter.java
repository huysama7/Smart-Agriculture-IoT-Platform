package com.huysama.authenticationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiddlewareRouter {
    @GetMapping("/auth/verify")
    public ResponseEntity<?> verify(@RequestHeader("Authorization") String authHeader) {

        System.out.println("Instance ID: " + MiddleWareConfig.instance);
        if (authHeader.contains("Huy")) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
