package com.cft.focusstart.library.service.impl;

import com.cft.focusstart.library.exception.ServiceException;
import com.cft.focusstart.library.model.Book;
import com.cft.focusstart.library.model.Reader;
import com.cft.focusstart.library.model.Writer;
import com.cft.focusstart.library.repository.BookRepository;
import com.cft.focusstart.library.service.BookService;
import com.cft.focusstart.library.service.ReaderService;
import com.cft.focusstart.library.service.WriterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.cft.focusstart.library.exception.ServiceException.*;
import static com.cft.focusstart.library.log.messages.ServiceLogMessages.*;
import static com.cft.focusstart.library.model.Book.BOOK_READER_FIELD_NAME;
import static com.cft.focusstart.library.model.Reader.READER_IS_DEBTOR_FIELD_NAME;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    public static final String SERVICE_NAME = "BookServiceImpl";

    @Value("${time-for-read.month:0}")
    private int backTimeMonth;
    @Value("${time-for-read.day:30}")
    private int backTimeDay;
    @Value("${time-for-read.min:0}")
    private int backTimeMin;

    private BookRepository bookRepository;
    private WriterService writerService;
    private ReaderService readerService;
    private EntityManager entityManager;

    @Autowired
    protected BookServiceImpl(BookRepository bookRepository, WriterService writerService, ReaderService readerService, EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.writerService = writerService;
        this.readerService = readerService;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Book create(String name, List<Long> writersIds) {
        Book savedBook;

        try {
            savedBook = bookRepository.save(new Book(name, getWritersByIds(writersIds)));
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
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public void setReader(Long bookId, Long readerId) {
        Book bookForUpdate = findById(bookId);
        Reader reader = readerService.findById(readerId);

        if(reader.getDebtor()) {
            serviceExceptionSetWrongSubEntity(SERVICE_NAME, readerId, READER_IS_DEBTOR_FIELD_NAME, reader.getDebtor().toString());
        }

        if(bookForUpdate.getReader() != null) {
            throw serviceExceptionDeleteOrUpdateRelatedEntity(SERVICE_NAME, BOOK_READER_FIELD_NAME);
        }

        bookForUpdate.setReader(reader);
        bookForUpdate.setBackTime(
                LocalDateTime.now()
                        .plusMonths(backTimeMonth)
                        .plusDays(backTimeDay)
                        .plusMinutes(backTimeMin)
        );

        Book updatedBook = bookRepository.save(bookForUpdate);
        log.debug(SERVICE_LOG_UPDATE_ENTITY, bookId, updatedBook);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public void unsetReader(Long id) {
        Book bookForUpdate = findById(id);
        bookForUpdate.setReader(null);
        bookForUpdate.setBackTime(null);
        Book updatedBook = bookRepository.save(bookForUpdate);
        log.debug(SERVICE_LOG_UPDATE_ENTITY, id, updatedBook);
    }

    @Override
    public Book findById(Long id) {
        Optional<Book> optionalBookForFind = bookRepository.findById(id);
        if(!optionalBookForFind.isPresent()) {
            throw serviceExceptionNoEntityWithId(SERVICE_NAME, id);
        }
        log.debug(SERVICE_LOG_GET_ENTITY, id);
        return optionalBookForFind.get();
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        log.debug(SERVICE_LOG_GET_ALL_ENTITY);
        return books;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public void deleteById(Long id) {
        Book bookForDelete = findById(id);
        if(bookForDelete.getReader() != null) {
            throw serviceExceptionDeleteOrUpdateRelatedEntity(SERVICE_NAME, BOOK_READER_FIELD_NAME);
        }

        try {
            bookRepository.deleteById(id);
            entityManager.flush();
        } catch (EmptyResultDataAccessException e) {
            throw serviceExceptionNoEntityWithId(SERVICE_NAME, id);
        }
        log.debug(SERVICE_LOG_DELETE_ENTITY, id);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = ServiceException.class)
    public Book updateById(Long id, String name, List<Long> writersIds) {
        Book bookForUpdate = findById(id);
        Book updatedBook;

        try {
            bookForUpdate.setName(name);
            bookForUpdate.setWriters(getWritersByIds(writersIds));
            updatedBook = bookRepository.save(bookForUpdate);
            entityManager.flush();
        } catch (ConstraintViolationException e) {
            throw serviceExceptionWrongEntity(SERVICE_NAME, e.getConstraintViolations().iterator().next().getMessage());
        } catch (PersistenceException e) {
            throw serviceExceptionExistEntity(SERVICE_NAME);
        }
        log.debug(SERVICE_LOG_UPDATE_ENTITY, id, updatedBook);
        return updatedBook;
    }

    private ArrayList<Writer> getWritersByIds(List<Long> writersIds) {
        HashSet<Writer> writers = new HashSet<>();
        for(int i = 0; i < writersIds.size(); i++)
            writers.add(writerService.findById(writersIds.get(i)));

        return new ArrayList<Writer>(writers);
    }
}
