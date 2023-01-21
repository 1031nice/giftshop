package com.example.giftshop

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [ProductController::class])
@AutoConfigureRestDocs
class ProductControllerTest {

    private val objectMapper = ObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var productRepository: ProductRepository

    @DisplayName("상품 조회: GET /api/products/{id}")
    @Test
    fun getProduct() {
        val product = Product(1, "hot coffee", 5000)
        given(productRepository.findById(product.id)).willReturn(Optional.of(product))

        mockMvc
            .perform(RestDocumentationRequestBuilders.get("/api/products/{id}", product.id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id", `is`(product.id)))

            .andDo(
                document(
                    "products/getProduct",
                    pathParameters(
                        parameterWithName("id").description("상품 아이디")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
                    )
                )
            )
    }

    @DisplayName("상품 추가: POST /api/products")
    @Test
    fun createProduct() {
        val product = Product(1, "hot coffee", 5000)
        given(productRepository.save(any(Product::class.java))).willReturn(product)

        mockMvc
            .perform(RestDocumentationRequestBuilders.post("/api/products")
                .content(objectMapper.writeValueAsString(product))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.id", `is`(product.id)))

            .andDo(
                document(
                    "products/createProduct",
                    requestFields(
                        fieldWithPath("id").ignored().optional(),
                        fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품 아이디"),
                        fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
                    )
                )
            )
    }

}