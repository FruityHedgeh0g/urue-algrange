package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID groupId;
}
