package com.cft.focusstart.library.controller.util;

import com.cft.focusstart.library.dto.WriterDto;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.util.TestStringFieldGenerator;

import java.util.Collections;

import static com.cft.focusstart.library.model.Writer.*;
import static com.cft.focusstart.library.model.Writer.WRITER_COMMENT_LEN_MAX;
import com.cft.focusstart.library.model.Book;

public class WriterRestTestUtil {
    public static final Long existWriterId = 1l;
    public static final Long notExistWriterId = 2l;
    public static final Long existWriterWithBookId = 3l;

    public static final String firstName = TestStringFieldGenerator.getRightByMax(WRITER_FIRST_NAME_LEN_MAX);
    public static final String surname = TestStringFieldGenerator.getRightByMax(WRITER_SURNAME_LEN_MAX);
    public static final String middleName = TestStringFieldGenerator.getRightByMax(WRITER_MIDDLE_NAME_LEN_MAX);
    public static final String comment = TestStringFieldGenerator.getRightByMax(WRITER_COMMENT_LEN_MAX);

    public static final  WriterDto inWriterDto = new WriterDto();
    public static final  WriterDto outWriterDto = new WriterDto();


    public static final  Writer inWriter = new Writer(firstName, surname, middleName, comment);
    public static final  Writer outWriter = new Writer(firstName, surname, middleName, comment);
    public static final  Writer outWriterWithBook = new Writer(firstName, surname, middleName, comment);

    static {
        inWriterDto.setFirstName(firstName);
        inWriterDto.setSurname(surname);
        inWriterDto.setMiddleName(middleName);
        inWriterDto.setComment(comment);

        outWriterDto.setFirstName(firstName);
        outWriterDto.setSurname(surname);
        outWriterDto.setMiddleName(middleName);
        outWriterDto.setComment(comment);
        outWriterDto.setId(existWriterId);

        outWriter.setId(existWriterId);
        outWriterWithBook.setId(existWriterWithBookId);
        outWriterWithBook.setBooks(Collections.singletonList(new Book(null, null)));
    }
}
