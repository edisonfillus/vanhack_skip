package com.skipthedishes.rest;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.skipthedishes.dao.impl.MySQLProductDAO;
import com.skipthedishes.dao.interfaces.ProductDAO;
import com.skipthedishes.dto.Product;

@Path("/api/v1/Product")
public class ProductRestService {

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Product product, @Context UriInfo uriInfo) {
		
		//Persist
		EntityManager em = getEntityManager();
    	ProductDAO dao = new MySQLProductDAO(em);
    	em.getTransaction().begin();
    	dao.create(product);
    	em.getTransaction().commit();
    	em.close();
    	
    	//Return
    	URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
   	    return Response.created(uri).entity(product).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		EntityManager em = getEntityManager();
    	ProductDAO dao = new MySQLProductDAO(em);
    	List<Product> products = dao.listAll();
    	em.close(); 	
    	if(products!=null && !products.isEmpty()) {
    		return Response.ok(products).build(); //Search OK
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}

	@GET
	@Path("/search/{searchText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("searchText") String searchText) {
		EntityManager em = getEntityManager();
    	ProductDAO dao = new MySQLProductDAO(em);
    	List<Product> products = dao.search(searchText);
    	em.close(); 	
    	if(products!=null && !products.isEmpty()) {
    		return Response.ok(products).build(); //Search OK
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}
	
	@GET
	@Path("/{productId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("productId") long productId) {
		EntityManager em = getEntityManager();
    	ProductDAO dao = new MySQLProductDAO(em);
    	Product product = dao.find(productId);
    	em.close(); 	
    	if(product!=null) {
    		return Response.ok(product).build(); //Found
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}
	
	private EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySQLPU");
		return emf.createEntityManager();
	}
	
}
