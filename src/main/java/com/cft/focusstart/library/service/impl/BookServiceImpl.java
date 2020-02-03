package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.repository.WriterRepository;
import com.cft.focusstart.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionExistEntity;
import static com.cft.focusstart.library.exception.ServiceException.serviceExceptionWrongEntity;
import static com.cft.focusstart.library.log.messages.ServiceLogMessages.SERVICE_LOG_NEW_ENTITY;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    public static final String SERVICE_NAME = "BookServiceImpl";

    private BookRepository bookRepository;
    private EntityManager entityManager;

    @Autowired
    protected BookServiceImpl(BookRepository bookRepository, EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Book create(String name, List<Writer> writers) {
        Book savedBook;
        try {
            savedBook = bookRepository.save(new Book(name, writers));
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw serviceExceptionWrongEntity(SERVICE_NAME, e.getConstraintViolations().iterator().next().getMessage());
        } catch (PersistenceException e) {
            throw serviceExceptionExistEntity(SERVICE_NAME);
        }
        log.debug(SERVICE_LOG_NEW_ENTITY, savedBook);
        return savedBook;
    }

    @Override
    public Book findById(Long id) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Book updateById(Long id, String name, List<Long> writers) {
        return null;
    }
}
