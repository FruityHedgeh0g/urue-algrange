package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.GroupDtos.GroupDto;
import fr.fruityhedgeh0g.exceptions.DuplicateResourceException;
import fr.fruityhedgeh0g.exceptions.UnknownResourceException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vavr.control.Try;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

@QuarkusTest
public class GroupServiceTest {

    @Inject
    GroupService groupService;

    @BeforeAll
    public static void setup(){

    }

    @Test
    @TestTransaction
    public void createGroupTest_Success() {
        GroupDto testDto = new GroupDto(null,"Test group", "Test group description",null,null);

        Try<GroupDto> result = groupService.createGroup(testDto);
        Assertions.assertTrue(result.isSuccess());

        GroupDto groupDto = result.get();

        Assertions.assertNotNull(groupDto.getGroupId());
        Assertions.assertEquals(testDto.getName(), groupDto.getName());
        Assertions.assertEquals(testDto.getDescription(), groupDto.getDescription());
    }

    @Test
    @TestTransaction
    public void createGroupTest_Failure_DuplicateName(){
        GroupDto testDto = new GroupDto(null,"Test group", "Test group description",null,null);
        groupService.createGroup(testDto);
        Try<GroupDto> result = groupService.createGroup(testDto);
        Assertions.assertTrue(result.isFailure());
        Assertions.assertThrowsExactly(DuplicateResourceException.class, result::get);
    }

    @Test
    @TestTransaction
    public void createGroupTest_Failure_Null(){
        GroupDto testDto = new GroupDto(null,null, null,null,null);
        Try<GroupDto> result = groupService.createGroup(testDto);
        Assertions.assertTrue(result.isFailure());
        Assertions.assertThrowsExactly(NullPointerException.class, result::get);
    }

    @Test
    @TestTransaction
    public void getGroupByIdTest_Success() throws Exception {
        GroupDto testDto = new GroupDto(null,"Test group", "Test group description",null,null);
        GroupDto groupDto = groupService.createGroup(testDto).getOrElseThrow(ex -> new Exception("Error creating group"));

        Try<GroupDto> result = groupService.getGroupById(groupDto.getGroupId());
        Assertions.assertTrue(result.isSuccess());

        GroupDto retrievedDto = result.get();
        Assertions.assertEquals(groupDto.getGroupId(), retrievedDto.getGroupId());
        Assertions.assertEquals(groupDto.getName(), retrievedDto.getName());
        Assertions.assertEquals(groupDto.getDescription(), retrievedDto.getDescription());
    }

    @Test
    @TestTransaction
    public void getGroupByIdTest_Failure_NotFound(){
        Try<GroupDto> result = groupService.getGroupById(UUID.randomUUID());
        Assertions.assertTrue(result.isFailure());
        Assertions.assertThrowsExactly(UnknownResourceException.class, result::get);
    }

    @Test
    @TestTransaction
    public void getGroupByIdTest_Failure_Null(){
        Try<GroupDto> result = groupService.getGroupById(null);
        Assertions.assertTrue(result.isFailure());
        Assertions.assertThrowsExactly(ConstraintViolationException.class, result::get);
    }


}
