package com.skipthedishes.dao.interfaces;

import java.util.List;

import com.skipthedishes.dto.Product;

public interface ProductDAO {

	List<Product> listByStore(long storeId);

	public void create(Product product);

	public Product find(long id);

	public List<Product> search(String searchText);
	
	public List<Product> listAll();

}
