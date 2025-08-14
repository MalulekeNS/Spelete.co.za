package com.spelete.shop.controller;
import com.spelete.shop.dto.ContactDto; import jakarta.validation.Valid; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/contact")
public class ContactController {
  @PostMapping public ResponseEntity<?> submit(@Valid @RequestBody ContactDto dto){ return ResponseEntity.ok().build(); }
}
