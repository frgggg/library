package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.AllControllerUtil;
import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.repository.ReaderRepository;
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

import java.util.Collections;

import static com.cft.focusstart.library.model.Book.*;
import static com.cft.focusstart.library.model.Writer.*;
import static com.cft.focusstart.library.model.Writer.WRITER_COMMENT_LEN_MAX;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository mockBookRepository;

    @MockBean
    private ReaderRepository mockReaderRepository;

    private static final String BOOK_REST_TEST_BASE_URL = "/book";

    private static String bookDtoToString(BookDto bookDto) {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"writers\":%s}"
                , bookDto.getId()
                , bookDto.getName()
                , bookDto.getWriters()
        );
    }

    // CREATE
    @Test
    public void createBookWrongNameTest() throws Exception {
        BookDto inBookDtoWrongName = new BookDto();
        String answer = BOOK_NAME_VALIDATION_MESSAGE;
        String url = BOOK_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        inBookDtoWrongName.setName(TestStringFieldGenerator.getWrongNull());
        AllControllerUtil.testUtilPost(mockMvc, url, inBookDtoWrongName, answer, resultMatcherStatus);

        inBookDtoWrongName.setName(TestStringFieldGenerator.getToBig(BOOK_NAME_LEN_MAX));
        AllControllerUtil.testUtilPost(mockMvc, url, inBookDtoWrongName, answer, resultMatcherStatus);

        inBookDtoWrongName.setName(TestStringFieldGenerator.getToLittle(BOOK_NAME_LEN_MIN));
        AllControllerUtil.testUtilPost(mockMvc, url, inBookDtoWrongName, answer, resultMatcherStatus);
    }

    @Test
    public void createReaderSuccessTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isCreated();

        Writer writerOfBook = new Writer(
                TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX)
        );
        Long writerOfBookId = 101l;
        writerOfBook.setId(writerOfBookId);

        BookDto inBookDto = new BookDto();
        inBookDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        inBookDto.setWriters(Collections.singletonList(writerOfBookId));

        BookDto outBookDto = new BookDto();
        Long outBookDtoId = 1l;
        outBookDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        inBookDto.setWriters(Collections.singletonList(writerOfBookId));
        outBookDto.setId(outBookDtoId);


    }
}
