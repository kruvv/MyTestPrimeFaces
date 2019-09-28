package ru.kruvv.primefaces.services;

import java.util.List;

import ru.kruvv.primefaces.models.Book;

public interface BookService {

	public List<Book> getAllBooks(String fio);
}
