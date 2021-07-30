package br.com.alura.aluraflix.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class VideosControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	private Category category;
	
	private Video video;

	@BeforeEach
	void init() throws URISyntaxException {
		
		category = new Category();
		category.setTitle("COZINHA");
		category.setColor("#000000");
		categoryRepository.save(category);
		
		video = new Video();
		video.setCategory(category);
		video.setTitle("Titulo teste para video");
		video.setDescription("Video teste 123456");
		video.setUrl("http://youtube.com");
		videoRepository.save(video);
		
	}

	@Test
	void returnsCode201ForInsertVideo() throws Exception {
		URI uriVideos = new URI("/videos");
		
		JSONObject json = new JSONObject();
		json.put("title", "FALA DO BOLTZ EM LIVE E CASIMIRO SE REVOLTA | Cortes do Casimito");
		json.put("description", "Live do dia 28/05/2021 para o dia 29/05/2021");
		json.put("url", "https://youtu.be/Ok6d04UFwQ");
		json.put("categoryId", "1");
		
		mockMvc.perform(
				post(uriVideos)
				.contentType(APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(status()
				.is(HttpStatus.CREATED.value()));

	}
	
	@Test
	void returnsCode200ForListAll() throws Exception {
		URI uriVideos = new URI("/videos");
		Video video = new Video(
				"FALA DO BOLTZ EM LIVE E CASIMIRO SE REVOLTA | Cortes do Casimito",
				"Live do dia 28/05/2021 para o dia 29/05/2021",
				"https://youtu.be/Ok6d04UFwQ",
				category);
		
		videoRepository.save(video);
		
		mockMvc.perform(
				get(uriVideos))
			    .andExpect(status().isOk())
			    .andExpect(content().contentType(APPLICATION_JSON));
	}
	
	@Test
	void returnsCode200ForRemoval() throws Exception {
		URI uri = new URI("/videos/1");
		
		JSONObject json = new JSONObject();
		json.put("message", "Video was removed");
		
		mockMvc.perform(
				delete(uri))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().json(json.toString()));
	}
	
	@Test
	void returnsCode404ForBadRemoval() throws Exception {
		URI uri = new URI("/videos/512");
		
		JSONObject json = new JSONObject();
		json.put("message", "Video not found");
		
		mockMvc.perform(
				delete(uri))
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(content().json(json.toString()));
	}

}
