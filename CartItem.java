package com.spelete.shop.entity;
import jakarta.persistence.*;
@Entity @Table(name="cart_items", indexes=@Index(name="idx_cart_user", columnList="userId"))
public class CartItem {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private Long userId; private String sessionId; private Long productId; private int quantity;
  public CartItem(){} public CartItem(Long u,String s,Long p,int q){userId=u;sessionId=s;productId=p;quantity=q;}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public Long getUserId(){return userId;} public void setUserId(Long userId){this.userId=userId;}
  public String getSessionId(){return sessionId;} public void setSessionId(String sessionId){this.sessionId=sessionId;}
  public Long getProductId(){return productId;} public void setProductId(Long productId){this.productId=productId;}
  public int getQuantity(){return quantity;} public void setQuantity(int quantity){this.quantity=quantity;}
}
