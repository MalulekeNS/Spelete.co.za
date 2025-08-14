package com.spelete.shop.entity;
import jakarta.persistence.*;
@Entity @Table(name="products")
public class Product {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private String sku; private String name; private String category; private Double price;
  @Column(name="image_url") private String imageUrl;
  public Product(){} public Product(String s,String n,String c,Double p,String i){sku=s;name=n;category=c;price=p;imageUrl=i;}
  public Long getId(){return id;} public void setId(Long id){this.id=id;}
  public String getSku(){return sku;} public void setSku(String sku){this.sku=sku;}
  public String getName(){return name;} public void setName(String name){this.name=name;}
  public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
  public Double getPrice(){return price;} public void setPrice(Double price){this.price=price;}
  public String getImageUrl(){return imageUrl;} public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
}
