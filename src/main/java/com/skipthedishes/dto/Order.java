package com.skipthedishes.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.skipthedishes.dto.Store.Builder;

@Entity
@Table(name = "order")
public class Order {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_order")
	private long id;
	
	@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
	@JsonIdentityReference(alwaysAsId=true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_customer", nullable = false)
	private Customer customer;
	
	@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
	@JsonIdentityReference(alwaysAsId=true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_store", nullable = false)
	private Store store;
	
	@OneToMany(mappedBy = "order", cascade = { CascadeType.REMOVE,
			CascadeType.PERSIST }, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderItem> items;
	
	@Column(name = "orderDate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;
	
	@Column(name = "deliveryAddress", nullable = false)
	private String deliveryAddess;
	
	@Column(name = "contact", nullable = false)
	private String contact;
	
	@Column(name = "total", nullable = false)
	private double total;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@Column(name = "lastUpdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	public Order() {
		this.items = new ArrayList<OrderItem>();
	}
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getDeliveryAddess() {
		return deliveryAddess;
	}
	public void setDeliveryAddess(String deliveryAddess) {
		this.deliveryAddess = deliveryAddess;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public List<OrderItem> getItems() {
		return Collections.unmodifiableList(items);
	}

	public void addItem(OrderItem item) {
		item.setOrder(this);
		items.add(item);
	}
	
	public static class Builder {

		private Customer customer;
		private String deliveryAddress;
		private String contact;
		private Date orderDate;
		private Date lastUpdate;
		private String status;
		private Store store;
		private List<OrderItem> items;
		
		public Builder() {
			this.orderDate = Calendar.getInstance().getTime();
			this.lastUpdate = Calendar.getInstance().getTime();
			this.status = "new";
			this.items = new ArrayList<OrderItem>();
		}
		
		public Builder toCustomer(Customer customer) {
			this.customer = customer;
			return this;
		}
		
		public Builder fromStore(Store store) {
			this.store = store;
			return this;
		}
		
		public Builder deliveryAtAddress(String deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
			return this;
		}

		public Builder contact(String contact) {
			this.contact = contact;
			return this;
		}

		public Builder addItem(Product product, long quantity, double price) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(product);
			orderItem.setQuantity(quantity);
			orderItem.setPrice(price);
			items.add(orderItem);
			return this;
		}

		public Order build() {
			Order order = new Order();
			order.setCustomer(customer);
			order.setDeliveryAddess(deliveryAddress);
			order.setContact(contact);
			order.setOrderDate(orderDate);
			order.setLastUpdate(lastUpdate);
			order.setStatus(status);
			order.setStore(store);
			items.forEach(i -> order.addItem(i));
			return order;
		}

	}
	
	
}
