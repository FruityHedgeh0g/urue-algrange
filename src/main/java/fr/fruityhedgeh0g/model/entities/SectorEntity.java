package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "sectors")
@NoArgsConstructor
@AllArgsConstructor
public class SectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sectorId;
}
