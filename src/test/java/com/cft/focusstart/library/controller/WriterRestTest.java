package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.AllControllerUtil;
import com.cft.focusstart.library.dto.WriterDto;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.impl.WriterServiceImpl;
import com.cft.focusstart.library.util.TestStringFieldGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import javax.persistence.PersistenceException;

import java.util.Collections;
import java.util.Optional;

import static com.cft.focusstart.library.controller.util.WriterRestTestUtil.*;
import static com.cft.focusstart.library.exception.ServiceException.*;
import static org.mockito.Mockito.*;

import static com.cft.focusstart.library.model.Writer.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class WriterRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WriterRepository mockWriterRepository;

    private static final String WRITER_REST_TEST_BASE_URL = "/writer";

    private static String writerDtoToString(WriterDto writerDto) {
        return String.format(
                "{\"id\":%d,\"firstName\":\"%s\",\"surname\":\"%s\",\"middleName\":\"%s\",\"comment\":\"%s\"}"
                , writerDto.getId()
                , writerDto.getFirstName()
                , writerDto.getSurname()
                , writerDto.getMiddleName()
                , writerDto.getComment()
        );
    }

    // CREATE
    @Test
    public void createWriterWrongFirstNameTest() throws Exception {
        WriterDto inWriterDtoWrongFirstName = new WriterDto();
        String answer = WRITER_FIRST_NAME_VALIDATION_MESSAGE;
        String url = WRITER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        inWriterDtoWrongFirstName.setSurname(TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX));
        inWriterDtoWrongFirstName.setMiddleName(TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX));
        inWriterDtoWrongFirstName.setComment(TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX));

        inWriterDtoWrongFirstName.setFirstName(TestStringFieldGenerator.getWrongNull());
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongFirstName, answer, resultMatcherStatus);

        inWriterDtoWrongFirstName.setFirstName(TestStringFieldGenerator.getToBig(WRITER_FIRST_NAME_LEN_MAX));
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongFirstName, answer, resultMatcherStatus);

        inWriterDtoWrongFirstName.setFirstName(TestStringFieldGenerator.getToLittle(WRITER_FIRST_NAME_LEN_MIN));
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongFirstName, answer, resultMatcherStatus);
    }

    @Test
    public void createWriterWrongSurnameTest() throws Exception {
        WriterDto inWriterDtoWrongSurname = new WriterDto();
        String answer = WRITER_SURNAME_VALIDATION_MESSAGE;
        String url = WRITER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        inWriterDtoWrongSurname.setFirstName(TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX));
        inWriterDtoWrongSurname.setMiddleName(TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX));
        inWriterDtoWrongSurname.setComment(TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX));

        inWriterDtoWrongSurname.setSurname(TestStringFieldGenerator.getWrongNull());
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongSurname, answer, resultMatcherStatus);

        inWriterDtoWrongSurname.setSurname(TestStringFieldGenerator.getToBig(WRITER_SURNAME_LEN_MAX));
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongSurname, answer, resultMatcherStatus);

        inWriterDtoWrongSurname.setSurname(TestStringFieldGenerator.getToLittle(WRITER_SURNAME_LEN_MIN));
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongSurname, answer, resultMatcherStatus);
    }

    @Test
    public void createWriterWrongMiddleNameTest() throws Exception {
        WriterDto inWriterDtoWrongMiddleName = new WriterDto();
        String answer = WRITER_MIDDLE_NAME_VALIDATION_MESSAGE;
        String url = WRITER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        inWriterDtoWrongMiddleName.setFirstName(TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX));
        inWriterDtoWrongMiddleName.setSurname(TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX));
        inWriterDtoWrongMiddleName.setComment(TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX));

        inWriterDtoWrongMiddleName.setMiddleName(TestStringFieldGenerator.getToBig(WRITER_MIDDLE_NAME_LEN_MAX));
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongMiddleName, answer, resultMatcherStatus);
    }

    @Test
    public void createWriterWrongCommentTest() throws Exception {
        WriterDto inWriterDtoWrongComment = new WriterDto();
        String answer = WRITER_COMMENT_VALIDATION_MESSAGE;
        String url = WRITER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        inWriterDtoWrongComment.setFirstName(TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX));
        inWriterDtoWrongComment.setSurname(TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX));
        inWriterDtoWrongComment.setMiddleName(TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX));

        inWriterDtoWrongComment.setComment(TestStringFieldGenerator.getToBig(WRITER_COMMENT_LEN_MAX));
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongComment, answer, resultMatcherStatus);
    }

    @Test
    public void createWriterSuccessTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isCreated();
        String answer = writerDtoToString(outWriterDto);

        when(mockWriterRepository.save(inWriter)).thenReturn(outWriter);
        AllControllerUtil.testUtilPost(mockMvc, url, inWriterDto, answer, resultMatcherStatus);
        verify(mockWriterRepository).save(inWriter);
    }

    @Test
    public void createWriterExistTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL;
        String answer = SERVICE_EXCEPTION_EXIST_ENTITY_FORMAT_STRING;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        Object inObject = inWriterDto;

        when(mockWriterRepository.save(inWriter)).thenThrow(PersistenceException.class);
        AllControllerUtil.testUtilPost(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockWriterRepository).save(inWriter);
    }

    // FIND BY ID
    @Test
    public void getWriterNotExistTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + notExistWriterId;
        String answer = serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId).getMessage();
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        when(mockWriterRepository.findById(notExistWriterId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(notExistWriterId);
    }

    @Test
    public void getWriterSuccessTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + existWriterId;
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = writerDtoToString(outWriterDto);

        when(mockWriterRepository.findById(existWriterId)).thenReturn(Optional.of(outWriter));
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(existWriterId);
    }

    // FIND ALL
    @Test
    public void getAllWritersSuccessTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = writerDtoToString(outWriterDto);

        when(mockWriterRepository.findAll()).thenReturn(Collections.singletonList(outWriter));
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockWriterRepository).findAll();
    }

    // DELETE BY ID
    @Test
    public void deleteWriterNotExistTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + notExistWriterId;
        String answer = serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId).getMessage();
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        when(mockWriterRepository.findById(notExistWriterId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(notExistWriterId);
    }

    @Test
    public void deleteWriterSuccessTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + existWriterId;
        String answer = "";
        ResultMatcher resultMatcherStatus = status().isOk();

        when(mockWriterRepository.findById(existWriterId)).thenReturn(Optional.of(outWriter));
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(existWriterId);
    }

    @Test
    public void deleteWriterRelatedTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + existWriterWithBookId;
        String answer = serviceExceptionDeleteOrUpdateRelatedEntity(WriterServiceImpl.SERVICE_NAME, WRITER_BOOKS_FIELD_NAME).getMessage();
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        when(mockWriterRepository.findById(existWriterWithBookId)).thenReturn(Optional.of(outWriterWithBook));
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(existWriterWithBookId);
    }

    // UPDATE BY ID
    @Test
    public void updateWriterExistTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + existWriterId;;
        String answer = SERVICE_EXCEPTION_EXIST_ENTITY_FORMAT_STRING;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        Object inObject = inWriterDto;

        when(mockWriterRepository.findById(existWriterId)).thenReturn(Optional.of(inWriter));
        when(mockWriterRepository.save(inWriter)).thenThrow(PersistenceException.class);
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(existWriterId);
        verify(mockWriterRepository).save(inWriter);
    }

    @Test
    public void updateWriterNotExistTest() throws Exception {
        String url = WRITER_REST_TEST_BASE_URL + "/" + notExistWriterId;
        String answer = serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId).getMessage();
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        Object inObject = inWriterDto;

        when(mockWriterRepository.findById(notExistWriterId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockWriterRepository).findById(notExistWriterId);
    }
}
