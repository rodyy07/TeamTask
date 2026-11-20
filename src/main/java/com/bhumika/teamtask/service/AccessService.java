package com.bhumika.teamtask.service;
import com.bhumika.teamtask.model.*;
import com.bhumika.teamtask.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class AccessService {
 private final TeamMemberRepository members; private final TeamRepository teams;
 public Team teamMember(Long teamId, Long userId){
  Team t=teams.findById(teamId).orElseThrow(()->new IllegalArgumentException("Team not found"));
  if(!t.getOwner().getId().equals(userId) && !members.existsByTeamIdAndUserId(teamId,userId)) throw new SecurityException("You are not a member of this team");
  return t;
 }
 public void projectMember(Project p,Long userId){teamMember(p.getTeam().getId(),userId);}
 public void taskMember(TaskItem t,Long userId){projectMember(t.getProject(),userId);}
}
