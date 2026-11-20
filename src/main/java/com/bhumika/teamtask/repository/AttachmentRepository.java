package com.bhumika.teamtask.repository;
import com.bhumika.teamtask.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AttachmentRepository extends JpaRepository<TaskAttachment,Long> { List<TaskAttachment> findByTaskId(Long taskId); }
