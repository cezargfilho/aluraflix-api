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

import javax.transaction.Transactional;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.NotValidURLException;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
@Transactional
@ActiveProfiles(value = "test")
class VideosControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestEntityManager em;

	private Category category;
	
	private Video video;

	private JSONObject json;

	@BeforeEach
	void init() throws Exception {
		
		category = new Category();
		category.setTitle("COZINHA");
		category.setColor("#000000");
		em.persist(category);
		
		video = new Video();
		video.setCategory(category);
		video.setTitle("videoTest");
		video.setDescription("Video teste 123456");
		video.setUrl("http://youtube.com");
		
		em.persist(video);
		
		json = new JSONObject();
		json.put("title", "TITULO PARA TESTE");
		json.put("description", "DESCRICAO TESTE");
		json.put("url", "https://youtu.be/Ok6d04UFwQ");
		json.put("categoryId", "1");
		
	}

	@Test
	void returnsStatusCode201_whenInsertVideo() throws Exception {
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status()
				.isCreated());
	}
	
	@Test
	void returnsStatusCode400_whenInsertVideo_thenMethodArgumentNotValid() throws Exception {
		
		json.put("title", "");
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status()
				.isBadRequest())
		.andExpect(
				result -> assertTrue(
						result.getResolvedException() instanceof MethodArgumentNotValidException));
	}
	
	@Test
	void returnsStatusCode400_whenInsertVideoNotValidUrl() throws Exception {
		
		json.put("url", "http://aaaaaaa");
		
		JSONObject jsonError = new JSONObject();
		jsonError.put("field", "url");
		jsonError.put("error", "Url is invalid");
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status()
				.isBadRequest())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof NotValidURLException))
		.andExpect(
	    		result -> assertEquals("Url is invalid",
	    		result.getResolvedException().getMessage()))
		.andExpect(content().json(jsonError.toString()));
	}
	
	@Test
	void returnsStatusCode201_whenInsertVideoWithoutCateogry() throws Exception {
		
		json.remove("categoryId");
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status()
				.isCreated());
	}
	
	@Test
	void returnsStatusCode200_whenListAll() throws Exception {
		
		mockMvc.perform(
				get("/videos"))
			    .andExpect(status().isOk())
			    .andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void returnsStatusCode200_whenListByTitle() throws Exception {
		
		mockMvc.perform(
				get("/videos?search=videoTest"))
			    .andExpect(status().isOk())
			    .andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void returnsStatusCode404_whenListByTitleNotFound() throws Exception {

		mockMvc.perform(get("/videos?search=test+test"))
		.andExpect(status().isNotFound())
		.andExpect(content().contentType(APPLICATION_JSON))
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
				result -> assertEquals("Video with title 'test+test' not found",
				result.getResolvedException().getMessage()));
	}
	
	@Test
	void returnsStatusCode200_whenDetailByID() throws Exception {
		
		mockMvc.perform(
				get("/videos/1"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void returnsStatusCode200_whenRemoval() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("message", "Video was removed");
		
		mockMvc.perform(delete("/videos/2"))
		.andExpect(status().isOk())
		.andExpect(content().json(json.toString()));
	}
	
	@Test
	void returnsStatusCode404_whenBadRemoval() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("message", "Video not found");
		
		mockMvc.perform(delete("/videos/512"))
		.andExpect(status().isNotFound())
		.andExpect(content().json(json.toString()))
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Video not found",
	    		result.getResolvedException().getMessage()));
	}
	
	@Test
	void returnsStatusCode200_whenUpdateVideo() throws Exception {
		
		json.put("title", "TESTE2");
		
		mockMvc.perform(
				put("/videos/1")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status().isOk());
	}
	
	@Test
	void returnsStatusCode404_whenUpdateVideoNotFound() throws Exception {
		
		mockMvc.perform(
				put("/videos/512")
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status()
				.isNotFound())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Video not found",
	    		result.getResolvedException().getMessage()));
	}

}
