package ru.kruvv.primefaces.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.kruvv.primefaces.models.User;
import ru.kruvv.primefaces.util.HibernateUtil;

@ManagedBean(name = "userService", eager = true)
@ApplicationScoped
public class UserServiceImpl implements UserService {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

	// Method for exit from app
	@Override
	public String logout() {
		// Reset Session
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/index?faces-redirect=true";
	}

}
