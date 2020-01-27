package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.WriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WriterServiceImpl implements WriterService {

    private WriterRepository writerRepository;

    @Autowired
    private WriterServiceImpl(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

    @Override
    public Writer create(String firstName, String surname, String middleName, String comment) {
        Writer writerForSave = new Writer(firstName, surname, middleName, comment);
        Writer savedWriter = writerRepository.save(writerForSave);
        log.debug("New Writer: "  + savedWriter);
        return savedWriter;
    }

    @Override
    public Writer updateById(Long id, String firstName, String surname, String middleName, String comment) {
        Writer writerForUpdate = findById(id);
        writerForUpdate.setFirstName(firstName);
        writerForUpdate.setSurname(surname);
        writerForUpdate.setMiddleName(middleName);
        writerForUpdate.setComment(comment);

        Writer updatedWriter = writerRepository.save(writerForUpdate);
        log.debug("Writer with id {} is updated. Now: {}", updatedWriter.getId(), updatedWriter);
        return updatedWriter;
    }

    @Override
    public Writer findById(Long id) {
        Optional<Writer> optionalWriterForFind = writerRepository.findById(id);
        if(!optionalWriterForFind.isPresent()) {
            return null;
        }
        return optionalWriterForFind.get();
    }

    @Override
    public List<Writer> findAll() {
        List<Writer> writers = new ArrayList<>();
        writerRepository.findAll().forEach(writers::add);
        return writers;
    }

    @Override
    public void deleteById(Long id) {
        writerRepository.deleteById(id);
        log.debug("Writer with id " + id + "is deleted");
    }

    @Override
    public boolean isWriterExist(Long id) {
        return writerRepository.existsById(id);
    }

    @PostConstruct
    public void myMethod() throws Exception {
        this.create("f1", "s1", null, null);
        this.create("f1", "s2", null, null);
        this.create("f1", "s2", null, null);

        this.create("f3", "s3", "m3", "c3");
        this.create("f3", "s3", "m3", "c3");
    }
}
