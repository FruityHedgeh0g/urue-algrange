package fr.fruityhedgeh0g.dtos.eventDtos;

/** An Event's roster as a spreadsheet file, with the name to download it under. */
public record RosterExportDto(String fileName, byte[] content) {
}
