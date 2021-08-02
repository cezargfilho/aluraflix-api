package br.com.alura.aluraflix.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.InvalidColorCodeException;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.repository.CategoryRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class CategoriesControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CategoryRepository categoryRepository;

	private Category category;

	private JSONObject jsonObject;

	@BeforeEach
	void init() throws JSONException {
		
		category = new Category();
		category.setTitle("COZINHA");
		category.setColor("#000000");
		categoryRepository.save(category);
		
		jsonObject = new JSONObject();
		jsonObject.put("title", "DEVOPS");
		jsonObject.put("color", "#AAAAAA");
	}
	
	@Test
	void returns200_whenlistAllCategories() throws Exception {

		mockMvc.perform(
				get("/categorias"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void returns200_whenDetailCategoriesByID() throws Exception {

		mockMvc.perform(
				get("/categorias/1"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void givenNotFound_whenDetailCategoriesByID_thenEntityNotFound() throws Exception {

		mockMvc.perform(
				get("/categorias/512"))
		.andExpect(status().isNotFound())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Category not found",
	    		result.getResolvedException().getMessage()));
	}

	@Test
	void returns200_whenListVideosByCategoryID() throws Exception {

		mockMvc.perform(
				get("/categorias/1/videos"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void givenNotFound_whenListVideosByCategoryID_thenEntityNotFound() throws Exception {
		mockMvc.perform(
				get("/categorias/512/videos"))
		.andExpect(status().isNotFound())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Category not found",
	    		result.getResolvedException().getMessage()));
	}
	
	@Test
	void returnsCode201_whenInsertCateogry() throws Exception {
		
		mockMvc.perform(
				post("/categorias")
				.contentType(APPLICATION_JSON)
				.content(jsonObject.toString()))
		.andExpect(status().isCreated());
	}
	
	@Test
	void givenBadRequest_whenInsertCateogry_thenInvalidColor() throws Exception {
		
		jsonObject.put("color", "#      ");
		
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("message", "Invalid color code");
		
		mockMvc.perform(
				post("/categorias")
				.contentType(APPLICATION_JSON)
				.content(jsonObject.toString()))
		.andExpect(status().isBadRequest())
		.andExpect(
				result -> assertTrue(
						result.getResolvedException() instanceof InvalidColorCodeException))
		.andExpect(
	    		result -> assertEquals("Invalid color code",
	    		result.getResolvedException().getMessage()))
		.andExpect(content().json(jsonResult.toString()));
	}
	
	@Test
	void givenBadRequest_whenInsertCateogry_thenMethodArgumentNotValid() throws Exception {
		
		jsonObject.put("title", "");
		
		mockMvc.perform(
				post("/categorias")
				.contentType(APPLICATION_JSON)
				.content(jsonObject.toString()))
		.andExpect(status()
				.isBadRequest())
		.andExpect(
				result -> assertTrue(
						result.getResolvedException() instanceof MethodArgumentNotValidException));
		}
	
	
	@Test
	void returnsStatusCode200_whenRemoval() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("message", "The category was removed");
		
		mockMvc.perform(delete("/categorias/1"))
		.andExpect(status().isOk())
		.andExpect(content().json(json.toString()));
	}
	
	@Test
	void givenNotFound_whenRemovesCategory_thenEntityNotFound() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("message", "Category not found");
		
		mockMvc.perform(delete("/categorias/512"))
		.andExpect(status().isNotFound())
		.andExpect(content().json(json.toString()))
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Category not found",
	    		result.getResolvedException().getMessage()));
	}
	
	@Test
	void returnsStatusCode200_whenUpdateCategory() throws Exception {
		
		jsonObject.put("title", "GITHUB");
		
		mockMvc.perform(
				put("/categorias/1")
				.contentType(APPLICATION_JSON)
				.content(jsonObject.toString()))
		.andExpect(status()
				.isOk());
		
	}
	
	@Test
	void givenNotFound_whenUpdateCategoryField_thenEntityNotFound() throws Exception {
		
		jsonObject.put("title", "GITHUB");
		
		mockMvc.perform(
				put("/categorias/512")
				.contentType(APPLICATION_JSON)
				.content(jsonObject.toString()))
		.andExpect(status().isNotFound())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Category not found",
	    		result.getResolvedException().getMessage()));
		
	}
	
	

}
