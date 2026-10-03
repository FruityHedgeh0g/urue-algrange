package fr.fruityhedgeh0g.dtos;

import fr.fruityhedgeh0g.entities.CarouselItemEntity;

import java.util.UUID;

/** A carousel slide; {@code order} is its place, from 1. */
public record CarouselItemDto(UUID id, String title, String caption, UUID mediaId, String linkTo, boolean active, int order) {

    public static CarouselItemDto of(CarouselItemEntity item) {
        return new CarouselItemDto(item.getItemId(), item.getTitle(), item.getCaption(),
                item.getMedia() == null ? null : item.getMedia().getMediaId(), item.getLinkTo(), item.isActive(), item.getPosition());
    }

    /** What the Bureau writes about a slide. */
    public record Input(String title, String caption, UUID mediaId, String linkTo, Boolean active) {}
}
