package com.cft.focusstart.library.quartz.finddebtors;

import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.repository.ReaderRepository;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.util.TestStringFieldGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Reader.*;
import static com.cft.focusstart.library.model.Writer.WRITER_FIRST_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.WRITER_SURNAME_LEN_MAX;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class FindDebtorsJobTest {

    @Value("${time-for-find-debtors-circle-test-delay}")
    private String findDebtorsJobTestTestDelay;

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

    @Test
    public void findDebtorsJobTest() throws Exception {
        Writer writer = writerRepository.save(
                new Writer(
                        TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
                        TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
                        TestStringFieldGenerator.getRightNull(),
                        TestStringFieldGenerator.getRightNull()
                )
        );

        Reader reader = readerRepository.save(
          new Reader(
                  TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX)
          )
        );

        Book book = new Book(
                TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX),
                Collections.singletonList(writer)
        );

        book.setReader(reader);
        book.setBackTime(LocalDateTime.now());
        bookRepository.save(book);

        Thread.sleep(new Long(findDebtorsJobTestTestDelay));
        Reader readerAfterDelay = readerRepository.findById(reader.getId()).get();
        assertEquals(readerAfterDelay.getDebtor(), READER_IS_DEBTOR_STATUS);
    }
}