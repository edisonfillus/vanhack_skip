package com.skipthedishes.dao.interfaces;

import java.util.List;

import com.skipthedishes.dto.Store;

public interface StoreDAO {
	void create(Store store);
	Store find(long id);
	List<Store> search(String searchText);
	List<Store> listAll();
	void truncate();

}
