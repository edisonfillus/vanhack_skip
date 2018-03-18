package com.skipthedishes.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.skipthedishes.dao.interfaces.OrderDAO;
import com.skipthedishes.dto.Order;

public class MySQLOrderDAO implements OrderDAO {

	EntityManager em;

	public MySQLOrderDAO(EntityManager em) {
		this.em = em;
	}
	@Override
	public void create(Order order) {
		em.persist(order);
	}

	@Override
	public Order find(long id) {
		return em.find(Order.class, id);
	}

	@Override
	public List<Order> listByCustomer(long idCustomer) {
		return em.createQuery("SELECT o FROM Order o where o.customer.id = ?",Order.class)
				.setParameter(0, idCustomer)
				.getResultList();
	}

	@Override
	public void truncate() {
		em.createNativeQuery("DELETE FROM Order").executeUpdate();		
	}

}
