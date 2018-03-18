CREATE DATABASE skip;

CREATE TABLE customer(
	id_customer bigint auto_increment primary key, 	
	name	    varchar(255) not null,
	email	    varchar(255) not null,
	address	    varchar(255) not null,
	creation    timestamp(6) not null,
	password    varchar(255) not null
);

CREATE TABLE store(
	id_store    bigint auto_increment primary key, 
	name	    varchar(255) not null,
	address	    varchar(255) not null
);

CREATE TABLE product(
	id_product  bigint auto_increment primary key, 
	id_store    bigint not null,
	name	    varchar(255) not null,
	description varchar(255) not null,
	price	    double not null,
	INDEX ix_product_store (id_store),
    CONSTRAINT fk_product_store
    FOREIGN KEY (id_store) REFERENCES store(id_store)
    ON DELETE CASCADE
);

CREATE TABLE orders(
	id_order   		 bigint auto_increment primary key, 
	id_customer 	 bigint not null,
	id_store		 bigint not null,
	order_date  	 timestamp(6) not null,
	deliveryAddress  varchar(255) not null,
	contact			 varchar(255) not null,
	total			 double not null,
	status			 varchar(255) not null,
	lastUpdate		 timestamp(6) not null,
	INDEX ix_orders_store (id_store),
    CONSTRAINT fk_order_store FOREIGN KEY (id_store) REFERENCES store(id_store) ON DELETE CASCADE,
    INDEX ix_orders_customer (id_customer),
    CONSTRAINT fk_order_customer FOREIGN KEY (id_customer) REFERENCES customer(id_customer) ON DELETE CASCADE
);

CREATE TABLE order_item(
	id_order_item		 bigint auto_increment primary key, 
	id_order			 bigint not null,
	id_product			 bigint not null,
	price				 double not null,
	quantity			 bigint not null,
	total				 double not null,	
	INDEX ix_order_item_order (id_order),
    CONSTRAINT fk_order_item_order FOREIGN KEY (id_order) REFERENCES orders(id_order) ON DELETE CASCADE,
    INDEX ix_order_item_product (id_product),
    CONSTRAINT fk_order_item_product FOREIGN KEY (id_product) REFERENCES product(id_product) ON DELETE CASCADE
);


