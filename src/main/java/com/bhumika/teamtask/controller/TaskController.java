package com.bhumika.teamtask.controller;
import com.bhumika.teamtask.model.*;
import com.bhumika.teamtask.repository.*;
import com.bhumika.teamtask.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class TaskController {
 private final TaskRepository tasks;private final ProjectRepository projects;private final UserRepository users;private final CurrentUserService current;private final AccessService access;
 public record TaskRequest(@NotBlank String title,String description,LocalDate dueDate,TaskPriority priority,Long assigneeId){}
 public record StatusRequest(TaskStatus status){}
 private Map<String,Object> view(TaskItem t){return Map.of("id",t.getId(),"title",t.getTitle(),"description",t.getDescription()==null?"":t.getDescription(),"dueDate",t.getDueDate()==null?"":t.getDueDate().toString(),"status",t.getStatus().name(),"priority",t.getPriority().name(),"projectId",t.getProject().getId(),"assigneeId",t.getAssignee()==null?0L:t.getAssignee().getId(),"createdBy",t.getCreatedBy().getId(),"createdAt",t.getCreatedAt().toString());}
 @PostMapping("/projects/{projectId}/tasks") public ResponseEntity<?> create(@PathVariable Long projectId,@Valid @RequestBody TaskRequest r){
  Project p=projects.findById(projectId).orElseThrow(()->new IllegalArgumentException("Project not found"));access.projectMember(p,current.get().getId());
  TaskItem t=new TaskItem();t.setProject(p);t.setCreatedBy(current.get());t.setTitle(r.title());t.setDescription(r.description());t.setDueDate(r.dueDate());if(r.priority()!=null)t.setPriority(r.priority());
  if(r.assigneeId()!=null){AppUser a=users.findById(r.assigneeId()).orElseThrow(()->new IllegalArgumentException("Assignee not found"));access.teamMember(p.getTeam().getId(),a.getId());t.setAssignee(a);}
  tasks.save(t);return ResponseEntity.status(201).body(view(t));
 }
 @GetMapping("/tasks") public List<Map<String,Object>> mine(@RequestParam(required=false) TaskStatus status,@RequestParam(required=false) Long projectId,@RequestParam(required=false) String q,@RequestParam(defaultValue="dueDate") String sort){
  Long uid=current.get().getId();return tasks.findAll().stream().filter(t->{try{access.taskMember(t,uid);return true;}catch(Exception e){return false;}})
   .filter(t->t.getAssignee()!=null&&t.getAssignee().getId().equals(uid)||t.getCreatedBy().getId().equals(uid)||t.getAssignee()!=null)
   .filter(t->status==null||t.getStatus()==status).filter(t->projectId==null||t.getProject().getId().equals(projectId))
   .filter(t->q==null||t.getTitle().toLowerCase().contains(q.toLowerCase())||(t.getDescription()!=null&&t.getDescription().toLowerCase().contains(q.toLowerCase())))
   .sorted(sort.equalsIgnoreCase("createdAt")?Comparator.comparing(TaskItem::getCreatedAt).reversed():Comparator.comparing(TaskItem::getDueDate,Comparator.nullsLast(Comparator.naturalOrder())))
   .map(this::view).toList();
 }
 @GetMapping("/tasks/{id}") public Map<String,Object> get(@PathVariable Long id){TaskItem t=tasks.findById(id).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());return view(t);}
 @PutMapping("/tasks/{id}") public Map<String,Object> update(@PathVariable Long id,@Valid @RequestBody TaskRequest r){
  TaskItem t=tasks.findById(id).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());
  t.setTitle(r.title());t.setDescription(r.description());t.setDueDate(r.dueDate());if(r.priority()!=null)t.setPriority(r.priority());
  if(r.assigneeId()!=null){AppUser a=users.findById(r.assigneeId()).orElseThrow(()->new IllegalArgumentException("Assignee not found"));access.teamMember(t.getProject().getTeam().getId(),a.getId());t.setAssignee(a);}
  t.setUpdatedAt(LocalDateTime.now());tasks.save(t);return view(t);
 }
 @PatchMapping("/tasks/{id}/status") public Map<String,Object> status(@PathVariable Long id,@Valid @RequestBody StatusRequest r){
  TaskItem t=tasks.findById(id).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());t.setStatus(r.status());t.setUpdatedAt(LocalDateTime.now());tasks.save(t);return view(t);
 }
 @DeleteMapping("/tasks/{id}") public ResponseEntity<?> delete(@PathVariable Long id){
  TaskItem t=tasks.findById(id).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());tasks.delete(t);return ResponseEntity.noContent().build();
 }
}
