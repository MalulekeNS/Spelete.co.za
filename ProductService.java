package com.spelete.shop.service;
import com.spelete.shop.entity.Product; import com.spelete.shop.repo.ProductRepository;
import org.springframework.stereotype.Service; import java.util.List;
@Service public class ProductService {
  private final ProductRepository repo; public ProductService(ProductRepository repo){ this.repo = repo; }
  public List<Product> all(){ return repo.findAll(); }
  public List<Product> byCategory(String category){ return repo.findByCategoryIgnoreCase(category); }
}
