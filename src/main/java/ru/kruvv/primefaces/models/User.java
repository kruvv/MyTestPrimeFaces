package ru.kruvv.primefaces.models;

import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "User")
@SessionScoped
//@ApplicationScoped
@Entity
@Table(name = "users")
public class User {

	private final static Logger logger = LoggerFactory.getLogger(User.class);

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

	@Override
	public String toString() {
		return fio;
	}

	public void onItemSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
	}

	/*
	 * Метод простейшей авторизации. Выполняется проверка имени и пароля
	 * пользователя. Результат проверки - наименование страницы перехода. В тестовом
	 * режиме для входа поля логин и пароль оставить пустым
	 */
	public String checkLogin() {
		if (login.equalsIgnoreCase("") && password.equalsIgnoreCase("")) {

			return "views/home?faces-redirect=true";
		} else {
			return "index?faces-redirect=true";
		}
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
