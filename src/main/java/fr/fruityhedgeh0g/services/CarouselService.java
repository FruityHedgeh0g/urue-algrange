package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.CarouselItemDto;
import fr.fruityhedgeh0g.entities.CarouselItemEntity;
import fr.fruityhedgeh0g.exceptions.InvalidResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import fr.fruityhedgeh0g.repositories.CarouselItemRepository;
import fr.fruityhedgeh0g.repositories.MediaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

/** The home page's carousel: everyone sees its active slides; the Bureau writes, orders and puts slides aside. */
@ApplicationScoped
public class CarouselService {

    @Inject CarouselItemRepository itemRepository;
    @Inject MediaRepository mediaRepository;

    public List<CarouselItemDto> list(boolean withInactive) {
        return itemRepository.listInOrder().stream()
                .filter(item -> withInactive || item.isActive())
                .map(CarouselItemDto::of)
                .toList();
    }

    @Transactional
    public CarouselItemDto create(CarouselItemDto.Input input) {
        CarouselItemEntity item = new CarouselItemEntity();
        item.setPosition(itemRepository.listInOrder().stream().mapToInt(CarouselItemEntity::getPosition).max().orElse(0) + 1);
        apply(item, input);
        itemRepository.persist(item);
        return CarouselItemDto.of(item);
    }

    @Transactional
    public CarouselItemDto update(UUID itemId, CarouselItemDto.Input input) {
        CarouselItemEntity item = itemOrThrow(itemId);
        apply(item, input);
        return CarouselItemDto.of(item);
    }

    @Transactional
    public void delete(UUID itemId) {
        itemRepository.delete(itemOrThrow(itemId));
    }

    /** Swaps the slide with the one before ({@code up}) or after it; nothing at either end. */
    @Transactional
    public List<CarouselItemDto> move(UUID itemId, boolean up) {
        List<CarouselItemEntity> items = itemRepository.listInOrder();
        int index = items.indexOf(itemOrThrow(itemId));
        int other = up ? index - 1 : index + 1;
        if (other >= 0 && other < items.size()) {
            int position = items.get(index).getPosition();
            items.get(index).setPosition(items.get(other).getPosition());
            items.get(other).setPosition(position);
        }
        return list(true);
    }

    private CarouselItemEntity itemOrThrow(UUID itemId) {
        return itemRepository.findByIdOptional(itemId)
                .orElseThrow(() -> new UnknownResourceException("Carousel slide not found: " + itemId));
    }

    private void apply(CarouselItemEntity item, CarouselItemDto.Input input) {
        if (input.title() == null || input.title().isBlank())
            throw new InvalidResourceException("A slide has a title.");
        String linkTo = input.linkTo() == null || input.linkTo().isBlank() ? null : input.linkTo().trim();
        if (linkTo != null && (!linkTo.startsWith("/") || linkTo.startsWith("//")))
            throw new InvalidResourceException("A slide links to a page of the site: " + linkTo);
        item.setTitle(input.title().trim());
        item.setCaption(input.caption());
        item.setLinkTo(linkTo);
        item.setActive(input.active() == null || input.active());
        item.setMedia(input.mediaId() == null ? null : mediaRepository.findByIdOptional(input.mediaId())
                .orElseThrow(() -> new InvalidResourceException("Unknown media: " + input.mediaId())));
    }
}
