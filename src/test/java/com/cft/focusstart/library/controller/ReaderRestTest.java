package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.ControllerUtil;
import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
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

import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionDeleteOrUpdateRelatedEntity;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionNoEntityWithId;
import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
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
        ControllerUtil.testUtilPost(mockMvc, url, inReaderDtoWrongName, answer, resultMatcherStatus);

        inReaderDtoWrongName.setName(TestStringFieldGenerator.getToBig(READER_NAME_LEN_MAX));
        ControllerUtil.testUtilPost(mockMvc, url, inReaderDtoWrongName, answer, resultMatcherStatus);

        inReaderDtoWrongName.setName(TestStringFieldGenerator.getToLittle(READER_NAME_LEN_MIN));
        ControllerUtil.testUtilPost(mockMvc, url, inReaderDtoWrongName, answer, resultMatcherStatus);
    }

    @Test
    public void createReaderSuccessTest() throws Exception {
        String url = READER_REST_TEST_BASE_URL;
        ResultMatcher resultMatcherStatus = status().isCreated();

        ReaderDto inReaderDto = new ReaderDto();
        inReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        ReaderDto outReaderDto = new ReaderDto();
        Long outReaderDtoId = 1l;
        outReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReaderDto.setId(outReaderDtoId);
        outReaderDto.setIsDebtor(READER_IS_NOT_DEBTOR_STATUS);

        Reader inReader = new Reader(inReaderDto.getName());
        Reader outReader = new Reader(outReaderDto.getName());
        outReader.setId(outReaderDto.getId());

        when(mockReaderRepository.save(inReader)).thenReturn(outReader);
        ControllerUtil.testUtilPost(mockMvc, url, inReaderDto, readerDtoToString(outReaderDto), resultMatcherStatus);
        verify(mockReaderRepository).save(inReader);
    }

    // FIND BY ID
    @Test
    public void getReaderSuccessTest() throws Exception {
        Long inReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + inReaderId;
        ResultMatcher resultMatcherStatus = status().isOk();

        Reader outReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReader.setId(inReaderId);

        ReaderDto outReaderDto = new ReaderDto();
        outReaderDto.setName(outReader.getName());
        outReaderDto.setId(outReader.getId());
        outReaderDto.setIsDebtor(READER_IS_NOT_DEBTOR_STATUS);

        when(mockReaderRepository.findById(inReaderId)).thenReturn(Optional.of(outReader));
        ControllerUtil.testUtilGet(mockMvc, url, readerDtoToString(outReaderDto), resultMatcherStatus);
        verify(mockReaderRepository).findById(inReaderId);
    }

    @Test
    public void getReaderNotExistTest() throws Exception {
        Long notExistReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String notExistReaderExceptionMessage = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        ControllerUtil.testUtilGet(mockMvc, url, notExistReaderExceptionMessage, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    // FIND ALL
    @Test
    public void getAllReadersSuccessTest() throws Exception {
        Long inReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL ;
        ResultMatcher resultMatcherStatus = status().isOk();

        Reader outReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReader.setId(inReaderId);

        ReaderDto outReaderDto = new ReaderDto();
        outReaderDto.setName(outReader.getName());
        outReaderDto.setId(outReader.getId());
        outReaderDto.setIsDebtor(READER_IS_NOT_DEBTOR_STATUS);

        when(mockReaderRepository.findAll()).thenReturn(Collections.singletonList(outReader));
        ControllerUtil.testUtilGet(mockMvc, url, readerDtoToString(outReaderDto), resultMatcherStatus);
        verify(mockReaderRepository).findAll();
    }

    // DELETE BY ID
    @Test
    public void deleteReaderNotExistTest() throws Exception {
        Long notExistReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String notExistReaderExceptionMessage = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        ControllerUtil.testUtilDelete(mockMvc, url, notExistReaderExceptionMessage, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    @Test
    public void deleteReaderSuccessTest() throws Exception {
        Long inReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + inReaderId;
        ResultMatcher resultMatcherStatus = status().isOk();

        Reader outReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReader.setId(inReaderId);

        when(mockReaderRepository.findById(inReaderId)).thenReturn(Optional.of(outReader));
        ControllerUtil.testUtilDelete(mockMvc, url, "", resultMatcherStatus);
        verify(mockReaderRepository).findById(inReaderId);
    }

    @Test
    public void deleteReaderDebtorTest() throws Exception {
        Long inReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + inReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();

        Reader outReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReader.setId(inReaderId);
        outReader.setBooks(Collections.singletonList(new Book(null, null)));

        String debtorReaderExceptionMessage = serviceExceptionDeleteOrUpdateRelatedEntity(ReaderServiceImpl.SERVICE_NAME, READER_BOOKS_FIELD_NAME).getMessage();

        when(mockReaderRepository.findById(inReaderId)).thenReturn(Optional.of(outReader));
        ControllerUtil.testUtilDelete(mockMvc, url, debtorReaderExceptionMessage, resultMatcherStatus);
        verify(mockReaderRepository).findById(inReaderId);
    }

    // UPDATE BY ID
    @Test
    public void updateReaderSuccessTest() throws Exception {
        Long updatedReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + updatedReaderId;
        ResultMatcher resultMatcherStatus = status().isOk();

        ReaderDto inReaderDto = new ReaderDto();
        inReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        ReaderDto outReaderDto = new ReaderDto();
        outReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReaderDto.setId(updatedReaderId);
        outReaderDto.setIsDebtor(READER_IS_NOT_DEBTOR_STATUS);

        Reader outReader = new Reader(outReaderDto.getName());
        outReader.setId(outReaderDto.getId());

        when(mockReaderRepository.findById(updatedReaderId)).thenReturn(Optional.of(outReader));
        when(mockReaderRepository.save(outReader)).thenReturn(outReader);
        ControllerUtil.testUtilPut(mockMvc, url, inReaderDto, readerDtoToString(outReaderDto), resultMatcherStatus);
        verify(mockReaderRepository).findById(updatedReaderId);
        verify(mockReaderRepository).save(outReader);
    }

    @Test
    public void updateReaderNotExistTest() throws Exception {
        Long notExistReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String notExistReaderExceptionMessage = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        Reader inReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        ControllerUtil.testUtilPut(mockMvc, url, inReader, notExistReaderExceptionMessage, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    // REFRESH DEBTOR STATUS BY ID
    @Test
    public void refreshDebtorStatusNotExistTest() throws Exception {
        Long notExistReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + notExistReaderId + "/refresh-debtor-status";
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String notExistReaderExceptionMessage = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        when(mockReaderRepository.findById(notExistReaderId)).thenReturn(Optional.empty());
        ControllerUtil.testUtilPut(mockMvc, url, "", notExistReaderExceptionMessage, resultMatcherStatus);
        verify(mockReaderRepository).findById(notExistReaderId);
    }

    @Test
    public void refreshDebtorStatusSuccessTest() throws Exception {
        Long refreshDebtorStatusReaderId = 1l;
        String url = READER_REST_TEST_BASE_URL + "/" + refreshDebtorStatusReaderId + "/refresh-debtor-status";
        ResultMatcher resultMatcherStatus = status().isOk();

        Reader outReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReader.setId(refreshDebtorStatusReaderId);

        when(mockReaderRepository.findById(refreshDebtorStatusReaderId)).thenReturn(Optional.of(outReader));
        when(mockReaderRepository.save(outReader)).thenReturn(outReader);
        ControllerUtil.testUtilPut(mockMvc, url, "", "", resultMatcherStatus);
        verify(mockReaderRepository).findById(refreshDebtorStatusReaderId);
        verify(mockReaderRepository).save(outReader);
    }

}