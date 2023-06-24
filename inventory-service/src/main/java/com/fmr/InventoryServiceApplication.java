package com.fmr;

import com.fmr.model.Inventory;
import com.fmr.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory1 = new Inventory();
			inventory1.setQuantity(10);
			inventory1.setSkuCode("Iphone 14");

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("Iphone 11");
			inventory2.setQuantity(5);

			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
		};
	}
}
