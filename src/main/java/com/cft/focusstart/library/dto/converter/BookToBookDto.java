package com.cft.focusstart.library.dto.converter;

import com.cft.focusstart.library.model.Writer;
import org.modelmapper.Converter;

import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.model.Book;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookToBookDto  implements Converter<Book, BookDto> {

    @Override
    public BookDto convert(MappingContext<Book, BookDto> context) {
        Book book = context.getSource();
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());

        if(book.getReader() != null) {
            bookDto.setReader(book.getReader().getId());
        }
        bookDto.setBackTime(book.getBackTime());

        List<Writer> writers = book.getWriters();
        if(writers != null) {
            bookDto.setWriters(
                    writers.stream()
                            .map(Writer::getId)
                            .collect(Collectors.toList())
            );
        }

        return bookDto;
    }
}
