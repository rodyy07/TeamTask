package com.bhumika.teamtask.controller;
import com.bhumika.teamtask.model.*;
import com.bhumika.teamtask.repository.*;
import com.bhumika.teamtask.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.*;
@RestController @RequestMapping("/api/tasks/{taskId}/attachments") @RequiredArgsConstructor
public class AttachmentController {
 private final AttachmentRepository attachments;private final TaskRepository tasks;private final CurrentUserService current;private final AccessService access;
 @Value("${app.upload-dir:uploads}") private String uploadDir;
 @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public ResponseEntity<?> upload(@PathVariable Long taskId,@RequestParam("file") MultipartFile file) throws Exception {
  TaskItem task=tasks.findById(taskId).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(task,current.get().getId());
  if(file.isEmpty())throw new IllegalArgumentException("Empty file");
  if(file.getSize()>10*1024*1024)throw new IllegalArgumentException("Maximum attachment size is 10 MB");
  String original=StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
  String stored=UUID.randomUUID()+"_"+original.replaceAll("[^a-zA-Z0-9._-]","_");
  Path dir=Paths.get(uploadDir).toAbsolutePath().normalize();Files.createDirectories(dir);Files.copy(file.getInputStream(),dir.resolve(stored),StandardCopyOption.REPLACE_EXISTING);
  TaskAttachment a=new TaskAttachment();a.setTask(task);a.setUploadedBy(current.get());a.setOriginalName(original);a.setStoredName(stored);a.setContentType(file.getContentType());a.setSize(file.getSize());attachments.save(a);
  return ResponseEntity.status(201).body(Map.of("id",a.getId(),"fileName",original,"size",a.getSize()));
 }
 @GetMapping public List<Map<String,Object>> list(@PathVariable Long taskId){
  TaskItem t=tasks.findById(taskId).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());
  return attachments.findByTaskId(taskId).stream().map(a->Map.<String,Object>of("id",a.getId(),"fileName",a.getOriginalName(),"size",a.getSize(),"uploadedBy",a.getUploadedBy().getFullName())).toList();
 }
 @GetMapping("/{attachmentId}") public ResponseEntity<Resource> download(@PathVariable Long taskId,@PathVariable Long attachmentId) throws Exception {
  TaskItem t=tasks.findById(taskId).orElseThrow(()->new IllegalArgumentException("Task not found"));access.taskMember(t,current.get().getId());
  TaskAttachment a=attachments.findById(attachmentId).filter(x->x.getTask().getId().equals(taskId)).orElseThrow(()->new IllegalArgumentException("Attachment not found"));
  Path path=Paths.get(uploadDir).toAbsolutePath().normalize().resolve(a.getStoredName());Resource resource=new UrlResource(path.toUri());
  if(!resource.exists())throw new IllegalArgumentException("File is missing from storage");
  return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+a.getOriginalName().replace("\"","")+"\"").body(resource);
 }
}
