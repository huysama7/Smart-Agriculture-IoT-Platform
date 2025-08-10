package com.huysama.authenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class MiddlewareRouter {
    @RequestMapping(value = "/auth/verify", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> verify(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        System.out.println("Instance ID: " + MiddleWareConfig.instance);
        if (authHeader != null && authHeader.contains("error")) {
            log.error("Simulated error triggered!");
            throw new RuntimeException("Simulated error for testing");
        }
        if (authHeader != null && authHeader.contains("Huy")) {
            log.info("Authorization successful, adding headers for downstream service");
            return ResponseEntity.ok()
                    .header("X-User-Id", "12345")  // custom header gửi cho service kia
                    .header("X-User-Role", "ADMIN") // role hoặc quyền
                    .build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
