package com.cft.focusstart.library.config;

import com.cft.focusstart.library.dto.converter.BookToBookDto;
import com.cft.focusstart.library.dto.converter.ReaderToReaderDto;
import com.cft.focusstart.library.dto.converter.WriterToWriterDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Autowired
    private WriterToWriterDto writerToWriterDto;

    @Autowired
    private BookToBookDto bookToBookDto;

    @Autowired
    private ReaderToReaderDto readerToReaderDto;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(writerToWriterDto);
        modelMapper.addConverter(bookToBookDto);
        modelMapper.addConverter(readerToReaderDto);

        return modelMapper;
    }
}
