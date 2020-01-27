package com.cft.focusstart.library.service;

import com.cft.focusstart.library.model.Writer;

import java.util.List;

public interface WriterService {
    Writer create(String firstName, String surname, String middleName, String comment);
    Writer updateById(Long id, String firstName, String surname, String middleName, String comment);

    boolean isWriterExist(Long id);
    Writer findById(Long id);
    List<Writer> findAll();

    void deleteById(Long id);
}
