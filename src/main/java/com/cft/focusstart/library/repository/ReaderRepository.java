package com.cft.focusstart.library.repository;

import com.cft.focusstart.library.model.Reader;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReaderRepository extends CrudRepository<Reader, Long> {

    @Query("SELECT r FROM Reader r WHERE r.isDebtor = false")
    List<Reader> findNotDebtors();
}
