package com.example.projectztp;

import com.example.projectztp.domain.Cart;
import com.example.projectztp.domain.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ProjectZtpApplication {
	@Autowired
	private CartRepository cartRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectZtpApplication.class, args);
	}


	@EventListener(value = ApplicationReadyEvent.class)
	public void createCart() {
		cartRepository.count()
				.flatMap(countCart -> {
					if (countCart == 0L) {
						Cart cart = new Cart();
						return cartRepository.insert(cart);
					}
					return Mono.empty();
				})
				.subscribe();
	}
}
