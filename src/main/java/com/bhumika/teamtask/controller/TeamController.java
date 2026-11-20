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
@RestController @RequestMapping("/api/teams") @RequiredArgsConstructor
public class TeamController {
 private final TeamRepository teams; private final TeamMemberRepository members; private final UserRepository users;
 private final CurrentUserService current; private final AccessService access;
 public record TeamRequest(@NotBlank String name,String description){}
 public record MemberRequest(@jakarta.validation.constraints.Email String email){}
 @PostMapping public ResponseEntity<?> create(@Valid @RequestBody TeamRequest r){
  Team t=new Team();t.setName(r.name());t.setDescription(r.description());t.setOwner(current.get());teams.save(t);
  TeamMember m=new TeamMember();m.setTeam(t);m.setUser(current.get());m.setRole("OWNER");members.save(m);
  return ResponseEntity.status(201).body(Map.of("id",t.getId(),"name",t.getName(),"description",t.getDescription()==null?"":t.getDescription()));
 }
 @GetMapping public List<Map<String,Object>> mine(){
  AppUser u=current.get(); List<Team> all=new ArrayList<>(teams.findAll().stream().filter(t->t.getOwner().getId().equals(u.getId())).toList());
  members.findAll().stream().filter(m->m.getUser().getId().equals(u.getId())).map(TeamMember::getTeam).forEach(t->{if(all.stream().noneMatch(x->x.getId().equals(t.getId())))all.add(t);});
  return all.stream().map(t->Map.<String,Object>of("id",t.getId(),"name",t.getName(),"description",t.getDescription()==null?"":t.getDescription(),"ownerId",t.getOwner().getId())).toList();
 }
 @PostMapping("/{teamId}/members") public ResponseEntity<?> addMember(@PathVariable Long teamId,@Valid @RequestBody MemberRequest r){
  AppUser me=current.get();Team team=teams.findById(teamId).orElseThrow(()->new IllegalArgumentException("Team not found"));
  if(!team.getOwner().getId().equals(me.getId()))throw new SecurityException("Only the team owner can add members");
  AppUser user=users.findByEmail(r.email().toLowerCase()).orElseThrow(()->new IllegalArgumentException("No registered user with that email"));
  if(members.existsByTeamIdAndUserId(teamId,user.getId()))return ResponseEntity.status(409).body(Map.of("error","Already a member"));
  TeamMember m=new TeamMember();m.setTeam(team);m.setUser(user);members.save(m);
  return ResponseEntity.status(201).body(Map.of("message","Member added","userId",user.getId(),"email",user.getEmail()));
 }
 @GetMapping("/{teamId}/members") public List<Map<String,Object>> listMembers(@PathVariable Long teamId){
  access.teamMember(teamId,current.get().getId());
  return members.findByTeamId(teamId).stream().map(m->Map.<String,Object>of("userId",m.getUser().getId(),"email",m.getUser().getEmail(),"fullName",m.getUser().getFullName(),"role",m.getRole())).toList();
 }
}
