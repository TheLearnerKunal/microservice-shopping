package com.fmr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fmr.model.Product;


public interface ProductRepository extends MongoRepository<Product,String>{

}
