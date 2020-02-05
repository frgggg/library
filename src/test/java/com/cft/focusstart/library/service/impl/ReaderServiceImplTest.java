package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
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

import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionDeleteOrUpdateRelatedEntity;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionNoEntityWithId;
import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Book.BOOK_NAME_VALIDATION_MESSAGE;
import static com.cft.focusstart.library.model.Reader.*;
import static com.cft.focusstart.library.model.Writer.WRITER_FIRST_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.WRITER_SURNAME_LEN_MAX;
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

    @Autowired
    private WriterRepository writerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Before
    public void init() {
        readerRepository.deleteAll();
        bookRepository.deleteAll();
        writerRepository.deleteAll();
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
    public void deleteByIdSuccessTest() {
        String name = TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX);
        Reader savedReader = readerService.create(name);

        assertEquals(1, readerService.findAll().size());
        readerService.deleteById(savedReader.getId());
        assertEquals(0, readerService.findAll().size());

    }

    @Test
    public void deleteByIdNotExistTest() {
        Long notExistReaderId = 333l;
        String notExistReaderExceptionMessage = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistReaderExceptionMessage);
        readerService.deleteById(notExistReaderId);
        testException = ExpectedException.none();

    }

    @Test
    public void deleteByIdDebtorTest() {
        Writer writer = writerRepository.save(
                new Writer(
                        TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                        TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                        TestStringFieldGenerator.getRightNull(),
                        TestStringFieldGenerator.getRightNull()
                )
        );

        Reader reader = readerService.create(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        Book bookForSave = new Book(
                TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX),
                Collections.singletonList(writer)
        );
        bookForSave.setReader(reader);

        Book book = bookRepository.save(bookForSave);

        String debtorReaderExceptionMessage = serviceExceptionDeleteOrUpdateRelatedEntity(ReaderServiceImpl.SERVICE_NAME, READER_BOOKS_FIELD_NAME).getMessage();
        testException.expect(ServiceException.class);
        testException.expectMessage(debtorReaderExceptionMessage);
        readerService.deleteById(reader.getId());
        testException = ExpectedException.none();
    }

    // FIND ALL
    @Test
    public void findAllTest() {
        Reader reader = readerService.create(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        List<Reader> readers = readerService.findAll();
        assertEquals(1, readers.size());
        assertEquals(readers.get(0).getName(), reader.getName());
    }

    // FIND BY ID
    @Test
    public void findByIdNotExistTest() {
        Long notExistReaderId = 333l;
        String notExistReaderExceptionMessage = serviceExceptionNoEntityWithId(ReaderServiceImpl.SERVICE_NAME, notExistReaderId).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistReaderExceptionMessage);
        readerService.findById(notExistReaderId);
        testException = ExpectedException.none();
    }

    @Test
    public void findByIdSuccessTest() {
        Reader savedReader = readerService.create(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        Reader findReader = readerService.findById(savedReader.getId());

        assertEquals(findReader.getId(), savedReader.getId());
        assertEquals(findReader.getName(), savedReader.getName());
    }
}