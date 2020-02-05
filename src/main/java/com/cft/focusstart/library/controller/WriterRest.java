package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.dto.WriterDto;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.service.WriterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/writer")
public class WriterRest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WriterService writerService;

    @GetMapping
    @ResponseBody
    public List<WriterDto> getAllWriters() {
        return writerService.findAll()
                .stream()
                .map(reader -> modelMapper.map(reader, WriterDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public WriterDto getWriter(@PathVariable("id") Long id) {
        Writer writer = writerService.findById(id);
        return modelMapper.map(writer, WriterDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public WriterDto createWriter(@Validated @RequestBody WriterDto writerDto) {
        Writer writer = writerService.create(
                writerDto.getFirstName(),
                writerDto.getSurname(),
                writerDto.getMiddleName(),
                writerDto.getComment()
        );
        return modelMapper.map(writer, WriterDto.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public WriterDto updateWriter(@Validated @RequestBody WriterDto writerDto, @PathVariable("id") Long id) {
        Writer writer = writerService.updateById(
                id,
                writerDto.getFirstName(),
                writerDto.getSurname(),
                writerDto.getMiddleName(),
                writerDto.getComment()
        );
        return modelMapper.map(writer, WriterDto.class);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteWriter(@PathVariable("id") Long id) {
        writerService.deleteById(id);
    }
}
