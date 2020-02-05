package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.AllControllerUtil;
import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.repository.ReaderRepository;

import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.impl.ReaderServiceImpl;
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
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import static com.cft.focusstart.library.controller.util.BookRestTestUtil.*;
import static com.cft.focusstart.library.model.Book.*;
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
    private WriterRepository mockWriterRepository;

    private static final String BOOK_REST_TEST_BASE_URL = "/book";

    private static String bookDtoToString(BookDto bookDto) {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"reader\":%s,\"backTime\":%s,\"writers\":%s}"
                , bookDto.getId()
                , bookDto.getName()
                , bookDto.getReader()
                , bookDto.getBackTime()
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

        Object inObject = inBookDto;
        String answer = bookDtoToString(outBookDto);

        when(mockBookRepository.save(inBook)).thenReturn(outBook);
        when(mockWriterRepository.findById(writerOfBookId)).thenReturn(Optional.of(writerOfBook));
        AllControllerUtil.testUtilPost(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).save(inBook);
        verify(mockWriterRepository).findById(writerOfBookId);

    }
}
