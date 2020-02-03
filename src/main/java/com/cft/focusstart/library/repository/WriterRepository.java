package com.cft.focusstart.library.repository;

import com.cft.focusstart.library.model.Writer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends CrudRepository<Writer, Long> {

    @Modifying(clearAutomatically=true)
    @Query("UPDATE Writer SET firstName = ?2, surname = ?3, middleName = ?4, comment = ?5 WHERE id = ?1")
    Writer updateById(Long id, String firstName, String surname, String middleName, String comment);
}
