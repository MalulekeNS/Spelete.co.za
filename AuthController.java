package com.spelete.shop.controller;
import com.spelete.shop.dto.AuthDtos.*; import com.spelete.shop.service.CartService; import com.spelete.shop.service.UserService; import com.spelete.shop.util.SessionUtil;
import jakarta.servlet.http.*; import jakarta.validation.Valid; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
  private final UserService users; private final CartService carts;
  public AuthController(UserService users, CartService carts){ this.users = users; this.carts = carts; }
  @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req, HttpServletRequest httpReq){
    var u = users.register(req.name, req.email.toLowerCase(), req.password);
    HttpSession s = SessionUtil.session(httpReq); s.setAttribute(SessionUtil.USER_ID_ATTR, u.getId());
    s.setAttribute(SessionUtil.USER_NAME_ATTR, u.getName()); s.setAttribute(SessionUtil.USER_EMAIL_ATTR, u.getEmail());
    carts.mergeSessionIntoUser(s.getId(), u.getId()); return ResponseEntity.ok(new UserResponse(u.getId(), u.getName(), u.getEmail()));
  }
  @PostMapping("/login") public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletRequest httpReq){
    var u = users.authenticate(req.email.toLowerCase(), req.password);
    HttpSession s = SessionUtil.session(httpReq); s.setAttribute(SessionUtil.USER_ID_ATTR, u.getId());
    s.setAttribute(SessionUtil.USER_NAME_ATTR, u.getName()); s.setAttribute(SessionUtil.USER_EMAIL_ATTR, u.getEmail());
    carts.mergeSessionIntoUser(s.getId(), u.getId()); return ResponseEntity.ok(new UserResponse(u.getId(), u.getName(), u.getEmail()));
  }
  @GetMapping("/me") public ResponseEntity<?> me(HttpServletRequest req){
    HttpSession s=req.getSession(false); if(s==null||s.getAttribute(SessionUtil.USER_ID_ATTR)==null) return ResponseEntity.ok().body(null);
    Long id=(Long)s.getAttribute(SessionUtil.USER_ID_ATTR); String name=(String)s.getAttribute(SessionUtil.USER_NAME_ATTR); String email=(String)s.getAttribute(SessionUtil.USER_EMAIL_ATTR);
    return ResponseEntity.ok(new UserResponse(id,name,email));
  }
  @PostMapping("/logout") public ResponseEntity<?> logout(HttpServletRequest req){ HttpSession s=req.getSession(false); if(s!=null) s.invalidate(); return ResponseEntity.ok().build(); }
}
