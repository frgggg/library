package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.AllControllerUtil;
import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.repository.ReaderRepository;

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

import java.util.Collections;
import java.util.Optional;

import static com.cft.focusstart.library.controller.util.ReaderRestTestUtil.*;
import static com.cft.focusstart.library.controller.util.ReaderRestTestUtil.notExistReaderId;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionDeleteOrUpdateRelatedEntity;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionNoEntityWithId;
import static com.cft.focusstart.library.model.Reader.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReaderRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderRepository mockReaderRepository;

    private static final String READER_REST_TEST_BASE_URL = "/reader";

    private static String readerDtoToString(ReaderDto readerDto) {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"isDebtor\":%s,\"books\":%s}"
                , readerDto.getId()
                , readerDto.getName()
                , readerDto.getIsDebtor()
                , readerDto.getBooks()
        );
    }

    // CREATE
    @Test
    public void createReaderWrongNameTest() throws Exception {
        ReaderDto inReaderDtoWrongName = new ReaderDto();
        String answer = READER_NAME_VALIDATION_MESSAGE;
        String url = READER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        inReaderDtoWrongName.setName(TestStringFieldGenerator.getWrongNull());
        AllControllerUtil.testUtilPost(mockMvc, url, inReaderDtoWrongName, answer, resultMatcherStatus);

        inReaderDtoWrongName.setName(TestStringFieldGenerator.getToBig(READER_NAME_LEN_MAX));
        AllControllerUtil.testUtilPost(mockMvc, url, inReaderDtoWrongName, answer, resultMatcherStatus);

        inReaderDtoWrongName.setName(TestStringFieldGenerator.getToLittle(READER_NAME_LEN_MIN));
        AllControllerUtil.testUtilPost(mockMvc, url, inReaderDtoWrongName, answer, resultMatcherStatus);
    }

    @Test
    public void createReaderSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isCreated();

        Object inObject = inReaderDto;
        String answer = readerDtoToString(outReaderDto);

        when(mockReaderRepository.save(inReader)).thenReturn(outReader);
        AllControllerUtil.testUtilPost(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockReaderRepository).save(inReader);
    }

    // FIND BY ID
    @Test
    public void getReaderSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + existReaderId;
        ResultMatcher resultMatcherStatus = status().isOk();

        String answer = readerDtoToString(outReaderDto);

        when(mockReaderRepository.findById(existReaderId)).thenReturn(Optional.of(outReader));
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(existReaderId);
    }

    @Test
    public void getReaderNotExistTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    // FIND ALL
    @Test
    public void getAllReadersSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isOk();

        String answer = readerDtoToString(outReaderDto);

        when(mockReaderRepository.findAll()).thenReturn(Collections.singletonList(outReader));
        AllControllerUtil.testUtilGet(mockMvc, url, answer, resultMatcherStatus);
        verify(mockReaderRepository).findAll();
    }

    // DELETE BY ID
    @Test
    public void deleteReaderNotExistTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    @Test
    public void deleteReaderSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + existReaderId;
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = "";

        when(mockReaderRepository.findById(existReaderId)).thenReturn(Optional.of(outReader));
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(existReaderId);
    }

    @Test
    public void deleteReaderDebtorTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + existReaderWithBooksId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionDeleteOrUpdateRelatedEntity(ReaderServiceImpl.SERVICE_NAME, READER_BOOKS_FIELD_NAME).getMessage();

        when(mockReaderRepository.findById(existReaderWithBooksId)).thenReturn(Optional.of(outReaderWithBooks));
        AllControllerUtil.testUtilDelete(mockMvc, url, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(existReaderWithBooksId);
    }

    // UPDATE BY ID
    @Test
    public void updateReaderSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + existReaderId;
        ResultMatcher resultMatcherStatus = status().isOk();

        Object inObject = inReaderDto;
        String answer = readerDtoToString(outReaderDto);

        when(mockReaderRepository.findById(existReaderId)).thenReturn(Optional.of(outReader));
        when(mockReaderRepository.save(outReader)).thenReturn(outReader);
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(existReaderId);
        verify(mockReaderRepository).save(outReader);
    }

    @Test
    public void updateReaderNotExistTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        Object inObject = inReader;

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    // REFRESH DEBTOR STATUS BY ID
    @Test
    public void refreshDebtorStatusNotExistTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId + "/refresh-debtor-status";
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();
        Object inObject = null;

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    @Test
    public void refreshDebtorStatusSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL + "/" + existReaderId + "/refresh-debtor-status";
        ResultMatcher resultMatcherStatus = status().isOk();
        String answer = "";
        Object inObject = null;

        when(mockReaderRepository.findById(existReaderId)).thenReturn(Optional.of(outReader));
        when(mockReaderRepository.save(outReader)).thenReturn(outReader);
        AllControllerUtil.testUtilPut(mockMvc, url, inObject, answer, resultMatcherStatus);
        verify(mockReaderRepository).findById(existReaderId);
        verify(mockReaderRepository).save(outReader);
    }

}