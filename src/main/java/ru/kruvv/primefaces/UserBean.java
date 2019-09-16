package ru.kruvv.primefaces;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.primefaces.event.SelectEvent;

@ManagedBean(name = "User")
@SessionScoped
@Entity
@Table(name = "user")
public class UserBean {

	@Id
	@Column(name = "user_id")
	private int id;

	@Column(name = "fio")
	private String fio;

	@Column(name = "login")
	private String login;

	@Column(name = "password")
	private String password;

	public UserBean() {
	}

	public UserBean(int id, String fio, String login, String password) {
		this.id = id;
		this.fio = fio;
		this.login = login;
		this.password = password;
	}

	public long getId() {
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

	public void onItemSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
	}

	/*
	 * Метод простейшей авторизации. Выполняется проверка имени и пароля
	 * пользователя. Результат проверки - наименование страницы перехода
	 */
	public String checkLogin() {
		if (login.equalsIgnoreCase("test") && password.equalsIgnoreCase("test")) {
			return "views/home?faces-redirect=true";
		} else {

			return "index?faces-redirect=true";
		}
	}

	// Метод для выхода из приложения
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/index?faces-redirect=true";
	}

	public List<String> completeFio(String query) {
		List<String> results = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}

		return results;
	}

}
