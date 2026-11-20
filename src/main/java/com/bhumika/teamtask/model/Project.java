package com.bhumika.teamtask.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="projects") @Getter @Setter @NoArgsConstructor
public class Project {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 private String description;
 @ManyToOne(optional=false) private Team team;
 @ManyToOne(optional=false) private AppUser createdBy;
 private LocalDateTime createdAt=LocalDateTime.now();
}
