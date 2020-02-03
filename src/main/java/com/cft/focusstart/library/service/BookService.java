package com.cft.focusstart.library.service;

import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Writer;

import java.util.List;

public interface BookService {
    Book create(String name, List<Writer> writers);
    Book updateById(Long id, String name, List<Long> writers);

    Book findById(Long id);
    List<Book> findAll();

    void deleteById(Long id);
}
