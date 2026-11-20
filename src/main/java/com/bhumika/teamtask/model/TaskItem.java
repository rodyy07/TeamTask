package com.bhumika.teamtask.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity @Table(name="tasks") @Getter @Setter @NoArgsConstructor
public class TaskItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String title;
 @Column(length=5000) private String description;
 private LocalDate dueDate;
 @Enumerated(EnumType.STRING) private TaskStatus status=TaskStatus.OPEN;
 @Enumerated(EnumType.STRING) private TaskPriority priority=TaskPriority.MEDIUM;
 @ManyToOne(optional=false) private Project project;
 @ManyToOne(optional=false) private AppUser createdBy;
 @ManyToOne private AppUser assignee;
 private LocalDateTime createdAt=LocalDateTime.now();
 private LocalDateTime updatedAt=LocalDateTime.now();
}
