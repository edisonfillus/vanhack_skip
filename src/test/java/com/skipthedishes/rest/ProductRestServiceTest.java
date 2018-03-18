package com.skipthedishes.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.skipthedishes.dao.impl.MySQLStoreDAO;
import com.skipthedishes.dao.interfaces.StoreDAO;
import com.skipthedishes.dto.Product;
import com.skipthedishes.dto.Store;

public class ProductRestServiceTest extends JerseyTest {

	@Override
	public Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		Set services = new HashSet();
		services.add(ProductRestService.class);
		services.add(StoreRestService.class);
		return new ResourceConfig(services);
	}

	@Before
	public void beforeTests() {
		EntityManager em = Persistence.createEntityManagerFactory("MySQLPU").createEntityManager();
		StoreDAO storeDAO = new MySQLStoreDAO(em);
		em.getTransaction().begin();
		storeDAO.truncate();
		em.getTransaction().commit();
	}

	@After
	public void afterTests() {
		EntityManager em = Persistence.createEntityManagerFactory("MySQLPU").createEntityManager();
		StoreDAO storeDAO = new MySQLStoreDAO(em);
		em.getTransaction().begin();
		storeDAO.truncate();
		em.getTransaction().commit();
	}

	@Test
	public void shouldCreateProduct() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto").build();

		Response createStoreResponse = target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 201", 201, createStoreResponse.getStatus());
		assertNotNull("Should return Store", createStoreResponse.getEntity());

		Store storeCreated = createStoreResponse.readEntity(Store.class);

		Product productToInsert = new Product(storeCreated.getId(), "Pasta", "Bolognese Pasta", 10.5);

		Response createProductResponse = target("/api/v1/Product").request()
				.post(Entity.entity(productToInsert, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 201", 201, createProductResponse.getStatus());
		assertNotNull("Should return Product", createProductResponse.getEntity());

		Product productInserted = createProductResponse.readEntity(Product.class);

		// Check if it is all good
		assertEquals(productToInsert.getName(), productInserted.getName());
		assertEquals(productToInsert.getDescription(), productInserted.getDescription());
		assertEquals(productToInsert.getPrice(), productInserted.getPrice(), 0.00001);
		assertEquals(productToInsert.getStoreId(), productInserted.getStoreId());

	}

	@Test
	public void shouldFindProductById() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto")
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0).build();

		Response createStoreResponse = target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 201", 201, createStoreResponse.getStatus());
		assertNotNull("Should return Store", createStoreResponse.getEntity());

		Store storeCreated = createStoreResponse.readEntity(Store.class);

		Product productCreated = storeCreated.getProducts().get(0);
		
		// Try to find product
		Response findProductByIdResponse = target("/api/v1/Product/" + productCreated.getId())
				.request(MediaType.APPLICATION_JSON).get();

		assertEquals("Should return status 201", 200, findProductByIdResponse.getStatus());
		assertNotNull("Should return Product", findProductByIdResponse.getEntity());

		Product productInserted = findProductByIdResponse.readEntity(Product.class);
				
		// Check if it is all good
		assertEquals(productCreated.getName(), productInserted.getName());
		assertEquals(productCreated.getDescription(), productInserted.getDescription());
		assertEquals(productCreated.getPrice(), productInserted.getPrice(), 0.00001);
		assertEquals(productCreated.getStoreId(), productInserted.getStoreId());

	}
	
	@Test
	public void shouldListProducts() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0).build();

		// Create Store
		Store storeToInsert2 = new Store.Builder("The Dishes Restaurant").atAddress("621 Dundas Street, Toronto")
				.addProduct("Pizza", "Cheese Pizza", 10.5)
				.addProduct("Roasted Beef", "A delicious Roasted Beef with Rice and Vegetables", 15.0).build();

		target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));
		
		target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert2, MediaType.APPLICATION_JSON));

		Response listResponse = target("/api/v1/Product").request(MediaType.APPLICATION_JSON).get();
		
		assertEquals("Should return status 200", 200, listResponse.getStatus());
		assertNotNull("Should return List Store", listResponse.getEntity());
		
		List<Product> listProducts = listResponse.readEntity(new GenericType<List<Product>>() {});
		assertEquals(4,listProducts.size());

	}

	@Test
	public void shoulSearchProducts() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0).build();

		// Create Store
		Store storeToInsert2 = new Store.Builder("The Dishes Restaurant").atAddress("621 Dundas Street, Toronto")
				.addProduct("Pizza", "Cheese Pizza", 10.5)
				.addProduct("Roasted Beef", "A delicious Roasted Beef with Rice and Vegetables", 15.0).build();

		target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));
		
		target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert2, MediaType.APPLICATION_JSON));

		//Search for Roasted
		Response listResponse = target("/api/v1/Product/search/Roasted").request(MediaType.APPLICATION_JSON).get();
		
		assertEquals("Should return status 200", 200, listResponse.getStatus());
		assertNotNull("Should return List Store", listResponse.getEntity());
		
		//Should find 2
		List<Product> listProducts = listResponse.readEntity(new GenericType<List<Product>>() {});
		assertEquals(2,listProducts.size());

	}

	
	
}
