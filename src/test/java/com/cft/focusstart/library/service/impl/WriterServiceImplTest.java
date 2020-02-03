package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.util.TestStringFieldGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static com.cft.focusstart.library.model.Writer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class WriterServiceImplTest {

    WriterServiceImpl writerService;

    @Autowired
    private WriterRepository writerRepository;
    @Autowired
    private EntityManager entityManager;

    @Before
    public void init() {
        writerService = new WriterServiceImpl(writerRepository, entityManager);
    }

    @Rule
    public ExpectedException testException = ExpectedException.none();

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
}