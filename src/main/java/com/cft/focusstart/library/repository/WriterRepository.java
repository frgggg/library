package com.cft.focusstart.library.repository;

import com.cft.focusstart.library.model.Writer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends CrudRepository<Writer, Long> {
}
