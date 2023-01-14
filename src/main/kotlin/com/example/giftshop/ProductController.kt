package com.example.giftshop

import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(("/api/products"))
@RequiredArgsConstructor
class ProductController(val productRepository: ProductRepository) {

    @GetMapping("/{id}")
    fun getProduct(@PathVariable id: Int): Product {
        return productRepository.findById(id).orElseThrow { IllegalArgumentException("product now found") }
    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): Product {
        return productRepository.save(product)
    }

}