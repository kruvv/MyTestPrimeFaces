package ru.kruvv.primefaces.services;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kruvv.primefaces.models.Book;

@ManagedBean(name = "bookService")
@ApplicationScoped
public class BookServiceImpl implements BookService {

	private final static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	private List<Book> returnBooks;

	public List<Book> getReturnBooks() {
		return returnBooks;
	}

	public void setReturnBooks(List<Book> returnBooks) {
		this.returnBooks = returnBooks;
	}

	@Override
	public List<Book> getAllBooks(String fio) {

		returnBooks = new ArrayList<>();
		Session session;

		try {
			session = setUp().openSession();
			session.beginTransaction();
			List books = session.createSQLQuery("select p.* from books as p where user_id=(select c.user_id from users c where fio=" + "'" + fio + "')").addEntity(Book.class).list();
			if (books == null || books.isEmpty()) {
				return null;
			}
			for (Book book : (List<Book>) books) {
				if (book != null && book instanceof Book) {
					logger.info(book.toString());
					returnBooks.add(book);
				}
			}
			session.getTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnBooks;
	}

	protected SessionFactory setUp() throws Exception {
		// A SessionFactory is set up once for an application
		SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml") // configures settings from hibernate.cfg.xml
				.buildSessionFactory();
		return sessionFactory;
	}

	public List<Book> completeBooks(String fio) {
//		BookServiceImpl service = new BookServiceImpl();
		List<Book> booksUser = getAllBooks(fio);

		return booksUser;
	}

}
