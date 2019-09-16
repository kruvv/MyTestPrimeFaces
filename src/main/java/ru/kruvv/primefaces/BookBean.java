package ru.kruvv.primefaces;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@ManagedBean(name = "Book")
@SessionScoped
@Entity
@Table(name = "books")
public class BookBean {

	@Id
	@Column(name = "book_id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "date")
	private Date date;

//	ToDo
//	private BookUser bookUser;

	public BookBean() {
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

}
