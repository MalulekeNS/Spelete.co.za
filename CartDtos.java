package com.spelete.shop.dto;
import jakarta.validation.constraints.*;
public class CartDtos {
  public static class AddItemRequest { @NotNull public Long productId; @Min(1) public int quantity = 1; }
  public static class UpdateQtyRequest { @NotNull public Long productId; @Min(0) public int quantity; }
}
