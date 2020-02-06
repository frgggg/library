package com.cft.focusstart.library.controller.util;

import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.util.TestStringFieldGenerator;

import java.util.Collections;

import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Reader.READER_IS_DEBTOR_STATUS;
import static com.cft.focusstart.library.model.Reader.READER_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.*;

public class BookRestTestUtil {
    public static final Writer writerOfBook = new Writer(
            TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
            TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
            TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX),
            TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX)
    );
    public static final Long writerOfBookId =  1001l;

    public static final Reader readerOfBook = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
    public static final Reader debtorReaderOfBook = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
    public static final Long readerOfBookId =  2001l;
    public static final Long notExistBookReaderId = 2002l;
    public static final Long debtorReaderOfBookId = 2003l;

    public static final Long existBookId =  1l;
    public static final Long notExistBookId =  2l;
    public static final Long existBookWithReaderId = existBookId;

    public static final BookDto inBookDto = new BookDto();
    public static final BookDto outBookDto = new BookDto();
    public static final BookDto outBookWithReaderDto = new BookDto();

    public static final Book inBook = new Book(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writerOfBook));
    public static final Book outBook = new Book(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writerOfBook));
    public static final Book outBookWithReader = new Book(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writerOfBook));

    static {
        writerOfBook.setId(writerOfBookId);
        readerOfBook.setId(readerOfBookId);

        debtorReaderOfBook.setId(debtorReaderOfBookId);
        debtorReaderOfBook.setDebtor(READER_IS_DEBTOR_STATUS);

        inBookDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        inBookDto.setWriters(Collections.singletonList(writerOfBookId));

        outBookDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        outBookDto.setWriters(Collections.singletonList(writerOfBookId));
        outBookDto.setId(existBookId);

        outBookWithReaderDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        outBookWithReaderDto.setWriters(Collections.singletonList(writerOfBookId));
        outBookWithReaderDto.setId(existBookId);
        outBookWithReaderDto.setReader(readerOfBookId);

        outBook.setId(existBookId);

        outBookWithReader.setId(existBookWithReaderId);
        outBookWithReader.setReader(readerOfBook);
    }
}
