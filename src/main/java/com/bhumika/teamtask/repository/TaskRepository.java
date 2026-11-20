package com.bhumika.teamtask.repository;
import com.bhumika.teamtask.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TaskRepository extends JpaRepository<TaskItem,Long> { List<TaskItem> findByAssigneeId(Long userId); }
