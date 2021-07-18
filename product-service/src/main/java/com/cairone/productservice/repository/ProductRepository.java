package com.cairone.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cairone.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
