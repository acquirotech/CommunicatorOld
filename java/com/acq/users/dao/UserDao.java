package com.acq.users.dao;

import com.acq.users.model.User;

public interface UserDao {

	User findByUserName(String username);
}