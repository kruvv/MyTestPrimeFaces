package ru.kruvv.primefaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Restrictions;

import ru.kruvv.primefaces.models.Book;
import ru.kruvv.primefaces.models.User;
import ru.kruvv.primefaces.services.BookService;
import ru.kruvv.primefaces.services.BookServiceImpl;
import ru.kruvv.primefaces.util.HibernateUtil;

public class Main {

	public static void main(String[] args) {
		String fio = "tom smith";
		Date date = new Main().formaterDate("01.01.2012"); 
		BookServiceImpl bookService = new BookServiceImpl();
		Session session = HibernateUtil.currentSession();
		List<Book> books = null;		
		
		try {
			session.beginTransaction();			
			
			Criteria criteria = session.createCriteria(Book.class, "book");
			criteria.createCriteria("book.user", "u");
			criteria.add(Restrictions.eq("u.fio", fio));		
			criteria.add(Restrictions.le("book.date", date));
			books = criteria.list();
			
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}finally{
			if(session != null) {
				session.close();
				System.out.println("This session was clossed!!!");
			}
		}
		
		books.stream().forEach(System.out::println);

	}
	
	public Date formaterDate(String date) {
		SimpleDateFormat siFormat = new SimpleDateFormat("dd.MM.yyyy");
		Date parsingDate = null;
		try {
			parsingDate = siFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parsingDate;
	}

}
