package com.spelete.shop.dto;
import jakarta.validation.constraints.*;
public class ContactDto { @NotBlank public String name; @Email @NotBlank public String email; @NotBlank public String subject; @NotBlank public String message; }
