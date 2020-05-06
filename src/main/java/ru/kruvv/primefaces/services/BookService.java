package ru.kruvv.primefaces.services;

import java.util.Date;
import java.util.List;

import ru.kruvv.primefaces.models.Book;

/**
 * This is interface for find books
 * @author viktor
 *
 */
public interface BookService {

	public List<Book> findAllBooks(String fio, Date from, Date to);
}
