package br.com.alura.aluraflix.controller;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.aluraflix.config.validation.ErrorMessageDto;
import br.com.alura.aluraflix.controller.form.CategoryForm;
import br.com.alura.aluraflix.controller.form.UpdateCategoryForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.InvalidColorCodeException;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles(value = "test")
class CategoriesControllerTest {

	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private VideoRepository videoRepository;
	
	@Test
	void returns200_whenlistAllCategories() throws Exception {
		categoryRepository.save(new Category("DEVOPS", "#AAAAAA"));
		categoryRepository.save(new Category("BACK-END", "#BBBBBB"));

		mockMvc.perform(
				get("/categorias"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON))
		.andExpect(jsonPath("$.content.[0].title", is("DEVOPS")))
		.andExpect(jsonPath("$.content.[0].color", is("#AAAAAA")))
		.andExpect(jsonPath("$.content.[1].title", is("BACK-END")))
		.andExpect(jsonPath("$.content.[1].color", is("#BBBBBB")));
		}
	
	@Test
	void returns200_whenDetailCategoriesByID() throws Exception {
		categoryRepository.save(new Category("BACK-END", "#BBBBBB"));

		mockMvc.perform(
				get("/categorias/1"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON))
		.andExpect(jsonPath("$.title", is("BACK-END")))
		.andExpect(jsonPath("$.color", is("#BBBBBB")));
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
		Category category = new Category("BACK-END", "#BBBBBB");
		categoryRepository.save(category);
		videoRepository.save(new Video("an-video-title-1", "an-video-description-1", "https://youtbe.com", category));
		videoRepository.save(new Video("an-video-title-2", "an-video-description-2", "https://youtbe.com", category));

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
		Category category = new Category("BACK-END", "#BBBBBB");

		mockMvc.perform(
				post("/categorias")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(category)))
		.andExpect(status().isCreated());
	}
	
	@ParameterizedTest
	@CsvSource({
		"'#      '", "'       '", "AAAAAAA", "'#colorr'", "an-text" 
		})
	void givenBadRequest_whenInsertCateogry_thenInvalidColor(String color) throws Exception {
		JSONObject json = new JSONObject();
		json.put("title", "an-video-title");
		json.put("color", color);
		
		ErrorMessageDto messageDto = new ErrorMessageDto("Invalid color code");
		
		mockMvc.perform(
				post("/categorias")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status().isBadRequest())
		.andExpect(
				result -> assertTrue(
						result.getResolvedException() instanceof InvalidColorCodeException))
		.andExpect(
	    		result -> assertEquals("Invalid color code",
	    		result.getResolvedException().getMessage()))
		.andExpect(content().json(jsonMapper.writeValueAsString(messageDto)));
	}

	@Test
	void givenBadRequest_whenInsertCateogry_thenMethodArgumentNotValid() throws Exception {
		CategoryForm form = new CategoryForm("", null);
		
		mockMvc.perform(
				post("/categorias")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(form)))
		.andExpect(status()
				.isBadRequest())
		.andExpect(
				result -> assertTrue(
						result.getResolvedException() instanceof MethodArgumentNotValidException));
		}
	
	@Test
	void returnsStatusCode200_whenRemoval() throws Exception {
		categoryRepository.save(new Category("BACK-END", "#BBBBBB"));
		ErrorMessageDto messageDto = new ErrorMessageDto("The category was removed");
		
		mockMvc.perform(delete("/categorias/1"))
		.andExpect(status().isOk())
		.andExpect(content().json(jsonMapper.writeValueAsString(messageDto)));
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
		categoryRepository.save(new Category("DEVOPS", "#BBBBBB"));
		UpdateCategoryForm updateForm = new UpdateCategoryForm("BACK-END", null);
		
		mockMvc.perform(
				put("/categorias/1")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(updateForm)))
		.andExpect(status().isOk());
	}
	
	@Test
	void givenNotFound_whenUpdateCategoryField_thenEntityNotFound() throws Exception {
		UpdateCategoryForm updateForm = new UpdateCategoryForm("BACK-END", null);
		
		mockMvc.perform(
				put("/categorias/512")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(updateForm)))
		.andExpect(status().isNotFound())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Category not found",
	    		result.getResolvedException().getMessage()));
		
	}
	
	

}
