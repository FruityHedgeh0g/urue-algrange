package fr.fruityhedgeh0g.services.interfaces;

import fr.fruityhedgeh0g.dtos.groupDtos.GroupDto;
import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import fr.fruityhedgeh0g.entities.GroupEntity;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import io.vavr.control.Try;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GroupService extends PublicGroupService, InternalGroupService {

}
