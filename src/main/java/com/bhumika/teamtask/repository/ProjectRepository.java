package com.bhumika.teamtask.repository;
import com.bhumika.teamtask.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ProjectRepository extends JpaRepository<Project,Long> { List<Project> findByTeamId(Long teamId); }
