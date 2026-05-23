package com.suranjan.mas.cart.controller;


import com.suranjan.mas.auth.entity.User;
import com.suranjan.mas.auth.repository.UserRepository;
import com.suranjan.mas.cart.dto.AddToCartRequest;
import com.suranjan.mas.cart.dto.CartResponse;
import com.suranjan.mas.cart.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }
    @PostMapping("/add")
    public String addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartService.addToCart(user, request);
    }

    @GetMapping
    public CartResponse getCart(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartService.getCart(user);
    }

    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(
            @PathVariable Long productId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartService.removeFromCart(user, productId);
    }

    @PutMapping("/update/{productId}")
    public String updateQuantity
            (@PathVariable Long productId,
             @RequestParam Integer quantity,
             Authentication authentication)
    {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return cartService.updateQuantity(user,productId, quantity);
    }

    @DeleteMapping("/clear")
    public String clearCart(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartService.clearCart(user);
    }
}
