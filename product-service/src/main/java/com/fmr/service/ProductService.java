package com.fmr.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fmr.dto.ProductRequest;
import com.fmr.dto.ProductResponse;
import com.fmr.model.Product;
import com.fmr.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	
	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		productRepository.save(product);
	    log.info("Product {} is Saved",product.getId());
	}

	public List<ProductResponse> getAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(product -> ProductResponse.builder()
				.Id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice()).build()).collect(Collectors.toList());
	}
}
