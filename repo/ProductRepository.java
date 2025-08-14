package com.spelete.shop.repo;
import com.spelete.shop.entity.Product; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryIgnoreCase(String category);
}
