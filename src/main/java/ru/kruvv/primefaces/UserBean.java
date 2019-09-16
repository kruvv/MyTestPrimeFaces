package ru.kruvv.primefaces;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

@ManagedBean(name = "User")
@SessionScoped
public class UserBean {

	private int id;
	private String fio;
	private String login;
	private String password;

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

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index?faces-redirect=true";
	}

}
