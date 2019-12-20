package ru.kruvv.primefaces.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kruvv.primefaces.models.User;
import ru.kruvv.primefaces.util.HibernateUtil;
import ru.kruvv.primefaces.views.MessagesView;

@ManagedBean(name = "userService", eager = true)
@SessionScoped
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	MessagesView messagesView = new MessagesView();

	private List<User> findUsers;

	public List<User> getfindUsers() {
		return findUsers;
	}

	public void setfindUsers(List<User> findUsers) {
		this.findUsers = findUsers;
	}

	/**
	 * This method searches for the user in the search bar.
	 */
	@Override
	public List<User> findUser(String filter) {

		Session session = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();
			findUsers = new ArrayList<>();
			List<User> users = session.createSQLQuery("select p.* from users as p").addEntity(User.class).list();

			if (users.isEmpty()) {
				return null;
			} else {
				findUsers = users.stream().filter(s -> s.toString().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
			}

			session.getTransaction().commit();

		} catch (HibernateException e) {
			messagesView.fatal();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				HibernateUtil.closeSession();
			}
		}

		return findUsers;
	}

	/*
	 * This method simple authorization.
	 */
	public String checkLogin(String login, String password) {

		Session session = null;
		List<User> users = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			SQLQuery query = session.createSQLQuery("select u.* from users as u where login=:userLogin");
			query.setParameter("userLogin", login);
			users = query.addEntity(User.class).list();

			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				HibernateUtil.closeSession();
			}
		}

		for (User user : users) {
			if (login.equals(user.getLogin()) & password.equals(user.getPassword())) {

				return "views/home?faces-redirect=true";
			} else {
				return "index?faces-redirect=true";
			}
		}
		return "Sorry Опаньки!!!";

	}

	/**
	 * Method for exit from app.
	 */
	@Override
	public String logout() {
		// Reset Session
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/index?faces-redirect=true";
	}

}
