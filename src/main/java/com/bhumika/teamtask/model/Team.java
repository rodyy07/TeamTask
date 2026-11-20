package com.bhumika.teamtask.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="teams") @Getter @Setter @NoArgsConstructor
public class Team {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 private String description;
 @ManyToOne(optional=false) private AppUser owner;
 private LocalDateTime createdAt=LocalDateTime.now();
}
