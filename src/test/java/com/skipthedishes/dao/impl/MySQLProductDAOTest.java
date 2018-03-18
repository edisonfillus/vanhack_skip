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

import com.skipthedishes.dto.Product;
import com.skipthedishes.dto.Store;

public class MySQLProductDAOTest {
	
	private EntityManagerFactory sessionFactory;
	private EntityManager em;
	private MySQLProductDAO productDAO;
	private MySQLStoreDAO storeDAO;

	@Before
	public void beforeTests() {
		sessionFactory = Persistence.createEntityManagerFactory("MySQLPU");
		em = sessionFactory.createEntityManager();
		storeDAO = new MySQLStoreDAO(em);
		productDAO = new MySQLProductDAO(em);
		em.getTransaction().begin();
	}

	@After
	public void afterTests() {
		em.getTransaction().rollback(); // Undo everything
		em.close();
	}

	@Test
	public void shouldFindAllProductsStore() {
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

		// Find products
		List<Product> storeProducts = productDAO.listByStore(storeToInsert.getId());
		
		
		//Check if it is bring just The Skip Restaurant
		assertNotNull(storeProducts);
		assertEquals(2, storeProducts.size());
 
	}
	
	@Test
	public void shouldFindAllProducts() {
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

		// Find products
		List<Product> allProducts = productDAO.listAll();
		
		
		//Check if it is bring just The Skip Restaurant
		assertNotNull(allProducts);
		assertEquals(4, allProducts.size());
 
	}
	
	
	@Test
	public void shouldAddProductsToStore() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant")
				.atAddress("651 Dundas Street, Toronto")
				.build();
		
		// Persist Store on DataBase
		storeDAO.create(storeToInsert);

		// Force hibernate to send command to database and clear context
		em.flush();
		em.clear();
		
		//Shouldn't have any products
		List<Product> noProductsInserted = productDAO.listByStore(storeToInsert.getId());
		assertEquals(0,noProductsInserted.size());

		Product product1 = new Product(storeToInsert.getId(),"Pasta", "Bolognese Pasta", 10.5);
		Product product2 = new Product(storeToInsert.getId(),"Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0);
		
		
		productDAO.create(product1);
		productDAO.create(product2);
		
		// Find products from store to check
		List<Product> productsInserted = productDAO.listByStore(storeToInsert.getId());
		assertNotNull(productsInserted);
		assertEquals(2, productsInserted.size());

	}
	

	@Test
	public void shouldSearchProducts() {
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

		// Find products
		List<Product> searchedProducts = productDAO.search("Roasted");
				
		//Check if it is bring just The Skip Restaurant
		assertNotNull(searchedProducts);
		assertEquals(2, searchedProducts.size());
 
	}
	
	
	
}
