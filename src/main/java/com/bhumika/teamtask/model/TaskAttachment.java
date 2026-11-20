package com.bhumika.teamtask.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="task_attachments") @Getter @Setter @NoArgsConstructor
public class TaskAttachment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private TaskItem task;
 @ManyToOne(optional=false) private AppUser uploadedBy;
 @Column(nullable=false) private String originalName;
 @Column(nullable=false) private String storedName;
 private String contentType;
 private long size;
 private LocalDateTime uploadedAt=LocalDateTime.now();
}
