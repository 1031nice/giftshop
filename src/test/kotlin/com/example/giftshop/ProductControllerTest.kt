package com.example.giftshop

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [ProductController::class])
@ExtendWith(SpringExtension::class)
internal class ProductControllerTest {

    val objectMapper = ObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var productRepository: ProductRepository

    @DisplayName("상품 조회: GET /api/products/{id}")
    @Test
    fun getProduct() {
        val product = Product(1, "hot coffee", 5000)
        given(productRepository.findById(product.id)).willReturn(Optional.of(product))

        mockMvc.perform(get("/api/products/${product.id}"))
                .andDo { MockMvcResultHandlers.print() }
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.id", `is`(product.id)))
    }

    @DisplayName("상품 추가: POST /api/products")
    @Test
    fun createProduct() {
        val product = Product(1, "hot coffee", 5000)
        given(productRepository.save(any(Product::class.java))).willReturn(product)

        mockMvc.perform(post("/api/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.id", `is`(product.id)))
    }

}