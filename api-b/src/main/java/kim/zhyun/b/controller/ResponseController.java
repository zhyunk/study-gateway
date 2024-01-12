package kim.zhyun.b.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
public class ResponseController {

    @GetMapping("/in")
    public ResponseEntity<Object> in(@Value("${server.servlet.context-path}") String path) {
        return ResponseEntity.ok(new Object() {
            public final String type = path;
            public final LocalDate date = LocalDate.now();
            public final LocalTime time = LocalTime.now();
        });
    }
    
}
