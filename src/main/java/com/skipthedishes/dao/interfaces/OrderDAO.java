package com.skipthedishes.dao.interfaces;

import java.util.List;

import com.skipthedishes.dto.Order;

public interface OrderDAO {
	void create(Order order);
	Order find(long id);
	List<Order> listByCustomer(long idCustomer);
	void truncate();

}
