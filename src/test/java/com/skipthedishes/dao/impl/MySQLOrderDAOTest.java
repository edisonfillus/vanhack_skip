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
import com.skipthedishes.dao.interfaces.OrderDAO;
import com.skipthedishes.dao.interfaces.StoreDAO;
import com.skipthedishes.dto.Customer;
import com.skipthedishes.dto.Order;
import com.skipthedishes.dto.Store;

public class MySQLOrderDAOTest {

	private EntityManagerFactory sessionFactory;
	private EntityManager em;
	private OrderDAO orderDAO;
	private CustomerDAO customerDAO;
	private StoreDAO storeDAO;
	
	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("MySQLPU");
		em = sessionFactory.createEntityManager();
		orderDAO = new MySQLOrderDAO(em);
		customerDAO = new MySQLCustomerDAO(em);
		storeDAO = new MySQLStoreDAO(em);
		em.getTransaction().begin();
	}

	@After
	public void afterTests() {
		em.getTransaction().rollback(); // Undo everything
		em.close();
	}

	@Test
	public void shouldCreateOrder() {
		// Create Customer
		Customer customerToInsert = new Customer();
		customerToInsert.setName("Steve Jobs");
		customerToInsert.setEmail("customer@skipthedishes.com");
		customerToInsert.setAddress("651 York Street, Toronto");
		customerToInsert.setPassword("secret");
		customerToInsert.setCreation(Calendar.getInstance().getTime());

		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant")
				.atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0)
				.build();

			
		
		Order orderToInsert = new Order.Builder().fromStore(storeToInsert).toCustomer(customerToInsert)
				.contact("Myself").deliveryAtAddress("My address").addItem(storeToInsert.getProducts().get(0), 1, 10.5).build();
				
		
		// Persist on DataBase
		customerDAO.create(customerToInsert);
		storeDAO.create(storeToInsert);
		orderDAO.create(orderToInsert);

						
		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find customer
		Order inserted = orderDAO.find(orderToInsert.getId());

		// Check if it is all good
		assertNotNull(inserted);

	}

}
