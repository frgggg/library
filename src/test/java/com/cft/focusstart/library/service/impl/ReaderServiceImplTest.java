package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.ReaderRepository;
import com.cft.focusstart.library.util.TestStringFieldGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Book.BOOK_NAME_VALIDATION_MESSAGE;
import static com.cft.focusstart.library.model.Reader.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ReaderServiceImplTest {

    @Autowired
    private ReaderServiceImpl readerService;

    @Autowired
    private ReaderRepository readerRepository;

    @Before
    public void init() {
        readerRepository.deleteAll();
    }

    @Rule
    public ExpectedException testException = ExpectedException.none();

    // CREATE
    @Test
    public void createSuccessTest() {
        String name = TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX);
        Reader savedReader = readerService.create(name);

        assertNotNull(savedReader.getId());
        assertEquals(name, savedReader.getName());
    }

    @Test
    public void createNullNameTest() {
        String name = TestStringFieldGenerator.getWrongNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(READER_NAME_VALIDATION_MESSAGE);
        readerService.create(name);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigNameTest() {
        String name = TestStringFieldGenerator.getToBig(READER_NAME_LEN_MAX);

        testException.expect(ServiceException.class);
        testException.expectMessage(READER_NAME_VALIDATION_MESSAGE);
        readerService.create(name);
        testException = ExpectedException.none();
    }

    @Test
    public void createToLittleNameTest() {
        String name = TestStringFieldGenerator.getToLittle(READER_NAME_LEN_MIN);

        testException.expect(ServiceException.class);
        testException.expectMessage(READER_NAME_VALIDATION_MESSAGE);
        readerService.create(name);
        testException = ExpectedException.none();
    }

    // DELETE BY ID
    @Test
    public void deleteSuccessTest() {
        String name = TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX);
        Reader savedReader = readerService.create(name);

        assertEquals(1, readerService.findAll().size());
        readerService.deleteById(savedReader.getId());
        assertEquals(0, readerService.findAll().size());

    }
}