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
import static com.cft.focusstart.library.model.Book.*;
import static com.cft.focusstart.library.model.Reader.READER_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.WRITER_FIRST_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.WRITER_SURNAME_LEN_MAX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookServiceImplTest {

    @Autowired
    private BookServiceImpl bookService;
    @Autowired
    private WriterServiceImpl writerService;
    @Autowired
    private ReaderServiceImpl readerService;

    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private WriterRepository writerRepository;

    @Before
    public void init() {
        readerRepository.deleteAll();
        bookRepository.deleteAll();
        writerRepository.deleteAll();
    }

    @Rule
    public ExpectedException testException = ExpectedException.none();

    public Writer utilAddWriterToWriterService() {
        return writerService.create(
                TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                TestStringFieldGenerator.getRightNull(),
                TestStringFieldGenerator.getRightNull());
    }

    public Book utilAddBookToBookService(Long writerId) {
        return bookService.create(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writerId));
    }

    // CREATE
    @Test
    public void createNullNameTest() {
        Writer existWriter = utilAddWriterToWriterService();

        String name = TestStringFieldGenerator.getWrongNull();
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createToLittleNameTest() {
        Writer existWriter = utilAddWriterToWriterService();

        String name = TestStringFieldGenerator.getToLittle(BOOK_NAME_LEN_MIN);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigNameTest() {
        Writer existWriter = utilAddWriterToWriterService();

        String name = TestStringFieldGenerator.getToBig(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createNotExistWritersTest() {
        Long notExistWriterId = 333l;
        String notExistWriterExceptionMessage = serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId).getMessage();

        String name = TestStringFieldGenerator.getToBig(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(notExistWriterId);

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistWriterExceptionMessage);
        bookService.create(name, writersIds);
        testException = ExpectedException.none();
    }

    @Test
    public void createSuccessTest() {
        Writer existWriter = utilAddWriterToWriterService();

        String name = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        Book savedBook = bookService.create(name, writersIds);
        List<Writer> savedBookWriters = savedBook.getWriters();

        assertNotNull(savedBook.getId());
        assertEquals(name, savedBook.getName());
        assertEquals(writersIds.size(), savedBookWriters.size());
        assertEquals(existWriter.getId(), savedBookWriters.get(0).getId());
    }

    // FIND ALL
    @Test
    public void findAllSuccessTest() {
        Writer existWriter = utilAddWriterToWriterService();

        String name = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        Book savedBook = bookService.create(name, writersIds);
        List<Writer> savedBookWriters = savedBook.getWriters();

        List<Book> allBooks = bookService.findAll();
        assertEquals(1, allBooks.size());

        Book findBook = allBooks.get(0);
        List<Writer> findBookWriters = findBook.getWriters();

        assertEquals(findBook.getId(), savedBook.getId());
        assertEquals(findBook.getName(), savedBook.getName());
        assertEquals(findBookWriters.size(), savedBookWriters.size());
        assertEquals(findBookWriters.get(0).getId(), savedBookWriters.get(0).getId());
    }

    // FIND BY ID
    @Test
    public void findByIdSuccessTest() {
        Writer existWriter = utilAddWriterToWriterService();

        String name = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        Book savedBook = bookService.create(name, writersIds);
        List<Writer> savedBookWriters = savedBook.getWriters();

        Book findByIdBook = bookService.findById(savedBook.getId());
        List<Writer> findByIdBookWriters = findByIdBook.getWriters();

        assertEquals(findByIdBook.getName(), savedBook.getName());
        assertEquals(findByIdBookWriters.size(), savedBookWriters.size());
        assertEquals(findByIdBookWriters.get(0).getId(), savedBookWriters.get(0).getId());
    }

    @Test
    public void findByIdNotExistBookTest() {
        Long notExistBookId = 333l;
        String notExistWriterExceptionMessage = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistWriterExceptionMessage);
        bookService.findById(notExistBookId);
        testException = ExpectedException.none();

    }

    // DELETE BY ID
    @Test
    public void deleteByIdNotExistBookTest() {
        Long notExistBookId = 333l;
        String notExistWriterExceptionMessage = serviceExceptionNoEntityWithId(BookServiceImpl.SERVICE_NAME, notExistBookId).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistWriterExceptionMessage);
        bookService.deleteById(notExistBookId);
        testException = ExpectedException.none();

    }

    @Test
    public void deleteByIdBusyBookTest() {
        Writer existWriter = utilAddWriterToWriterService();
        Reader reader = readerService.create(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        String name = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        Book savedBook = bookService.create(name, writersIds);
        bookService.setReader(savedBook.getId(), reader.getId());

        String busyBookExceptionMessage = serviceExceptionDeleteOrUpdateRelatedEntity(BookServiceImpl.SERVICE_NAME, BOOK_READER_FIELD_NAME).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(busyBookExceptionMessage);
        bookService.deleteById(savedBook.getId());
        testException = ExpectedException.none();
    }

    // UPDATE BY ID
    @Test
    public void updateByIdNullNameTest() {
        Writer existWriter = utilAddWriterToWriterService();
        Book existBook = utilAddBookToBookService(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        String toBigBookName = TestStringFieldGenerator.getWrongNull();
        bookService.updateById(existBook.getId(), toBigBookName, Collections.singletonList(existWriter.getId()));
        testException = ExpectedException.none();
    }

    @Test
    public void updateByIdToLittleNameTest() {
        Writer existWriter = utilAddWriterToWriterService();
        Book existBook = utilAddBookToBookService(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        String toBigBookName = TestStringFieldGenerator.getToLittle(BOOK_NAME_LEN_MIN);
        bookService.updateById(existBook.getId(), toBigBookName, Collections.singletonList(existWriter.getId()));
        testException = ExpectedException.none();
    }

    @Test
    public void updateByIdToBigNameTest() {
        Writer existWriter = utilAddWriterToWriterService();
        Book existBook = utilAddBookToBookService(existWriter.getId());

        testException.expect(ServiceException.class);
        testException.expectMessage(BOOK_NAME_VALIDATION_MESSAGE);
        String toBigBookName = TestStringFieldGenerator.getToBig(BOOK_NAME_LEN_MAX);
        bookService.updateById(existBook.getId(), toBigBookName, Collections.singletonList(existWriter.getId()));
        testException = ExpectedException.none();
    }

    @Test
    public void updateByIdNotExistWriterTest() {
        Writer existWriter = utilAddWriterToWriterService();
        Book existBook = utilAddBookToBookService(existWriter.getId());
        Long notExistWriterId = 333l;
        String notExistWriterExceptionMessage = serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistWriterExceptionMessage);
        bookService.updateById(existBook.getId(), existBook.getName(), Collections.singletonList(notExistWriterId));
        testException = ExpectedException.none();
    }

    // SET READER
    @Test
    public void setReaderSuccess() {
        Writer existWriter = utilAddWriterToWriterService();
        Reader reader = readerService.create(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        String name = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        List<Long> writersIds = Collections.singletonList(existWriter.getId());

        Book savedBook = bookService.create(name, writersIds);
        bookService.setReader(savedBook.getId(), reader.getId());

        Book findByIdBook = bookService.findById(savedBook.getId());
        Reader findByIdBookReader = findByIdBook.getReader();

        assertEquals(reader.getName()
                , findByIdBookReader.getName());
        assertEquals(reader.getId()
                , findByIdBookReader.getId());
    }
}