package com.cft.focusstart.library.dto.converter;

import com.cft.focusstart.library.model.Book;
import org.modelmapper.Converter;

import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.model.Reader;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReaderToReaderDto implements Converter<Reader, ReaderDto> {

    @Override
    public ReaderDto convert(MappingContext<Reader, ReaderDto> context) {
        Reader reader = context.getSource();
        ReaderDto readerDto = new ReaderDto();

        readerDto.setId(reader.getId());
        readerDto.setName(reader.getName());
        readerDto.setDebtor(reader.getDebtor());

        List<Book> books = reader.getBooks();
        if(books != null)
        {
            readerDto.setBooks(
                    books.stream()
                            .map(Book::getId)
                            .collect(Collectors.toList())
            );
        }

        return readerDto;
    }
}
