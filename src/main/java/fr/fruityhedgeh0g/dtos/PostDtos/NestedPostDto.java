package fr.fruityhedgeh0g.dtos.PostDtos;

import fr.fruityhedgeh0g.entities.medias.MediaEntity;

import java.util.UUID;

public record NestedPostDto (UUID postId, String title) {
}
