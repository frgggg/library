package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.controller.util.ControllerUtil;
import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.model.Reader;
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

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.cft.focusstart.library.model.Reader.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReaderRestTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderRepository mockReaderRepository;

    // CREATE
    @Test
    public void createWrongNameTest() throws Exception {
        ReaderDto readerDtoWrongName = new ReaderDto();
        String answer = READER_NAME_VALIDATION_MESSAGE;
        String url = "/reader";
        ResultMatcher resultMatcher = status().isBadRequest();

        readerDtoWrongName.setName(TestStringFieldGenerator.getWrongNull());
        ControllerUtil.testUtilPost(mockMvc, url, readerDtoWrongName, answer, resultMatcher);

        readerDtoWrongName.setName(TestStringFieldGenerator.getToBig(READER_NAME_LEN_MAX));
        ControllerUtil.testUtilPost(mockMvc, url, readerDtoWrongName, answer, resultMatcher);

        readerDtoWrongName.setName(TestStringFieldGenerator.getToLittle(READER_NAME_LEN_MIN));
        ControllerUtil.testUtilPost(mockMvc, url, readerDtoWrongName, answer, resultMatcher);
    }

    @Test
    public void createSuccessTest() throws Exception {
        ReaderDto inReaderDto = new ReaderDto();
        inReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        ReaderDto outReaderDto = new ReaderDto();
        Long outReaderDtoId = 1l;
        outReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReaderDto.setId(outReaderDtoId);

        Reader readerForSave = new Reader(inReaderDto.getName());
        Reader savedReader = new Reader(inReaderDto.getName());
        savedReader.setId(outReaderDtoId);
        when(mockReaderRepository.save(readerForSave))
                .thenReturn(savedReader);

        String url = "/reader";
        ResultMatcher resultMatcher = status().isCreated();
        ControllerUtil.testUtilPost(mockMvc, url, inReaderDto, outReaderDtoId.toString(), resultMatcher);
    }

    // FIND BY ID
    @Test
    public void getSuccessTest() throws Exception {
        Long idForFind = 1l;

        Reader findReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        findReader.setId(idForFind);
        when(mockReaderRepository.findById(idForFind))
                .thenReturn(Optional.of(findReader));

        ReaderDto findReaderDto = new ReaderDto();
        findReaderDto.setName(findReader.getName());
        findReaderDto.setId(findReader.getId());
        findReaderDto.setIsDebtor(false);

        String url = "/reader/" + idForFind;
        ResultMatcher resultMatcher = status().isOk();
        ControllerUtil.testUtilGet(mockMvc, url, findReaderDto.toString(), resultMatcher);
    }
}