package com.bhumika.teamtask.controller;
import com.bhumika.teamtask.model.*;
import com.bhumika.teamtask.repository.*;
import com.bhumika.teamtask.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/tasks/{taskId}/comments") @RequiredArgsConstructor
public class CommentController {
 private final CommentRepository comments;private final TaskRepository tasks;private final CurrentUserService current;private final AccessService access;
 public record CommentRequest(@NotBlank String body){}
 @PostMapping public ResponseEntity<?> add(@PathVariable Long taskId,@Valid @RequestBody CommentRequest r){
  TaskItem t=tasks.findById(taskId).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());
  TaskComment c=new TaskComment();c.setTask(t);c.setAuthor(current.get());c.setBody(r.body());comments.save(c);
  return ResponseEntity.status(201).body(Map.of("id",c.getId(),"body",c.getBody(),"author",c.getAuthor().getFullName(),"createdAt",c.getCreatedAt().toString()));
 }
 @GetMapping public List<Map<String,Object>> list(@PathVariable Long taskId){
  TaskItem t=tasks.findById(taskId).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());
  return comments.findByTaskIdOrderByCreatedAtAsc(taskId).stream().map(c->Map.<String,Object>of("id",c.getId(),"body",c.getBody(),"author",c.getAuthor().getFullName(),"createdAt",c.getCreatedAt().toString())).toList();
 }
}
