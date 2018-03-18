package com.skipthedishes.rest;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.skipthedishes.dao.impl.MySQLCustomerDAO;
import com.skipthedishes.dao.interfaces.CustomerDAO;
import com.skipthedishes.dto.Customer;

public class CustomerRestServiceTest  extends JerseyTest {

	@Override
	public Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(CustomerRestService.class);
	}

	@Before
	public void beforeTests() {
		EntityManager em = Persistence.createEntityManagerFactory("MySQLPU").createEntityManager();
		CustomerDAO customerDAO = new MySQLCustomerDAO(em);
		em.getTransaction().begin();
		customerDAO.truncate();
		em.getTransaction().commit();
	}
	
	@After
	public void afterTests() {
		EntityManager em = Persistence.createEntityManagerFactory("MySQLPU").createEntityManager();
		CustomerDAO customerDAO = new MySQLCustomerDAO(em);
		em.getTransaction().begin();
		customerDAO.truncate();
		em.getTransaction().commit();
	}

	@Test
	public void testCreate() {
		Customer customerToCreate = new Customer();
		customerToCreate.setName("Steve Jobs");
		customerToCreate.setEmail("customer@skipthedishes.com");
		customerToCreate.setAddress("651 York Street, Toronto");
		customerToCreate.setPassword("secret");
		customerToCreate.setCreation(Calendar.getInstance().getTime());
		
		Response createResponse = target("/api/v1/Customer").request()
				.post(Entity.entity(customerToCreate, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 201", 201, createResponse.getStatus());
		assertNotNull("Should return Customer", createResponse.getEntity());

		Customer created = createResponse.readEntity(Customer.class);
		
		assertEquals(customerToCreate.getName(), created.getName());
		assertEquals(customerToCreate.getEmail(), created.getEmail());
		assertEquals(customerToCreate.getAddress(), created.getAddress());
		assertEquals(customerToCreate.getCreation().getTime(), created.getCreation().getTime());
		assertEquals(customerToCreate.getPassword(), created.getPassword());
		
	}
	
	
	@Test
	public void testCustomerLogin() {
		//Create Customer
		Customer customerToLogin = new Customer();
		customerToLogin.setName("Steve Jobs");
		customerToLogin.setEmail("customer@skipthedishes.com");
		customerToLogin.setAddress("651 York Street, Toronto");
		customerToLogin.setPassword("secret");
		customerToLogin.setCreation(Calendar.getInstance().getTime());
		
		Response createResponse = target("/api/v1/Customer").request()
				.post(Entity.entity(customerToLogin, MediaType.APPLICATION_JSON));

		assertEquals("Should return status 201", 201, createResponse.getStatus());
		assertNotNull("Should return Customer", createResponse.getEntity());

		//Try to login
		
		MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();
  	    parameters.add("email", customerToLogin.getEmail());
		parameters.add("password", customerToLogin.getPassword());
		Response loginResponse = target("/api/v1/Customer/auth").request().post(Entity.form(parameters));
		
		assertEquals("Should return status 200", 200, loginResponse.getStatus());
		
		
	}

}
