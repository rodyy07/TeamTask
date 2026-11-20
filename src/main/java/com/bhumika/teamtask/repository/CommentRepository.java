package com.bhumika.teamtask.repository;
import com.bhumika.teamtask.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface CommentRepository extends JpaRepository<TaskComment,Long> { List<TaskComment> findByTaskIdOrderByCreatedAtAsc(Long taskId); }
