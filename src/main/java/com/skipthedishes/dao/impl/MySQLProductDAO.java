package com.skipthedishes.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.skipthedishes.dao.interfaces.ProductDAO;
import com.skipthedishes.dto.Product;
import com.skipthedishes.dto.Store;

public class MySQLProductDAO implements ProductDAO{

	EntityManager em;

	public MySQLProductDAO(EntityManager em) {
		this.em = em;
	}
	
	public List<Product> listByStore(long storeId) {
		return em.createQuery("SELECT p FROM Product p where p.store.id = ?",Product.class)
				.setParameter(0, storeId)
				.getResultList();
	}

	public void create(Product product) {
		if(product.getStore() == null) { //If Store doesn't exist, get a reference to avoid fetch
			product.setStore(em.getReference(Store.class, product.getStoreId()));
		}
		em.persist(product);
	}

	public Product find(long id) {
		return em.find(Product.class, id);
	}

	public List<Product> search(String searchText) {
		return em.createQuery("SELECT p FROM Product p where p.name like ?",Product.class)
				.setParameter(0, "%"+searchText+"%")
				.getResultList();
	}
	public List<Product> listAll() {
		return em.createQuery("SELECT p FROM Product p",Product.class)
				.getResultList();
	}
	

}
