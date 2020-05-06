package ru.kruvv.primefaces.models;

import java.util.List;
import java.util.Objects;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * This is simple object User.
 * 
 * @author viktor
 *
 */
@ManagedBean(name = "User")
@SessionScoped
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;

	@Column(name = "fio")
	private String fio;

	@Column(name = "login")
	private String login;

	@Column(name = "password")
	private String password;
	
	
	@OneToMany(mappedBy = "user")
	private List<Book> books;

	public User() {
	}

	

	public User(int id, String fio, String login, String password) {
		this.id = id;
		this.fio = fio;
		this.login = login;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFio() {
		return fio;
	}

	public void setFio(String fio) {
		this.fio = fio;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}



	@Override
	public String toString() {
		return fio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fio, id, login, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(fio, other.fio) && id == other.id && Objects.equals(login, other.login) && Objects.equals(password, other.password);
	}

}
