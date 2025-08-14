package com.spelete.shop.dto;
import jakarta.validation.constraints.*;
public class AuthDtos {
  public static class RegisterRequest { @NotBlank public String name; @Email @NotBlank public String email; @NotBlank public String password; }
  public static class LoginRequest { @Email @NotBlank public String email; @NotBlank public String password; }
  public static class UserResponse { public Long id; public String name; public String email;
    public UserResponse(Long id,String name,String email){this.id=id;this.name=name;this.email=email;} }
}
