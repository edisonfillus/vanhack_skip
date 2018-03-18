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
import com.skipthedishes.dao.impl.MySQLStoreDAO;
import com.skipthedishes.dao.interfaces.ProductDAO;
import com.skipthedishes.dao.interfaces.StoreDAO;
import com.skipthedishes.dto.Product;
import com.skipthedishes.dto.Store;

@Path("/api/v1/Store")
public class StoreRestService {

	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Store store, @Context UriInfo uriInfo) {
		//Set the store on products
		store.getProducts().forEach(p -> p.setStore(store));
		
		//Persist
		EntityManager em = getEntityManager();
    	StoreDAO dao = new MySQLStoreDAO(em);
    	em.getTransaction().begin();
    	dao.create(store);
    	em.getTransaction().commit();
    	em.close();
    	
    	//Return
    	URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(store.getId())).build();
   	    return Response.created(uri).entity(store).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		EntityManager em = getEntityManager();
    	StoreDAO dao = new MySQLStoreDAO(em);
    	
    	List<Store> stores = dao.listAll();
    	
    	//Get store products (Lazy Loading) before closing connection) 
    	stores.forEach(s->s.getProducts().size());
    	    	
    	em.close(); 	
    	if(stores!=null && !stores.isEmpty()) {
    		return Response.ok(stores).build(); //Search OK
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}

	@GET
	@Path("/search/{searchText}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("searchText") String searchText) {
		EntityManager em = getEntityManager();
    	StoreDAO dao = new MySQLStoreDAO(em);
    	List<Store> stores = dao.search(searchText);
    	
    	//Get store products (Lazy Loading) before closing connection) 
    	stores.forEach(s->s.getProducts().size());
    	
    	em.close(); 	
    	if(stores!=null && !stores.isEmpty()) {
    		return Response.ok(stores).build(); //Search OK
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}
	
	@GET
	@Path("/{storeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@PathParam("storeId") long storeId) {
		EntityManager em = getEntityManager();
    	StoreDAO dao = new MySQLStoreDAO(em);
    	Store store = dao.find(storeId);
    	
    	if(store!=null) {
    		//Get store products (Lazy Loading) before closing connection
    		store.getProducts().size();
    	}
    	
    	em.close(); 	
    	if(store!=null) {
    		return Response.ok(store).build(); //Found
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}
	
	@GET
	@Path("/{storeId}/products")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listProductsFromStore(@PathParam("storeId") long storeId) {
		EntityManager em = getEntityManager();
		ProductDAO dao = new MySQLProductDAO(em);
    	List<Product> products = dao.listByStore(storeId);
    	em.close(); 	
    	if(products!=null && !products.isEmpty()) {
    		return Response.ok(products).build(); //Found
    	} else {
    		return Response.noContent().build(); // Not found
    	}
	}
	

	private EntityManager getEntityManager() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySQLPU");
		return emf.createEntityManager();
	}

}