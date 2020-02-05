package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.AllControllerUtil;
import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.repository.ReaderRepository;

import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.impl.BookServiceImpl;
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

import static com.cft.focusstart.library.controller.util.ReaderRestTestUtil.*;
import static com.cft.focusstart.library.controller.util.ReaderRestTestUtil.notExistReaderId;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionDeleteOrUpdateRelatedEntity;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionNoEntityWithId;
import static com.cft.focusstart.library.model.Reader.READER_BOOKS_FIELD_NAME;
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

    @MockBean
    private ReaderRepository mockReaderRepository;

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

    // FIND ALL
    @Test
    public void getAllBooksSuccessTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = bookDtoToString(outBookDto);

        when(mockBookRepository.findAll()).thenReturn(Collections.singletonList(outBook));
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockBookRepository).findAll();
    }

    // FIND BY ID
    @Test
    public void getBookSuccessTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + existBookId;
        ResultMatcher resultMatcherStatus = status().isOk();

        String answer = bookDtoToString(outBookDto);

        when(mockBookRepository.findById(existBookId)).thenReturn(Optional.of(outBook));
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(existBookId);
    }

    @Test
    public void getBookNotExistTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + notExistBookId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        when(mockBookRepository.findById(notExistBookId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(notExistBookId);
    }

    // DELETE BY ID
    @Test
    public void deleteBookNotExistTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + notExistBookId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        when(mockBookRepository.findById(notExistBookId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(notExistBookId);
    }

    @Test
    public void deleteBookSuccessTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + existBookId;
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = "";

        when(mockBookRepository.findById(existBookId)).thenReturn(Optional.of(outBook));
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(existBookId);
    }

    @Test
    public void deleteBookBusyTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + existBookWithReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionDeleteOrUpdateRelatedEntity(ReaderServiceImpl.SERVICE_NAME, BOOK_READER_FIELD_NAME).getMessage();

        when(mockBookRepository.findById(existReaderWithBooksId)).thenReturn(Optional.of(outBookWithReader));
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(existReaderWithBooksId);
    }

    // UPDATE BY ID
    @Test
    public void updateBookSuccessTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + existBookId;
        ResultMatcher resultMatcherStatus = status().isOk();

        Object inObject = inBookDto;
        String answer = bookDtoToString(outBookDto);

        when(mockBookRepository.findById(existBookId)).thenReturn(Optional.of(outBook));
        when(mockWriterRepository.findById(writerOfBookId)).thenReturn(Optional.of(writerOfBook));
        when(mockBookRepository.save(outBook)).thenReturn(outBook);
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(existBookId);
        verify(mockWriterRepository).findById(writerOfBookId);
        verify(mockBookRepository).save(outBook);
    }

    @Test
    public void updateBookNotExistTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + notExistBookId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        Object inObject = inBookDto;

        when(mockBookRepository.findById(notExistBookId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(notExistBookId);
    }

    // UNSET READER
    @Test
    public void setBookFreeNotExistTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + notExistBookId + "/unset-reader";
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        Object inObject = inBookDto;

        when(mockBookRepository.findById(notExistBookId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(notExistBookId);
    }

    @Test
    public void setBookFreeSuccessTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + existBookId + "/unset-reader";
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = "";

        Object inObject = inBookDto;

        when(mockBookRepository.findById(existBookId)).thenReturn(Optional.of(outBook));
        when(mockBookRepository.save(outBook)).thenReturn(outBook);
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(existBookId);
        verify(mockBookRepository).save(outBook);
    }

    // UNSET READER
    @Test
    public void setReaderForBookNotExistBookTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + notExistBookId + "/set-reader?reader=" + readerOfBookId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        Object inObject = inBookDto;

        when(mockBookRepository.findById(notExistBookId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(notExistBookId);
    }

    @Test
    public void setReaderForBookNotExistReaderTest() throws Exception {
        String url = BOOK_REST_TEST_BASE_URL + "/" + existBookId + "/set-reader?reader=" + readerOfBookId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistBookReaderId).getMessage();

        Object inObject = inBookDto;

        when(mockBookRepository.findById(existBookId)).thenReturn(Optional.of(outBook));
        when(mockReaderRepository.findById(notExistBookReaderId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockBookRepository).findById(existBookId);
        verify(mockReaderRepository).findById(notExistBookReaderId);
    }
}
