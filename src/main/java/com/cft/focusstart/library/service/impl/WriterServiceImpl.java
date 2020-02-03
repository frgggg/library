package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.WriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cft.focusstart.library.exception.ServiceException.*;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionWrongEntity;
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
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Writer create(String firstName, String surname, String middleName, String comment) {
        Writer savedWriter;
        try {
            savedWriter = writerRepository.save(new Writer(firstName, surname, middleName, comment));
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw serviceExceptionWrongEntity(SERVICE_NAME, e.getConstraintViolations().iterator().next().getMessage());
        } catch (PersistenceException e) {
            throw serviceExceptionExistEntity(SERVICE_NAME);
        }
        log.debug(SERVICE_LOG_NEW_ENTITY, savedWriter);
        return savedWriter;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Writer updateById(Long id, String firstName, String surname, String middleName, String comment) {

        Writer updatedWriter;
        try {
            updatedWriter = writerRepository.updateById(id, firstName, surname, middleName, comment);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw serviceExceptionWrongEntity(SERVICE_NAME, e.getConstraintViolations().iterator().next().getMessage());
        } catch (PersistenceException e) {
            throw serviceExceptionExistEntity(SERVICE_NAME);
        }
        log.debug(SERVICE_LOG_UPDATE_ENTITY, id, updatedWriter);
        return updatedWriter;
    }

    @Override
    public Writer findById(Long id) throws SecurityException {
        Optional<Writer> optionalWriterForFind = writerRepository.findById(id);
        if(!optionalWriterForFind.isPresent()) {
            throw serviceExceptionNoEntityWithId(SERVICE_NAME, id);
        }
        log.debug(SERVICE_LOG_GET_ENTITY, id);
        return optionalWriterForFind.get();
    }

    @Override
    public List<Writer> findAll() {
        List<Writer> writers = new ArrayList<>();
        writerRepository.findAll().forEach(writers::add);
        log.debug(SERVICE_LOG_GET_ALL_ENTITY);
        return writers;
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void deleteById(Long id) throws SecurityException {
        try {
            writerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw serviceExceptionNoEntityWithId(SERVICE_NAME, id);
        }
        log.debug(SERVICE_LOG_DELETE_ENTITY, id);
    }
}
