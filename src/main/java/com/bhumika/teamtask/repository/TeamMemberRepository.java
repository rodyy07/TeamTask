package com.bhumika.teamtask.repository;
import com.bhumika.teamtask.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> { boolean existsByTeamIdAndUserId(Long teamId, Long userId); List<TeamMember> findByTeamId(Long teamId); }
