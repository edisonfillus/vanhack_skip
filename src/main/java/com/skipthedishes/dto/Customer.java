package com.skipthedishes.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "customer")
public class Customer {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id_customer")
	private long id;

	@Column(name= "name", nullable = false)
	private String name;
	
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "creation", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creation;

	@Column(name = "password", nullable = false)
	private String password;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
}
