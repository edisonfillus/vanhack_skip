package com.skipthedishes.dao.interfaces;

import com.skipthedishes.dto.Customer;

public interface CustomerDAO {
	void create(Customer customer);
	Customer find(long id);
	void truncate();
	Customer auth(String email, String password);
}
