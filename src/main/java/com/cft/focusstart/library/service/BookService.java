package com.cft.focusstart.library.service;

import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Writer;

import java.util.List;

public interface BookService {
    Book create(String name, List<Long> writersIds);
    Book updateById(Long id, String name, List<Long> writersIds);

    Book findById(Long id);
    List<Book> findAll();

    void setReader(Long bookId, Long readerId);
    void unsetReader(Long bookId);

    void deleteById(Long id);
}
