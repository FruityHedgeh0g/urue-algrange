package fr.fruityhedgeh0g.model.entities.medias;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "PHOTO")
public class PhotoEntity extends MediaEntity{
}
