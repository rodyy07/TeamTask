package com.bhumika.teamtask.controller;
import com.bhumika.teamtask.model.AppUser;
import com.bhumika.teamtask.repository.UserRepository;
import com.bhumika.teamtask.security.JwtService;
import com.bhumika.teamtask.service.CurrentUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final AuthenticationManager authManager; private final JwtService jwt; private final CurrentUserService current;
 public record RegisterRequest(@NotBlank String fullName,@Email @NotBlank String email,@Size(min=8) String password){}
 public record LoginRequest(@Email @NotBlank String email,@NotBlank String password){}
 @PostMapping("/register") public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest r){
  if(users.findByEmail(r.email()).isPresent()) return ResponseEntity.status(409).body(Map.of("error","Email already registered"));
  AppUser u=new AppUser();u.setFullName(r.fullName());u.setEmail(r.email().toLowerCase());u.setPassword(encoder.encode(r.password()));users.save(u);
  return ResponseEntity.status(201).body(Map.of("id",u.getId(),"email",u.getEmail(),"fullName",u.getFullName()));
 }
 @PostMapping("/login") public Map<String,Object> login(@Valid @RequestBody LoginRequest r){
  authManager.authenticate(new UsernamePasswordAuthenticationToken(r.email().toLowerCase(),r.password()));
  return Map.of("token",jwt.generate(r.email().toLowerCase()),"tokenType","Bearer");
 }
 @GetMapping("/me") public Map<String,Object> me(){AppUser u=current.get();return Map.of("id",u.getId(),"email",u.getEmail(),"fullName",u.getFullName(),"bio",u.getBio()==null?"":u.getBio());}
 @PutMapping("/profile") public Map<String,Object> update(@RequestBody Map<String,String> body){
  AppUser u=current.get(); if(body.containsKey("fullName"))u.setFullName(body.get("fullName")); if(body.containsKey("bio"))u.setBio(body.get("bio"));users.save(u);
  return Map.of("id",u.getId(),"email",u.getEmail(),"fullName",u.getFullName(),"bio",u.getBio()==null?"":u.getBio());
 }
 @PostMapping("/logout") public Map<String,String> logout(){return Map.of("message","Logged out. Discard the bearer token on the client.");}
}
