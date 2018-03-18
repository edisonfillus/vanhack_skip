package com.skipthedishes.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.skipthedishes.dto.Store;

public class MySQLStoreDAOTest {
	
	private EntityManagerFactory sessionFactory;
	private EntityManager em;
	private MySQLStoreDAO storeDAO;

	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("MySQLPU");
		em = sessionFactory.createEntityManager();
		storeDAO = new MySQLStoreDAO(em);
		em.getTransaction().begin();
	}

	@After
	public void afterTests() {
		em.getTransaction().rollback(); // Undo everything
		em.close();
	}

	@Test
	public void shouldCreateStore() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant")
				.atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0)
				.build();

		// Persist on DataBase
		storeDAO.create(storeToInsert);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find store
		Store inserted = storeDAO.find(storeToInsert.getId());
		
		
		//Check if it is all good
		assertNotNull(inserted);
		assertEquals(storeToInsert.getName(), inserted.getName());
		assertEquals(storeToInsert.getAddress(), inserted.getAddress());
		assertEquals(storeToInsert.getProducts().size(), inserted.getProducts().size());

	}
	
	@Test
	public void shouldFindStoreByID() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant")
				.atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0)
				.build();

		// Persist on DataBase
		storeDAO.create(storeToInsert);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find store
		Store inserted = storeDAO.find(storeToInsert.getId());
		
		
		//Check if it is all good
		assertNotNull(inserted);
		assertEquals(storeToInsert.getName(), inserted.getName());
		assertEquals(storeToInsert.getAddress(), inserted.getAddress());
		assertEquals(storeToInsert.getProducts().size(), inserted.getProducts().size());

	}

	
	
	@Test
	public void shouldSearchStoreByText() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant")
				.atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0)
				.build();
		
		// Create Store
		Store storeToInsert2 = new Store.Builder("The Dishes Restaurant")
				.atAddress("621 Dundas Street, Toronto")
				.addProduct("Pizza", "Cheese Pizza", 10.5)
				.addProduct("Roasted Beef", "A delicious Roasted Beef with Rice and Vegetables", 15.0)
				.build();

		// Persist on DataBase
		storeDAO.create(storeToInsert);
		storeDAO.create(storeToInsert2);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find store
		List<Store> searched = storeDAO.search("Skip");
		
		
		//Check if it is bring just The Skip Restaurant
		assertNotNull(searched);
		assertEquals(1, searched.size());
		assertEquals(storeToInsert.getName(), searched.get(0).getName());
 
	}
	
	@Test
	public void shouldListAllStore() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant")
				.atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0)
				.build();
		
		// Create Store
		Store storeToInsert2 = new Store.Builder("The Dishes Restaurant")
				.atAddress("621 Dundas Street, Toronto")
				.addProduct("Pizza", "Cheese Pizza", 10.5)
				.addProduct("Roasted Beef", "A delicious Roasted Beef with Rice and Vegetables", 15.0)
				.build();

		// Persist on DataBase
		storeDAO.create(storeToInsert);
		storeDAO.create(storeToInsert2);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();

		// Find store
		List<Store> searched = storeDAO.listAll();
		
		//Check if it is bring just The Skip Restaurant
		assertNotNull(searched);
		assertEquals(2, searched.size());
	 
	}


	
	
}
