package ru.kruvv.primefaces.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kruvv.primefaces.models.User;
import ru.kruvv.primefaces.util.HibernateUtil;
import ru.kruvv.primefaces.views.MessagesView;

@ManagedBean(name = "userService", eager = true)
@ViewScoped
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
	 * This method searches for all users in the search bar using autocomplete.
	 */
	@Override
	public List<User> findUser(String filter) {

		Session session = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();
			findUsers = new ArrayList<>();
			
			Criteria criteria = session.createCriteria(User.class);
			List<User> users = criteria.list();

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
	@Override
	public String checkLogin(String login, String password) {

		Session session = null;
		List<User> users = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();
			
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("login", login));
			users = criteria.list();

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
