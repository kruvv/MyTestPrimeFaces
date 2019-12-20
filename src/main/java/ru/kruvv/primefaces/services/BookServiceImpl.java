package ru.kruvv.primefaces.services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kruvv.primefaces.models.Book;
import ru.kruvv.primefaces.util.HibernateUtil;
import ru.kruvv.primefaces.views.MessagesView;

@ManagedBean(name = "bookService")
@SessionScoped
public class BookServiceImpl implements BookService {

	private final static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	private List<Book> allBooks = null;
	MessagesView messagesView = new MessagesView();

	public List<Book> getallBooks() {
		return allBooks;
	}

	public void setallBooks(List<Book> allBooks) {
		this.allBooks = allBooks;
	}

	/**
	 * This method searches for all books found by the user.
	 */
	@Override
	public List<Book> findAllBooks(String fio, Date start, Date end) {

		Session session = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			if (start == null) {
				// If the start date is not specified, then select all books by the mandatory
				// end date.
				SQLQuery requestTo = session.createSQLQuery("select p.* from books as p where user_id=(select c.user_id from users c where fio=:name and createDate<=:endDate)");
				requestTo.setParameter("name", fio);
				requestTo.setParameter("endDate", formatDate(end));
				allBooks = requestTo.addEntity(Book.class).list();
			} else {
				// Otherwise, select from the specified period.
				SQLQuery requestFromTo = session.createSQLQuery("select p.* from books as p where user_id=(select c.user_id from users c where fio=:name and createDate>=:startDate and createDate<=:endDate)");
				requestFromTo.setParameter("name", fio);
				requestFromTo.setParameter("startDate", formatDate(start));
				requestFromTo.setParameter("endDate", formatDate(end));
				allBooks = requestFromTo.addEntity(Book.class).list();
			}

			session.getTransaction().commit();

		} catch (HibernateException e) {
			messagesView.fatal();
			e.printStackTrace();
		} catch (Exception e) {
			messagesView.fatalFindBooks("A serious error occurred while searching for books: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null) {
				HibernateUtil.closeSession();
			}
		}

		return allBooks;
	}

	/**
	 * This method formats the date.
	 * 
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}

	/**
	 * This method edit title book and date.
	 * 
	 * @param event
	 */
	public void onCellEdit(CellEditEvent event) {

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		List<Book> lists = null;
		Session session = null;

		if (oldValue instanceof Date) {
			updateDateBook(oldValue, newValue, session);
		} else {
			updateTitleBook(oldValue, newValue, session);
		}

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			messagesView.warn();
		}
	}

	/**
	 * This method update title book.
	 * 
	 * @param oldValue
	 * @param newValue
	 * @param session
	 */
	private void updateTitleBook(Object oldValue, Object newValue, Session session) {
		List<Book> lists;
		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			int bookId = 0;
			String bookValue = "";
			for (Book book : allBooks) {
				bookValue = book.getTitle();
				if (bookValue.equals(newValue.toString())) {
					bookId = book.getId();
				}
			}

			SQLQuery updateTitle = session.createSQLQuery("select p.* from books as p where p.titleBook=:old");
			updateTitle.setParameter("old", oldValue.toString());
			lists = updateTitle.addEntity(Book.class).list();

			Book updateBook = (Book) session.get(Book.class, bookId);

			updateBook.setTitle(newValue.toString());

			session.getTransaction().commit();

		} catch (HibernateException e) {
			messagesView.fatal();
			e.printStackTrace();
		} catch (Exception e) {
			messagesView.fatalUpdate("Serious error occurred while updating title: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null) {
				HibernateUtil.closeSession();
			}
		}
	}

	/**
	 * This method update date book.
	 * 
	 * @param oldValue
	 * @param newValue
	 * @param session
	 */
	private void updateDateBook(Object oldValue, Object newValue, Session session) {
		List<Book> lists;
		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			int bookId = 0;
			String bookValue;
			for (Book book : allBooks) {
				bookValue = formatDate(book.getDate());
				if (bookValue.equals(formatDate((Date) newValue))) {
					bookId = book.getId();
				}
			}

			SQLQuery updateDate = session.createSQLQuery("select p.* from books as p where p.createDate=:old");
			updateDate.setParameter("old", oldValue);
			lists = updateDate.addEntity(Book.class).list();

			Book updateBook = (Book) session.get(Book.class, bookId);

			updateBook.setDate((Date) newValue);

			session.getTransaction().commit();
		} catch (HibernateException e) {
			messagesView.fatal();
			e.printStackTrace();
		} catch (Exception e) {
			messagesView.fatalUpdate("Serious error occurred while updating date: " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null) {
				HibernateUtil.closeSession();
			}
		}
	}

}
