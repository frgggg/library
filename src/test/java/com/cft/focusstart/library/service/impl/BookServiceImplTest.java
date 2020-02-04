package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.repository.ReaderRepository;
import com.cft.focusstart.library.repository.WriterRepository;
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

import static com.cft.focusstart.library.model.Book.*;
import static com.cft.focusstart.library.model.Writer.WRITER_FIRST_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.WRITER_SURNAME_LEN_MAX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookServiceImplTest {

    @Autowired
    BookServiceImpl bookService;
    @Autowired
    WriterServiceImpl writerService;
    @Autowired
    ReaderServiceImpl readerService;

    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    WriterRepository writerRepository;

    @Before
    public void init() {
        readerRepository.deleteAll();
        bookRepository.deleteAll();
        writerRepository.deleteAll();
    }

    @Rule
    public ExpectedException testException = ExpectedException.none();

    @Test
    public void createNullNameTest() {
        Writer existWriter = writerService.create(
                TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                TestStringFieldGenerator.getRightNull(),
                TestStringFieldGenerator.getRightNull());

        String name = TestStringFieldGenerator.getWrongNull();
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createToLittleNameTest() {
        Writer existWriter = writerService.create(
                TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                TestStringFieldGenerator.getRightNull(),
                TestStringFieldGenerator.getRightNull());

        String name = TestStringFieldGenerator.getToLittle(BOOK_NAME_LEN_MIN);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigNameTest() {
        Writer existWriter = writerService.create(
                TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                TestStringFieldGenerator.getRightNull(),
                TestStringFieldGenerator.getRightNull());

        String name = TestStringFieldGenerator.getToBig(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createSuccessTest() {
        Writer existWriter = writerService.create(
                TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                TestStringFieldGenerator.getRightNull(),
                TestStringFieldGenerator.getRightNull());

        String name = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        Book savedBook = bookService.create(name, writersIds);
        List<Writer> savedBookWriters = savedBook.getWriters();

        assertNotNull(savedBook.getId());
        assertEquals(name, savedBook.getName());
        assertEquals(writersIds.size(), savedBookWriters.size());
        assertEquals(existWriter.getId(), savedBookWriters.get(0).getId());
    }
}