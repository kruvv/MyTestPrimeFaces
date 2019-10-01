package ru.kruvv.primefaces.services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

//	@ManagedProperty(value = "calend")
//	private CalendarView calendarView;

	public List<Book> getReturnBooks() {
		return returnBooks;
	}

	public void setReturnBooks(List<Book> returnBooks) {
		this.returnBooks = returnBooks;
	}

//	public CalendarView getCalendarView() {
//		return calendarView;
//	}
//
//	public void setCalendarView(CalendarView calendarView) {
//		this.calendarView = calendarView;
//	}

	@Override
	public List<Book> findAllBooks(String fio, Date from, Date to) {

		returnBooks = new ArrayList<>();
		Session session;

		try {
			session = setUp().openSession();
			session.beginTransaction();
			List books;
			if (from == null) {
				books = session.createSQLQuery("select p.* from books as p where user_id=(select c.user_id from users c where " + "fio=" + "'" + fio + "'" + " and createDate<=" + "'" + formatDate(to) + "'" + ")").addEntity(Book.class).list();
			} else {
				books = session
						.createSQLQuery(
								"select p.* from books as p where user_id=(select c.user_id from users c where " + "fio=" + "'" + fio + "'" + " and createDate>=" + "'" + formatDate(from) + "'" + " and createDate<=" + "'" + formatDate(to) + "'" + ")")
						.addEntity(Book.class).list();
			}

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

	public String formatDate(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

}
