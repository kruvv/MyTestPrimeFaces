package ru.kruvv.primefaces.services;

import java.util.List;

import ru.kruvv.primefaces.models.User;

public interface UserService {

	public List<User> findUser(String filter);

	public String logout();

	public List<User> completeFio(String user_fio);

}
