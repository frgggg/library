package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.WriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cft.focusstart.library.exception.ServiceException.*;
import static com.cft.focusstart.library.log.messages.ServiceLogMessages.*;

@Service
@Slf4j
public class WriterServiceImpl implements WriterService {

    public static final String SERVICE_NAME = "WriterServiceImpl";

    private WriterRepository writerRepository;
    private EntityManager entityManager;

    @Autowired
    protected WriterServiceImpl(WriterRepository writerRepository, EntityManager entityManager) {
        this.writerRepository = writerRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Writer create(String firstName, String surname, String middleName, String comment) {
        Writer savedWriter;
        try {
            savedWriter = writerRepository.save(new Writer(firstName, surname, middleName, comment));
            entityManager.flush();
        } catch (DataIntegrityViolationException | PersistenceException | ConstraintViolationException e) {
            throw getSaveServiceException(SERVICE_NAME, (new Writer(firstName, surname, middleName, comment)).toString(), e);
        } catch (Exception e) {
            throw getUnknownServiceException(SERVICE_NAME, e);
        }
        log.debug(SERVICE_LOG_NEW_ENTITY, savedWriter);
        return savedWriter;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Writer updateById(Long id, String firstName, String surname, String middleName, String comment) {
        Writer writerForUpdate = findById(id);
        writerForUpdate.setFirstName(firstName);
        writerForUpdate.setSurname(surname);
        writerForUpdate.setMiddleName(middleName);
        writerForUpdate.setComment(comment);
        Writer updatedWriter;
        try {
            updatedWriter = writerRepository.save(writerForUpdate);
            entityManager.flush();
        } catch (DataIntegrityViolationException | PersistenceException | ConstraintViolationException e) {
            throw getUpdateServiceException(SERVICE_NAME, writerForUpdate.toString(), e);
        } catch (Exception e) {
            throw getUnknownServiceException(SERVICE_NAME, e);
        }
        log.debug(SERVICE_LOG_UPDATE_ENTITY, updatedWriter.getId(), updatedWriter);
        return updatedWriter;
    }

    @Override
    public Writer findById(Long id) throws SecurityException {
        Optional<Writer> optionalWriterForFind = writerRepository.findById(id);
        if(!optionalWriterForFind.isPresent()) {
            throw getFindByIdServiceException(SERVICE_NAME, id);
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteById(Long id) throws SecurityException {
        Writer writerForDelete = findById(id);
        if(writerForDelete.getBooks() != null) {
            if(writerForDelete.getBooks().size() > 0) {
                throw getDeleteByIdServiceException(SERVICE_NAME, writerForDelete.toString());
            }
        }
        writerRepository.delete(writerForDelete);
        log.debug(SERVICE_LOG_DELETE_ENTITY, id);
    }
}
