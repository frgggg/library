package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.dto.ReaderDto;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.service.ReaderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reader")
public class ReaderRest {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReaderService readerService;

    @GetMapping
    @ResponseBody
    public List<ReaderDto> getAllReaders() {
        return readerService.findAll()
                .stream()
                .map(reader -> modelMapper.map(reader, ReaderDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ReaderDto getReader(@PathVariable("id") Long id) {
        Reader reader = readerService.findById(id);
        return modelMapper.map(reader, ReaderDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ReaderDto createReader(@Validated @RequestBody ReaderDto readerDto) {
        Reader reader = readerService.create(readerDto.getName());
        return modelMapper.map(reader, ReaderDto.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ReaderDto updateReader(@Validated @RequestBody ReaderDto readerDto, @PathVariable("id") Long id) {
        Reader reader = readerService.updateById(id, readerDto.getName());
        return modelMapper.map(reader, ReaderDto.class);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteReader(@PathVariable("id") Long id) {
        readerService.deleteById(id);
    }

    @PutMapping(value = "/{id}/refresh-debtor-status")
    @ResponseBody
    public void refreshDebtorStatus(@PathVariable("id") Long id) {
        readerService.refreshDebtorStatus(id);
    }
}
