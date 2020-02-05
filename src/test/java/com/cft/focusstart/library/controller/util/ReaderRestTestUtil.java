package com.cft.focusstart.library.controller.util;

import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.util.TestStringFieldGenerator;

import java.util.Collections;

import static com.cft.focusstart.library.model.Reader.READER_IS_NOT_DEBTOR_STATUS;
import static com.cft.focusstart.library.model.Reader.READER_NAME_LEN_MAX;

public class ReaderRestTestUtil {

    public static final Reader inReader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
    public static final Reader outReader = new Reader(inReader.getName());
    public static final Reader outReaderWithBooks = new Reader(inReader.getName());

    public static final ReaderDto inReaderDto = new ReaderDto();
    public static final ReaderDto outReaderDto = new ReaderDto();


    public static final Long existReaderId = 1l;
    public static final Long notExistReaderId = 2l;
    public static final Long existReaderWithBooksId = 1l;

    static {
        inReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));

        outReaderDto.setName(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
        outReaderDto.setId(existReaderId);
        outReaderDto.setIsDebtor(READER_IS_NOT_DEBTOR_STATUS);

        outReader.setId(existReaderId);

        outReaderWithBooks.setId(existReaderWithBooksId);
        outReaderWithBooks.setBooks(Collections.singletonList(new Book(null, null)));
    }
}
