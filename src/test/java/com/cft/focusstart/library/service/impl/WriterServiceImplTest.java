package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Writer;

import com.cft.focusstart.library.repository.WriterRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import static com.cft.focusstart.library.exception.ServiceException.getSaveServiceException;
import static com.cft.focusstart.library.model.Writer.*;

import com.cft.focusstart.library.util.TestStringFieldGenerator;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WriterServiceImplTest {

    WriterServiceImpl writerService;

    @Mock
    private WriterRepository mockWriterRepository;
    @Autowired
    private EntityManager mockEntityManager;

    @Before
    public void init() {
        writerService = new WriterServiceImpl(mockWriterRepository, mockEntityManager);
    }


    @Rule
    public ExpectedException testException = ExpectedException.none();

    @Test
    public void createNullFirstNameTest() {
        String firstName = TestStringFieldGenerator.getWrongNull();
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        ServiceException serviceException = getSaveServiceException(
                WriterServiceImpl.SERVICE_NAME,
                (new Writer(firstName, surname, middleName, comment)).toString(),
                null
        );

        testException.expect(ServiceException.class);
        testException.expectMessage(serviceException.getMessage());
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToLittleFirstNameTest() {
        String firstName = TestStringFieldGenerator.getToLittle(WRITER_FIRST_NAME_LEN_MIN);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        ServiceException serviceException = getSaveServiceException(
                WriterServiceImpl.SERVICE_NAME,
                (new Writer(firstName, surname, middleName, comment)).toString(),
                null
        );

        testException.expect(ServiceException.class);
        testException.expectMessage(serviceException.getMessage());
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createToBigFirstNameTest() {
        String firstName = TestStringFieldGenerator.getToBig(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();

        ServiceException serviceException = getSaveServiceException(
                WriterServiceImpl.SERVICE_NAME,
                (new Writer(firstName, surname, middleName, comment)).toString(),
                null
        );

        testException.expect(ServiceException.class);
        testException.expectMessage(serviceException.getMessage());
        writerService.create(firstName, surname, middleName, comment);
        testException = ExpectedException.none();
    }

    @Test
    public void createRightTest() {
        String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
        String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
        String middleName = TestStringFieldGenerator.getRightNull();
        String comment = TestStringFieldGenerator.getRightNull();
        Long id = 1l;

        Writer writerForSave = new Writer(firstName, surname, middleName, comment);
        Writer saveReturnWriter = new Writer(firstName, surname, middleName, comment);
        saveReturnWriter.setId(id);
        when(mockWriterRepository.save(writerForSave)).thenReturn(saveReturnWriter);

        Writer savedWriter = writerService.create(firstName, surname, middleName, comment);

        assertEquals(saveReturnWriter, savedWriter);

        verify(mockWriterRepository).save(writerForSave);
    }

}