package fr.fruityhedgeh0g.model.entities.medias;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "VIDEO")
@Getter
@Setter
public class VideoEntity extends MediaEntity {
}
