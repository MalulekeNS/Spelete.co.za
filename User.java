package com.spelete.shop.entity;
import jakarta.persistence.*; import jakarta.validation.constraints.*;
@Entity @Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class User {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @NotBlank private String name; @Email @NotBlank private String email; @NotBlank private String passwordHash;
  public User(){} public User(String n,String e,String p){name=n;email=e;passwordHash=p;}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
  public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String p){this.passwordHash=p;}
}
