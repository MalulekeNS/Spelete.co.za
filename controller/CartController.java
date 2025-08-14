package com.spelete.shop.controller;
import com.spelete.shop.dto.CartDtos.*; import com.spelete.shop.entity.CartItem; import com.spelete.shop.service.CartService; import com.spelete.shop.util.SessionUtil;
import jakarta.servlet.http.*; import jakarta.validation.Valid; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/cart")
public class CartController {
  private final CartService service; public CartController(CartService service){ this.service = service; }
  private Long currentUserId(HttpSession s){ return (Long) s.getAttribute(SessionUtil.USER_ID_ATTR); }
  @GetMapping public List<CartItem> getCart(HttpServletRequest req){ HttpSession s=SessionUtil.session(req); Long userId=currentUserId(s); return userId!=null?service.getForUser(userId):service.getForSession(s.getId()); }
  @PostMapping("/add") public ResponseEntity<?> add(@Valid @RequestBody AddItemRequest body, HttpServletRequest req){ HttpSession s=SessionUtil.session(req); Long userId=currentUserId(s); service.add(userId, userId==null?s.getId():null, body.productId, body.quantity); return ResponseEntity.ok().build(); }
  @PutMapping("/qty") public ResponseEntity<?> setQty(@Valid @RequestBody UpdateQtyRequest body, HttpServletRequest req){ HttpSession s=SessionUtil.session(req); Long userId=currentUserId(s); service.setQty(userId, userId==null?s.getId():null, body.productId, body.quantity); return ResponseEntity.ok().build(); }
  @DeleteMapping("/clear") public ResponseEntity<?> clear(HttpServletRequest req){ HttpSession s=SessionUtil.session(req); Long userId=currentUserId(s); if(userId!=null) service.clearForUser(userId); else service.getForSession(s.getId()).forEach(ci->service.setQty(null,s.getId(),ci.getProductId(),0)); return ResponseEntity.ok().build(); }
}
