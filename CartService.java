package com.spelete.shop.service;
import com.spelete.shop.entity.CartItem; import com.spelete.shop.repo.CartItemRepository;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.List;
@Service public class CartService {
  private final CartItemRepository repo; public CartService(CartItemRepository repo){ this.repo = repo; }
  public List<CartItem> getForUser(Long userId){ return repo.findByUserId(userId); }
  public List<CartItem> getForSession(String sessionId){ return repo.findBySessionId(sessionId); }
  @Transactional public void add(Long userId,String sessionId,Long productId,int qty){
    if(userId!=null){ var item=repo.findByUserIdAndProductId(userId,productId).orElse(null);
      if(item==null) repo.save(new CartItem(userId,null,productId,qty)); else { item.setQuantity(item.getQuantity()+qty); repo.save(item);} }
    else { var item=repo.findBySessionIdAndProductId(sessionId,productId).orElse(null);
      if(item==null) repo.save(new CartItem(null,sessionId,productId,qty)); else { item.setQuantity(item.getQuantity()+qty); repo.save(item);} } }
  @Transactional public void setQty(Long userId,String sessionId,Long productId,int qty){
    if(userId!=null){ var item=repo.findByUserIdAndProductId(userId,productId).orElse(null); if(item!=null){ if(qty<=0) repo.delete(item); else { item.setQuantity(qty); repo.save(item);} } }
    else { var item=repo.findBySessionIdAndProductId(sessionId,productId).orElse(null); if(item!=null){ if(qty<=0) repo.delete(item); else { item.setQuantity(qty); repo.save(item);} } } }
  @Transactional public void clearForUser(Long userId){ repo.deleteByUserId(userId); }
  @Transactional public void mergeSessionIntoUser(String sessionId,Long userId){
    var sessionItems=repo.findBySessionId(sessionId);
    for(var s:sessionItems){ var existing=repo.findByUserIdAndProductId(userId,s.getProductId()).orElse(null);
      if(existing==null) repo.save(new CartItem(userId,null,s.getProductId(),s.getQuantity()));
      else { existing.setQuantity(existing.getQuantity()+s.getQuantity()); repo.save(existing);} }
    repo.deleteBySessionId(sessionId);
  }
}
