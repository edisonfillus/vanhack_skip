package com.skipthedishes.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.skipthedishes.dao.interfaces.CustomerDAO;
import com.skipthedishes.dto.Customer;

public class MySQLCustomerDAOTest {

	private EntityManagerFactory sessionFactory;
	private EntityManager em;
	private CustomerDAO customerDAO;

	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("MySQLPU");
		em = sessionFactory.createEntityManager();
		customerDAO = new MySQLCustomerDAO(em);
		em.getTransaction().begin();
	}

	@After
	public void afterTests() {
		em.getTransaction().rollback(); // Undo everything
		em.close();
	}

	@Test
	public void shouldCreateCustomer() {
		// Create Customer
		Customer customerToInsert = new Customer();
		customerToInsert.setName("Steve Jobs");
		customerToInsert.setEmail("customer@skipthedishes.com");
		customerToInsert.setAddress("651 York Street, Toronto");
		customerToInsert.setPassword("secret");
		customerToInsert.setCreation(Calendar.getInstance().getTime());
		
		// Persist on DataBase
		customerDAO.create(customerToInsert);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find customer
		Customer inserted = customerDAO.find(customerToInsert.getId());
		
		
		//Check if it is all good
		assertNotNull(inserted);
		assertEquals(customerToInsert.getName(), inserted.getName());
		assertEquals(customerToInsert.getEmail(), inserted.getEmail());
		assertEquals(customerToInsert.getAddress(), inserted.getAddress());
		assertEquals(customerToInsert.getCreation().getTime(), inserted.getCreation().getTime());
		assertEquals(customerToInsert.getPassword(), inserted.getPassword());

	}
	
	@Test
	public void shouldLogin() {
		// Create Customer
		Customer customerToLogin = new Customer();
		customerToLogin.setName("Steve Jobs");
		customerToLogin.setEmail("customer@skipthedishes.com");
		customerToLogin.setAddress("651 York Street, Toronto");
		customerToLogin.setPassword("secret");
		customerToLogin.setCreation(Calendar.getInstance().getTime());
		
		// Persist on DataBase
		customerDAO.create(customerToLogin);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find customer
		Customer customerLogged = customerDAO.auth(customerToLogin.getEmail(), customerToLogin.getPassword());
		
		//Check if it is all good
		assertNotNull(customerLogged);
		assertEquals(customerToLogin.getEmail(), customerLogged.getEmail());
		assertEquals(customerToLogin.getPassword(), customerLogged.getPassword());
		
	}
	
	

	
	
}
