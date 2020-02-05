package com.cft.focusstart.library.dto.converter;

import org.modelmapper.Converter;

import com.cft.focusstart.library.dto.WriterDto;
import com.cft.focusstart.library.model.Writer;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class WriterToWriterDto  implements Converter<Writer, WriterDto> {
    @Override
    public WriterDto convert(MappingContext<Writer, WriterDto> context) {
        Writer writer = context.getSource();
        WriterDto writerDto = new WriterDto();

        writerDto.setId(writer.getId());
        writerDto.setFirstName(writer.getFirstName());
        writerDto.setSurname(writer.getSurname());
        writerDto.setMiddleName(writer.getMiddleName());
        writerDto.setComment(writer.getComment());


        return writerDto;
    }
}
