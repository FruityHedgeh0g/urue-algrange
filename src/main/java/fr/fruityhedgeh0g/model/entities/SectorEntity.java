package fr.fruityhedgeh0g.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "sectors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sectorId;
}
