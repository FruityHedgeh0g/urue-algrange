package fr.fruityhedgeh0g.model.entities.medias;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@DiscriminatorValue(value = "PHOTO")
@Getter
@Setter
public class PhotoEntity extends MediaEntity{
}
