package fr.fruityhedgeh0g.services;

import fr.fruityhedgeh0g.dtos.postDtos.PostDto;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
@TestTransaction
public class PostServiceTest {

    @BeforeEach
    public void setUp() {

    }

    /** @see PostServiceImpl#getAllPosts() **/

    @Test
    public void GetAllPosts_Success(){

    }

    @Test
    public void GetAllPosts_Failure_NotManagedException(){

    }


    /** @see PostServiceImpl#getPostById(UUID)  **/
    @Test
    public void GetPostById_Success(){

    }

    @Test
    public void GetPostById_Failure_NotManagedException(){

    }

    @Test
    public void GetPostById_Failure_UnknownResource(){

    }

    @Test
    public void GetPostById_Failure_ConstraintViolation(){

    }


    /** @see PostServiceImpl#createPost(PostDto)  **/

    @Test
    public void CreatePost_Success(){

    }

    @Test
    public void CreatePost_Failure_ConstraintViolation(){

    }

    @Test
    public void CreatePost_Failure_NotManagedException(){

    }



    /** @see PostServiceImpl#updatePost(PostDto) **/

    @Test
    public void UpdatePost_Success(){

    }

    @Test
    public void UpdatePost_Failure_ConstraintViolation(){

    }

    @Test
    public void UpdatePost_Failure_UnknownResource(){

    }

    @Test
    public void UpdatePost_Failure_NotManagedException(){

    }


    /** @see PostServiceImpl#deletePost(UUID) **/

    @Test
    public void DeletePost_Success(){

    }

    @Test
    public void DeletePost_Failure_ConstraintViolation(){

    }

    @Test
    public void DeletePost_Failure_UnknownResource(){

    }

    @Test
    public void DeletePost_Failure_NotManagedException(){

    }

    /** @see PostServiceImpl#addPostBanner(UUID, UUID) **/

    /** @see PostServiceImpl#deletePostBanner(UUID) **/

    /** @see PostServiceImpl#updatePostBanner(UUID, UUID) **/

    /** @see PostServiceImpl#addPostAttachment(UUID, UUID) **/

    /** @see PostServiceImpl#deletePostAttachment(UUID, UUID) **/
   

}
