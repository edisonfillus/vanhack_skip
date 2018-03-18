package com.skipthedishes.rest;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.skipthedishes.dao.impl.MySQLCustomerDAO;
import com.skipthedishes.dao.interfaces.CustomerDAO;
import com.skipthedishes.dto.Customer;

@Path("/api/v1/Customer")
public class CustomerRestService {

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Customer customer, @Context UriInfo uriInfo) {
		EntityManager em = getEntityManager();
    	CustomerDAO dao = new MySQLCustomerDAO(em);
    	em.getTransaction().begin();
    	dao.create(customer);
    	em.getTransaction().commit();
    	em.close();
    	URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(customer.getId())).build();
   	    return Response.created(uri).entity(customer).build();
	}
	

	@POST
	@Path("/auth")
	public Response login(@FormParam("email") String email, @FormParam("password") String password) {
		EntityManager em = getEntityManager();
    	CustomerDAO dao = new MySQLCustomerDAO(em);
    	Customer customer = dao.auth(email, password);
    	em.close(); 	
    	if(customer!=null) {
    		return Response.ok().build(); //Login OK
    	} else {
    		return Response.status(400).build(); //Login fail
    	}
	}
	

	
	

	private EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySQLPU");
		return emf.createEntityManager();
	}

}