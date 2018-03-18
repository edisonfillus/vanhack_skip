package com.skipthedishes.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

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

public class StoreRestServiceTest extends JerseyTest {

	@Override
	public Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(StoreRestService.class);
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
	public void shouldCreateStore() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0).build();

		Response createResponse = target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 201", 201, createResponse.getStatus());
		assertNotNull("Should return Store", createResponse.getEntity());

		Store created = createResponse.readEntity(Store.class);

		// Check if it is all good
		assertEquals(storeToInsert.getName(), created.getName());
		assertEquals(storeToInsert.getAddress(), created.getAddress());
		assertEquals(storeToInsert.getProducts().size(), created.getProducts().size());

	}

	@Test
	public void shouldListStores() {
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

		Response listResponse = target("/api/v1/Store").request(MediaType.APPLICATION_JSON).get();
		
		assertEquals("Should return status 200", 200, listResponse.getStatus());
		assertNotNull("Should return List Store", listResponse.getEntity());
		
		List<Store> listStores = listResponse.readEntity(new GenericType<List<Store>>() {});
		assertEquals(2,listStores.size());

	}

	
	
	@Test
	public void shouldSearchStores() {
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

		
		//Try to search by Skip
		Response searchResponse = target("/api/v1/Store/search/skip").request(MediaType.APPLICATION_JSON).get();
		
		assertEquals("Should return status 200", 200, searchResponse.getStatus());
		assertNotNull("Should return List Store", searchResponse.getEntity());
		
		List<Store> listStores = searchResponse.readEntity(new GenericType<List<Store>>() {});
		assertEquals(1,listStores.size());
		assertEquals("The Skip Restaurant", listStores.get(0).getName());

	}

	
	@Test
	public void shouldGetStoreByID() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0).build();

		Response createResponse = target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));
		
		Store created = createResponse.readEntity(Store.class);

		
		//Try to get by ID
		Response findByIDResponse = target("/api/v1/Store/"+String.valueOf(created.getId())).request(MediaType.APPLICATION_JSON).get();
						
		assertEquals("Should return status 200", 200, findByIDResponse.getStatus());
		assertNotNull("Should return List Store", findByIDResponse.getEntity());
		
		Store storeGetByID = findByIDResponse.readEntity(Store.class);

		assertEquals("The Skip Restaurant", storeGetByID.getName());

	}

	@Test
	public void shouldListProductsFromStore() {
		// Create Store
		Store storeToInsert = new Store.Builder("The Skip Restaurant").atAddress("651 Dundas Street, Toronto")
				.addProduct("Pasta", "Bolognese Pasta", 10.5)
				.addProduct("Roasted Chicken", "A delicious Roasted Chicken with Rice and Vegetables", 15.0).build();

		Response createResponse = target("/api/v1/Store").request()
				.post(Entity.entity(storeToInsert, MediaType.APPLICATION_JSON));
		
		Store created = createResponse.readEntity(Store.class);

		String url = new StringBuilder().append("/api/v1/Store/").append(String.valueOf(created.getId())).append("/products").toString();
		
		//Try to get store products
		Response findProductsFromStoreResponse = target(url).request(MediaType.APPLICATION_JSON).get();
						
		assertEquals("Should return status 200", 200, findProductsFromStoreResponse.getStatus());
		assertNotNull("Should return Producs from Store", findProductsFromStoreResponse.getEntity());
		
		List<Product> listProducts = findProductsFromStoreResponse.readEntity(new GenericType<List<Product>>() {});
				
		
		//Check if returned 2
		assertEquals(2, listProducts.size());

	}


	
	
}
