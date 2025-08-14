package com.spelete.shop.controller;
import com.spelete.shop.entity.Product; import com.spelete.shop.service.ProductService; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/products")
public class ProductController {
  private final ProductService service; public ProductController(ProductService service){ this.service = service; }
  @GetMapping public List<Product> all(@RequestParam(required=false) String category){ return (category==null||category.isBlank()) ? service.all() : service.byCategory(category); }
}
