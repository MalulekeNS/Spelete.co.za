package com.spelete.shop.service;
import com.spelete.shop.entity.User; import com.spelete.shop.repo.UserRepository;
import org.springframework.stereotype.Service; import org.springframework.security.crypto.bcrypt.BCrypt;
import java.util.Optional;
@Service public class UserService {
  private final UserRepository repo; public UserService(UserRepository repo){ this.repo = repo; }
  public Optional<User> findByEmail(String email){ return repo.findByEmail(email); }
  public User register(String name,String email,String rawPassword){
    if(repo.existsByEmail(email)) throw new IllegalStateException("Email already registered");
    String hash = BCrypt.hashpw(rawPassword, BCrypt.gensalt()); return repo.save(new User(name,email,hash));
  }
  public User authenticate(String email,String rawPassword){
    User u = repo.findByEmail(email).orElseThrow(() -> new IllegalStateException("Account not found"));
    if(!BCrypt.checkpw(rawPassword, u.getPasswordHash())) throw new IllegalStateException("Invalid credentials"); return u;
  }
}
