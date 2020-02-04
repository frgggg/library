package com.cft.focusstart.library.service;

import com.cft.focusstart.library.model.Reader;

import java.util.List;

public interface ReaderService {
    Reader create(String name);
    Reader updateById(Long id, String name);

    Reader findById(Long id);
    List<Reader> findAll();

    void refreshDebtorStatus(Long id);

    void deleteById(Long id);
}
