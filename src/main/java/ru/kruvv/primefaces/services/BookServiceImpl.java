package ru.kruvv.primefaces.services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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
				Criteria criteria = session.createCriteria(Book.class, "book");
				criteria.createCriteria("book.user", "u");
				criteria.add(Restrictions.eq("u.fio", fio));		
				criteria.add(Restrictions.le("book.date", end));
				allBooks = criteria.list();
				
			} else {
				// Otherwise, select from the specified period.				
				Criteria criteria = session.createCriteria(Book.class, "book");
				criteria.createCriteria("book.user", "u");
				criteria.add(Restrictions.eq("u.fio", fio));		
				criteria.add(Restrictions.ge("book.date", start));
				criteria.add(Restrictions.le("book.date", end));
				allBooks = criteria.list();
				
			}

			session.getTransaction().commit();

		} catch (HibernateException e) {
			messagesView.fatal(e.getMessage());
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

		if (oldValue instanceof Date) {
			updateDateBook(oldValue, newValue);
		} else {
			updateTitleBook(oldValue, newValue);
		}

		onCellEditMessages(oldValue, newValue);
	}

	/**
	 * This method displays a message after editing title or date in a table.
	 * 
	 * @param oldValue
	 * @param newValue
	 */
	public void onCellEditMessages(Object oldValue, Object newValue) {
		if (newValue != null && !newValue.equals(oldValue) && !(newValue instanceof Date))  {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + formatDate((Date)newValue));
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	/**
	 * This method update title book.
	 * 
	 * @param oldValue
	 * @param newValue
	 * @param session
	 */
	public void updateTitleBook(Object oldValue, Object newValue) {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			int bookId = 0;
			String bookTitle = "";
			for (Book book : allBooks) {
				bookTitle = book.getTitle();
				if (bookTitle.equals(newValue.toString())) {
					bookId = book.getId();
				}
			}

			Book updateTitleBook = (Book) session.get(Book.class, bookId);
			updateTitleBook.setTitle(newValue.toString());			
			session.update(updateTitleBook);
			session.getTransaction().commit();

		} catch (HibernateException e) {
			messagesView.fatal(e.getMessage());
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
	public void updateDateBook(Object oldValue, Object newValue) {
		Session session = null;
		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			int bookId = 0;
			String bookDate;
			for (Book book : allBooks) {
				bookDate = formatDate(book.getDate());
				if (bookDate.equals(formatDate((Date) newValue))) {
					bookId = book.getId();
				}
			}

			Book updateDateBook = (Book) session.get(Book.class, bookId);
			updateDateBook.setDate((Date) newValue);
			session.update(updateDateBook);
			session.getTransaction().commit();
			
		} catch (HibernateException e) {
			messagesView.fatal(e.getMessage());
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
