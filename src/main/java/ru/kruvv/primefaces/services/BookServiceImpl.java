package ru.kruvv.primefaces.services;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.primefaces.event.CellEditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kruvv.primefaces.models.Book;
import ru.kruvv.primefaces.util.HibernateUtil;

@ManagedBean(name = "bookService")
@ApplicationScoped
//@ViewScoped
//@SessionScoped
public class BookServiceImpl implements BookService {

	private final static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	private List<Book> allBooks;

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
	public List<Book> findAllBooks(String fio, Date from, Date to) {

		Session session = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			if (from == null) {
				// если начальная дата не указанна, то выбераем все книги по обязательной
				// конечной дате.

				Query requestTo = session.createSQLQuery("select p.* from books as p where user_id=(select c.user_id from users c where fio=:name and createDate<=:endDate)");
				requestTo.setParameter("name", fio);
				requestTo.setParameter("endDate", formatDate(to));
				allBooks = ((SQLQuery) requestTo).addEntity(Book.class).list();
			} else {
				// иначе выбираем из указанного периода.
				Query requestFromTo = session.createSQLQuery("select p.* from books as p where user_id=(select c.user_id from users c where fio=:name and createDate>=:startDate and createDate<=:endDate)");
				requestFromTo.setParameter("name", fio);
				requestFromTo.setParameter("startDate", formatDate(from));
				requestFromTo.setParameter("endDate", formatDate(to));
				allBooks = ((SQLQuery) requestFromTo).addEntity(Book.class).list();
			}

			session.getTransaction().commit();

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
	 * This method update title book and date.
	 * 
	 * @param event
	 */
	public void onCellEdit(CellEditEvent event) {

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		Session session = null;
		int id = 0;

		if (oldValue instanceof Date) {

			logger.info("This is oldValue date: " + oldValue.toString());

			try {
				session = HibernateUtil.currentSession();
				session.beginTransaction();

				Query editDate = session.createSQLQuery("select p.* books as p where p.createDate=:old");
				editDate.setParameter("old", oldValue);
				List<Book> lists = ((SQLQuery) editDate).addEntity(Book.class).list();

				for (Book book : lists) {
					if (book != null) {
						logger.info("This is old date book: " + book.toString());
						id = book.getId();
					}
				}

				Book updateBook = (Book) session.get(Book.class, id);

				updateBook.setDate((Date) newValue);

				session.getTransaction().commit();
			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (session != null) {
					HibernateUtil.closeSession();
				}
			}

		} else {
			try {
				session = HibernateUtil.currentSession();
				session.beginTransaction();

				Query editTitle = session.createSQLQuery("select p.* from books as p where p.titleBook=:old");
				editTitle.setParameter("old", oldValue.toString());
				List<Book> lists = ((SQLQuery) editTitle).addEntity(Book.class).list();

				for (Book book : lists) {
					if (book != null) {
						logger.info("This is old name book: " + book.toString());
						id = book.getId();
					}
				}

				Book updateBook = (Book) session.get(Book.class, id);

				updateBook.setTitle(newValue.toString());

				session.getTransaction().commit();

			} catch (HibernateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (session != null) {
					HibernateUtil.closeSession();
				}
			}

		}

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

}
