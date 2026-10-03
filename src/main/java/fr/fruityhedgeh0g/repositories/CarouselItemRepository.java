package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.CarouselItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CarouselItemRepository implements PanacheRepositoryBase<CarouselItemEntity, UUID> {

    public List<CarouselItemEntity> listInOrder() {
        return listAll(Sort.ascending("position"));
    }
}
