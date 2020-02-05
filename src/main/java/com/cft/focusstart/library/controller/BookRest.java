package com.cft.focusstart.library.controller;

import com.cft.focusstart.library.dto.BookDto;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookRest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookService bookService;

    @GetMapping
    @ResponseBody
    public List<BookDto> getAllBooks() {
        return bookService.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public BookDto getBook(@PathVariable("id") Long id) {
        Book book = bookService.findById(id);
        return modelMapper.map(book, BookDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public BookDto createBook(@Validated @RequestBody BookDto bookDto) {
        Book book = bookService.create(
                bookDto.getName(),
                bookDto.getWriters()
        );
        return modelMapper.map(book, BookDto.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BookDto updateBook(@Validated @RequestBody BookDto bookDto, @PathVariable("id") Long id) {
        Book book = bookService.updateById(
                id,
                bookDto.getName(),
                bookDto.getWriters()
        );
        return modelMapper.map(book, BookDto.class);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    @PutMapping(value = "/{id}/set-reader")
    @ResponseStatus(HttpStatus.OK)
    public void setReaderForBook(@PathVariable("id") Long id, @RequestParam(name="reader") Long readerId) {
        bookService.setReader(id, readerId);
    }

    @PutMapping(value = "/{id}/unset-reader")
    @ResponseStatus(HttpStatus.OK)
    public void setBookFree(@PathVariable("id") Long id) {
        bookService.unsetReader(id);
    }
}
