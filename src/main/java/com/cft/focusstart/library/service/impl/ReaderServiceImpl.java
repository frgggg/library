package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.repository.ReaderRepository;
import com.cft.focusstart.library.service.BookService;
import com.cft.focusstart.library.service.ReaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cft.focusstart.library.exception.ServiceException.*;
import static com.cft.focusstart.library.log.messages.ServiceLogMessages.*;
import static com.cft.focusstart.library.model.Reader.*;

@Service
@Slf4j
public class ReaderServiceImpl implements ReaderService {

    public static final String SERVICE_NAME = "ReaderServiceImpl";

    private ReaderRepository readerRepository;
    private EntityManager entityManager;

    @Autowired
    protected ReaderServiceImpl(ReaderRepository readerRepository, EntityManager entityManager) {
        this.readerRepository = readerRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Reader create(String name) {
        Reader savedReader;
        try {
            savedReader = readerRepository.save(new Reader(name));
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw serviceExceptionWrongEntity(SERVICE_NAME, e.getConstraintViolations().iterator().next().getMessage());
        } catch (PersistenceException e) {
            System.out.println(e.getMessage());
            throw serviceExceptionExistEntity(SERVICE_NAME);
        }
        log.debug(SERVICE_LOG_NEW_ENTITY, savedReader);
        return savedReader;
    }

    @Override
    public Reader findById(Long id) {
        Optional<Reader> optionalReaderForFind = readerRepository.findById(id);
        if(!optionalReaderForFind.isPresent()) {
            throw serviceExceptionNoEntityWithId(SERVICE_NAME, id);
        }
        log.debug(SERVICE_LOG_GET_ENTITY, id);
        return optionalReaderForFind.get();
    }

    @Override
    public List<Reader> findAll() {
        List<Reader> readers = new ArrayList<>();
        readerRepository.findAll().forEach(readers::add);
        log.debug(SERVICE_LOG_GET_ALL_ENTITY);
        return readers;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Reader updateById(Long id, String name) {
        Reader readerForUpdate = findById(id);
        Reader updatedReader;

        try {
            readerForUpdate.setName(name);
            updatedReader = readerRepository.save(readerForUpdate);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw serviceExceptionWrongEntity(SERVICE_NAME, e.getConstraintViolations().iterator().next().getMessage());
        } catch (PersistenceException e) {
            throw serviceExceptionExistEntity(SERVICE_NAME);
        }
        log.debug(SERVICE_LOG_UPDATE_ENTITY, id, updatedReader);
        return updatedReader;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public void deleteById(Long id) {
        Reader readerForDelete = findById(id);
        List<Book> books = readerForDelete.getBooks();
        if(books != null) {
            if(books.size() > 0) {
                throw serviceExceptionDeleteOrUpdateRelatedEntity(SERVICE_NAME, READER_BOOKS_FIELD_NAME);
            }
        }
        try {
            readerRepository.deleteById(id);
            entityManager.flush();
        } catch (EmptyResultDataAccessException e) {
            throw serviceExceptionNoEntityWithId(SERVICE_NAME, id);
        }
        log.debug(SERVICE_LOG_DELETE_ENTITY, id);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public void refreshDebtorStatus(Long id) {
        Reader readerForUpdateReaderStatus = findById(id);
        List<Book> books = readerForUpdateReaderStatus.getBooks();
        Boolean debtorStatus = READER_IS_NOT_DEBTOR_STATUS;

        if(books != null) {
            if(books.size() > 0) {
                LocalDateTime nowTime = LocalDateTime.now();
                for(Book book: books) {
                    if(nowTime.isAfter(book.getBackTime())) {
                        debtorStatus = READER_IS_DEBTOR_STATUS;
                        break;
                    }
                }
            }
        }

        readerForUpdateReaderStatus.setDebtor(debtorStatus);
        Reader updatedReader = readerRepository.save(readerForUpdateReaderStatus);
        log.debug(SERVICE_LOG_UPDATE_ENTITY, id, updatedReader);
    }

    @Override
    public List<Reader> findNotDebtors() {
        return readerRepository.findNotDebtors();
    }
}
