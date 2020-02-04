package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.BookRepository;
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

import static com.cft.focusstart.library.exception.ServiceException.*;
import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class WriterServiceImplTest {

    @Autowired
    WriterServiceImpl writerService;
    @Autowired
    BookServiceImpl bookService;

    @Autowired
    WriterRepository writerRepository;
    @Autowired
    BookRepository bookRepository;

    @Before
    public void init() {
        writerRepository.deleteAll();
        bookRepository.deleteAll();

    }

    @Rule
    public ExpectedException testException = ExpectedException.none();

    // CREATE
    @Test
    public void createNullFirstNameTest() {
        String firstName = TestStringFieldGenerator.getWrongNull();
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_FIRST_NAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToLittleFirstNameTest() {
        String firstName = TestStringFieldGenerator.getToLittle(WRITER_FIRST_NAME_LEN_MIN);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_FIRST_NAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigFirstNameTest() {
        String firstName = TestStringFieldGenerator.getToBig(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_FIRST_NAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createNullSurnameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getWrongNull();
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_SURNAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToLittleSurnameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getToLittle(WRITER_SURNAME_LEN_MIN);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_SURNAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigSurnameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getToBig(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_SURNAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigMiddleNameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getToBig(WRITER_MIDDLE_NAME_LEN_MAX);
        String comment = TestStringFieldGenerator.getRightNull();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_MIDDLE_NAME_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigCommentTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX);
        String comment = TestStringFieldGenerator.getToBig(WRITER_COMMENT_LEN_MAX);

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_COMMENT_VALIDATION_MESSAGE);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createExistWriter() {
        String firstName = TestStringFieldGenerator.getRightByMin(WRITER_FIRST_NAME_LEN_MIN);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        writerService.create(firstName, surname, middleName, comment);

        testException.expect(ServiceException.class);
        testException.expectMessage(SERVICE_EXCEPTION_EXIST_ENTITY_FORMAT_STRING);
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createSuccess() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Writer savedWriter = writerService.create(firstName, surname, middleName, comment);
        assertNotNull(savedWriter.getId());
        assertEquals(firstName, savedWriter.getFirstName());
        assertEquals(surname, savedWriter.getSurname());
        assertEquals(middleName, savedWriter.getMiddleName());
        assertEquals(comment, savedWriter.getComment());
    }

    // FIND ALL
    @Test
    public void findAllSuccess() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Writer savedWriter = writerService.create(firstName, surname, middleName, comment);
        List<Writer> writerList = writerService.findAll();
        assertEquals(writerList.size(), 1);
        Writer findWriter = writerList.get(0);
        assertEquals(savedWriter.getId(), findWriter.getId());
        assertEquals(savedWriter.getFirstName(), findWriter.getFirstName());
        assertEquals(savedWriter.getSurname(), findWriter.getSurname());
        assertEquals(savedWriter.getMiddleName(), findWriter.getMiddleName());
        assertEquals(savedWriter.getComment(), findWriter.getComment());
    }

    // FIND BY ID
    @Test
    public void findByIdSuccess() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Writer savedWriter = writerService.create(firstName, surname, middleName, comment);
        Writer findWriter = writerService.findById(savedWriter.getId());

        assertEquals(savedWriter.getId(), findWriter.getId());
        assertEquals(savedWriter.getFirstName(), findWriter.getFirstName());
        assertEquals(savedWriter.getSurname(), findWriter.getSurname());
        assertEquals(savedWriter.getMiddleName(), findWriter.getMiddleName());
        assertEquals(savedWriter.getComment(), findWriter.getComment());
    }

    @Test
    public void findByIdNotExistWriter() {
        Long notExistWriterId = 333l;
        String notExistWriterExceptionMessage = serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId).getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistWriterExceptionMessage);
        writerService.findById(notExistWriterId);
        testException = ExpectedException.none();
    }

    // DELETE BY ID
    @Test
    public void deleteByIdNotExistWriter() {
        Long notExistWriterId = 333l;
        String notExistWriterExceptionMessage =
                serviceExceptionNoEntityWithId(WriterServiceImpl.SERVICE_NAME, notExistWriterId)
                        .getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(notExistWriterExceptionMessage);
        writerService.deleteById(notExistWriterId);
        testException = ExpectedException.none();
    }

    @Test
    public void deleteByIdRelatedWriter() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();
        Writer savedWriter = writerService.create(firstName, surname, middleName, comment);

        String bookName = TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX);
        bookService.create(bookName, Collections.singletonList(savedWriter.getId()));

        String exceptionMessage =
                serviceExceptionDeleteOrUpdateRelatedEntity(WriterServiceImpl.SERVICE_NAME, WRITER_BOOKS_FIELD_NAME)
                        .getMessage();

        testException.expect(ServiceException.class);
        testException.expectMessage(exceptionMessage);
        writerService.deleteById(savedWriter.getId());
        testException = ExpectedException.none();
    }

    @Test
    public void deleteSuccess() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        assertEquals(writerService.findAll().size(), 0);

        Long savedWriterId = writerService.create(firstName, surname, middleName, comment).getId();
        assertEquals(writerService.findAll().size(), 1);

        writerService.deleteById(savedWriterId);
        assertEquals(writerService.findAll().size(), 0);

    }

    // UPDATE
    @Test
    public void updateNullFirstNameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Long createdWriterId = writerService.create(firstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_FIRST_NAME_VALIDATION_MESSAGE);
        String updatedFirstName = TestStringFieldGenerator.getWrongNull();
        writerService.updateById(createdWriterId, updatedFirstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void updateToLittleFirstNameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Long createdWriterId = writerService.create(firstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_FIRST_NAME_VALIDATION_MESSAGE);
        String updatedFirstName = TestStringFieldGenerator.getToLittle(WRITER_FIRST_NAME_LEN_MIN);
        writerService.updateById(createdWriterId, updatedFirstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void updateToBigFirstNameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Long createdWriterId = writerService.create(firstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_FIRST_NAME_VALIDATION_MESSAGE);
        String updatedFirstName = TestStringFieldGenerator.getToBig(WRITER_FIRST_NAME_LEN_MAX);
        writerService.updateById(createdWriterId, updatedFirstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void updateNullSurnameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Long createdWriterId = writerService.create(firstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_SURNAME_VALIDATION_MESSAGE);
        String updatedSurname = TestStringFieldGenerator.getWrongNull();
        writerService.updateById(createdWriterId, firstName, updatedSurname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void updateToLittleSurnameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Long createdWriterId = writerService.create(firstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_SURNAME_VALIDATION_MESSAGE);
        String updatedSurname = TestStringFieldGenerator.getToLittle(WRITER_SURNAME_LEN_MIN);
        writerService.updateById(createdWriterId, firstName, updatedSurname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void updateToBigSurnameTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        Long createdWriterId = writerService.create(firstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(WRITER_SURNAME_VALIDATION_MESSAGE);
        String updatedSurname = TestStringFieldGenerator.getToBig(WRITER_SURNAME_LEN_MAX);
        writerService.updateById(createdWriterId, firstName, updatedSurname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void updateExistWriter() {
        String writerOneFirstName = TestStringFieldGenerator.getRightByMin(WRITER_FIRST_NAME_LEN_MIN) + "_1";
        String writerTwoFirstName = TestStringFieldGenerator.getRightByMin(WRITER_FIRST_NAME_LEN_MIN) + "_2";
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        writerService.create(writerOneFirstName, surname, middleName, comment);
        Long writerTwoId = writerService.create(writerTwoFirstName, surname, middleName, comment).getId();

        testException.expect(ServiceException.class);
        testException.expectMessage(SERVICE_EXCEPTION_EXIST_ENTITY_FORMAT_STRING);
        writerService.updateById(writerTwoId, writerOneFirstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }
}