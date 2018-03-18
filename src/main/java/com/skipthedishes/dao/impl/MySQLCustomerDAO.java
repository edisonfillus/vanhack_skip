package com.skipthedishes.dao.impl;

import javax.persistence.EntityManager;

import com.skipthedishes.dao.interfaces.CustomerDAO;
import com.skipthedishes.dto.Customer;

public class MySQLCustomerDAO implements CustomerDAO {
	
	EntityManager em;

	public MySQLCustomerDAO(EntityManager em) {
		this.em = em;
	}
	
	public void create(Customer customer) {
		em.persist(customer);
	}

	public Customer find(long id) {
		return em.find(Customer.class, id);
	}

	public void truncate() {
		em.createNativeQuery("DELETE FROM Customer").executeUpdate();
	}

	@Override
	public Customer auth(String email, String password) {
		return em.createQuery("SELECT c FROM Customer c where email = ? and password = ?",Customer.class)
				.setParameter(0, email)
				.setParameter(1, password)
				.getSingleResult();
	}
	
	
	
	
}
