package com.bhumika.teamtask.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="task_comments") @Getter @Setter @NoArgsConstructor
public class TaskComment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private TaskItem task;
 @ManyToOne(optional=false) private AppUser author;
 @Column(nullable=false,length=3000) private String body;
 private LocalDateTime createdAt=LocalDateTime.now();
}
