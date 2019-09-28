package ru.kruvv.primefaces.models;

import java.util.Date;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "Book")
@SessionScoped
//@ApplicationScoped
@Entity
@Table(name = "books")
public class Book {

	private final static Logger logger = LoggerFactory.getLogger(Book.class);

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private int id;

	@Column(name = "titleBook")
	private String title;

	@Temporal(TemporalType.DATE)
	@Column(name = "createDate")
	private Date date;

	public Book() {
	}

	public Book(int id, String title, Date date) {
		this.id = id;
		this.title = title;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", date=" + date + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return id == other.id && Objects.equals(title, other.title);
	}

}
