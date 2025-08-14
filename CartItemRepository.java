package com.spelete.shop.repo;
import com.spelete.shop.entity.CartItem; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByUserId(Long userId); List<CartItem> findBySessionId(String sessionId);
  Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
  Optional<CartItem> findBySessionIdAndProductId(String sessionId, Long productId);
  void deleteByUserId(Long userId); void deleteBySessionId(String sessionId);
}
