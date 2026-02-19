package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.mediaDtos.MediaDto;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
@TestTransaction
public class MediaServiceTest {

    @BeforeEach
    public void setUp() {

    }

    /** @see MediaServiceImpl#getAllMedia() **/

    @Test
    public void GetAllMedia_Success(){

    }

    @Test
    public void GetAllMedia_Failure_NotManagedException(){

    }



    /** @see MediaServiceImpl#getMediaById(UUID)  **/

    @Test
    public void GetMediaById_Success(){

    }

    @Test
    public void GetMediaById_Failure_NotManagedException(){

    }

    @Test
    public void GetMediaById_Failure_UnknownResource(){

    }

    @Test
    public void GetMediaById_Failure_ConstraintViolation(){

    }


    /** @see MediaServiceImpl#createMedia(MediaDto)  **/

    @Test
    public void CreateMedia_Success(){

    }

    @Test
    public void CreateMedia_Failure_ConstraintViolation(){

    }

    @Test
    public void CreateMedia_Failure_NotManagedException(){

    }

    @Test
    public void CreateMedia_Failure_DuplicateResource(){

    }

    /** @see MediaServiceImpl#updateMedia(MediaDto) **/

    @Test
    public void UpdateMedia_Success(){

    }

    @Test
    public void UpdateMedia_Failure_ConstraintViolation(){

    }

    @Test
    public void UpdateMedia_Failure_UnknownResource(){

    }

    @Test
    public void UpdateMedia_Failure_NotManagedException(){

    }

    /** @see MediaServiceImpl#deleteMedia(UUID) **/

    @Test
    public void DeleteMedia_Success(){

    }

    @Test
    public void DeleteMedia_Failure_ConstraintViolation(){

    }

    @Test
    public void DeleteMedia_Failure_UnknownResource(){

    }

    @Test
    public void DeleteMedia_Failure_NotManagedException(){

    }


}
