package com.skipthedishes.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "store")
public class Store {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_store")
	private long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "address", nullable = false)
	private String address;

	@OneToMany(mappedBy = "store", cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST }, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Product> products;

	public Store() {
		super();
		this.products = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Product> getProducts() {
		return Collections.unmodifiableList(products);
	}

	public void addProduct(Product product) {
		product.setStore(this);
		this.products.add(product);
	}

	public static class Builder {

		private String storeName;
		private String address;
		private List<Product> products;

		public Builder(String storeName) {
			this.storeName = storeName;
			this.products = new ArrayList<>();
		}

		public Builder atAddress(String address) {
			this.address = address;
			return this;
		}

		public Builder addProduct(String name, String description, double price) {
			Product product = new Product(name, description, price);
			products.add(product);
			return this;
		}

		public Store build() {
			Store store = new Store();
			store.setAddress(address);
			store.setName(storeName);
			products.forEach(p -> store.addProduct(p));
			return store;
		}

	}

}
