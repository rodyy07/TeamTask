package com.bhumika.teamtask.service;
import com.bhumika.teamtask.model.AppUser;
import com.bhumika.teamtask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class CurrentUserService {
 private final UserRepository users;
 public AppUser get(){
  String email=SecurityContextHolder.getContext().getAuthentication().getName();
  return users.findByEmail(email).orElseThrow(()->new IllegalStateException("Authenticated user not found"));
 }
}
