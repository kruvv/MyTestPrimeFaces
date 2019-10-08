package ru.kruvv.primefaces.services;

import java.util.ArrayList;
import java.util.List;

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

	private List<User> returnUsers;

	public List<User> getReturnUsers() {
		return returnUsers;
	}

	public void setReturnUsers(List<User> returnUsers) {
		this.returnUsers = returnUsers;
	}

	@Override
	public List<User> findUser(String filter) {

		Session session = null;

		try {
			session = HibernateUtil.currentSession();
			session.beginTransaction();

			List<User> findUsers = session.createSQLQuery("select p.* from users as p").addEntity(User.class).list();

			returnUsers = new ArrayList<>();

			if (findUsers == null && findUsers.isEmpty()) {
				return null;
			} else {
				for (User user : findUsers) {
					if (user.toString().toLowerCase().contains(filter.toLowerCase())) {
//						logger.info("Find User: " + user.getFio());
						returnUsers.add(user);
					}
				}
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

		return returnUsers;
	}

	// Method for exit from app
	@Override
	public String logout() {
		// Reset Session
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		return "/index?faces-redirect=true";
	}

	// Method search user
	@Override
	public List<User> completeFio(String user_fio) {
		UserServiceImpl serviceImpl = new UserServiceImpl();

		return serviceImpl.findUser(user_fio);
	}

}
