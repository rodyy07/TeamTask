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
@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class ProjectController {
 private final ProjectRepository projects;private final TeamRepository teams;private final CurrentUserService current;private final AccessService access;
 public record ProjectRequest(@NotBlank String name,String description){}
 @PostMapping("/teams/{teamId}/projects") public ResponseEntity<?> create(@PathVariable Long teamId,@Valid @RequestBody ProjectRequest r){
  Team t=access.teamMember(teamId,current.get().getId());Project p=new Project();p.setTeam(t);p.setCreatedBy(current.get());p.setName(r.name());p.setDescription(r.description());projects.save(p);
  return ResponseEntity.status(201).body(Map.of("id",p.getId(),"name",p.getName(),"teamId",teamId));
 }
 @GetMapping("/teams/{teamId}/projects") public List<Map<String,Object>> list(@PathVariable Long teamId){
  access.teamMember(teamId,current.get().getId());return projects.findByTeamId(teamId).stream().map(p->Map.<String,Object>of("id",p.getId(),"name",p.getName(),"description",p.getDescription()==null?"":p.getDescription(),"teamId",teamId)).toList();
 }
}
