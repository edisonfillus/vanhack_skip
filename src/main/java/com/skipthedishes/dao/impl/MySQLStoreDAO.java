package com.skipthedishes.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.skipthedishes.dao.interfaces.StoreDAO;
import com.skipthedishes.dto.Store;

public class MySQLStoreDAO implements StoreDAO{

	EntityManager em;

	public MySQLStoreDAO(EntityManager em) {
		this.em = em;
	}
	
	public void create(Store store) {
		em.persist(store);
	}

	public Store find(long id) {
		return em.find(Store.class, id);
	}

	public List<Store> search(String searchText) {
		return em.createQuery("SELECT s FROM Store s where s.name like ?",Store.class)
				.setParameter(0, "%"+searchText+"%")
				.getResultList();
	}
	
	public List<Store> listAll() {
		return em.createQuery("SELECT s FROM Store s",Store.class)
				.getResultList();
	}

	public void truncate() {
		em.createNativeQuery("DELETE FROM Store").executeUpdate();
	}

	
}
