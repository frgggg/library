package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.ControllerUtil;
import com.cft.focusstart.library.dto.WriterDto;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.ReaderRepository;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.util.TestStringFieldGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.cft.focusstart.library.model.Writer.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongFirstName, answer, resultMatcherStatus);

        inWriterDtoWrongFirstName.setFirstName(TestStringFieldGenerator.getToBig(WRITER_FIRST_NAME_LEN_MAX));
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongFirstName, answer, resultMatcherStatus);

        inWriterDtoWrongFirstName.setFirstName(TestStringFieldGenerator.getToLittle(WRITER_FIRST_NAME_LEN_MIN));
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongFirstName, answer, resultMatcherStatus);
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
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongSurname, answer, resultMatcherStatus);

        inWriterDtoWrongSurname.setSurname(TestStringFieldGenerator.getToBig(WRITER_SURNAME_LEN_MAX));
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongSurname, answer, resultMatcherStatus);

        inWriterDtoWrongSurname.setSurname(TestStringFieldGenerator.getToLittle(WRITER_SURNAME_LEN_MIN));
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongSurname, answer, resultMatcherStatus);
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
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongMiddleName, answer, resultMatcherStatus);
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
        ControllerUtil.testUtilPost(mockMvc, url, inWriterDtoWrongComment, answer, resultMatcherStatus);
    }
}
