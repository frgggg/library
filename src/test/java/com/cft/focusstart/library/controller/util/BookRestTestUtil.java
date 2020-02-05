package com.cft.focusstart.library.controller.util;

import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.util.TestStringFieldGenerator;

import java.util.Collections;

import static com.cft.focusstart.library.model.Book.BOOK_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Reader.READER_NAME_LEN_MAX;
import static com.cft.focusstart.library.model.Writer.*;

public class BookRestTestUtil {
    public static final Writer writer = new Writer(
            TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX),
            TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX),
            TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX),
            TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX)
    );
    public static final Long writerId =  1001l;

    public static final Reader reader = new Reader(TestStringFieldGenerator.getRightByMax(READER_NAME_LEN_MAX));
    public static final Long readerId =  2001l;

    public static final Long existBookId =  1l;
    public static final Long notExistBookId =  2l;
    public static final Long existBookWithReaderId =  3l;

    public static final BookDto inBookDto = new BookDto();
    public static final BookDto outBookDto = new BookDto();

    public static final Book inBook = new Book(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writer));
    public static final Book outBook = new Book(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writer));
    public static final Book outBookWithReader = new Book(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX), Collections.singletonList(writer));

    static {
        writer.setId(writerId);
        reader.setId(readerId);

        inBookDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        inBookDto.setWriters(Collections.singletonList(writerId));

        outBookDto.setName(TestStringFieldGenerator.getRightByMax(BOOK_NAME_LEN_MAX));
        outBookDto.setWriters(Collections.singletonList(writerId));
        outBookDto.setId(existBookId);

        outBook.setId(existBookId);

        outBookWithReader.setId(existBookId);
        outBookWithReader.setReader(reader);
    }
}
