package com.bhumika.teamtask.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="team_members", uniqueConstraints=@UniqueConstraint(columnNames={"team_id","user_id"}))
@Getter @Setter @NoArgsConstructor
public class TeamMember {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Team team;
 @ManyToOne(optional=false) private AppUser user;
 @Column(nullable=false) private String role="MEMBER";
 private LocalDateTime joinedAt=LocalDateTime.now();
}
