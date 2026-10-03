package fr.fruityhedgeh0g.repositories;

import fr.fruityhedgeh0g.entities.FeatureRequestEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FeatureRequestRepository implements PanacheRepositoryBase<FeatureRequestEntity, UUID> {

    public List<FeatureRequestEntity> listNewestFirst() {
        return listAll(Sort.descending("createdAt"));
    }
}
