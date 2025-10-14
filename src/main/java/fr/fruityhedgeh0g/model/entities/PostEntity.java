package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postId;
}
