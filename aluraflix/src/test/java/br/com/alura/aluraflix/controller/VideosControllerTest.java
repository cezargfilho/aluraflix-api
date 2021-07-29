package br.com.alura.aluraflix.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
	private VideoRepository viedeoRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	private Category category;

	private URI uriVideos;
	
	@BeforeEach
	void init() throws URISyntaxException {
		uriVideos = new URI("/videos");
		
		category = new Category();
		category.setTitle("COZINHA");
		category.setColor("#000000");
		categoryRepository.save(category);
	}

	@Test
	void deveRetornar200ParaInserirVideo() throws Exception {
		
		JSONObject json = new JSONObject();
		json.put("title", "FALA DO BOLTZ EM LIVE E CASIMIRO SE REVOLTA | Cortes do Casimito");
		json.put("description", "Live do dia 28/05/2021 para o dia 29/05/2021");
		json.put("url", "https://youtu.be/Ok6d04UFwQ");
		json.put("categoryId", "1");
		
		mockMvc.perform(MockMvcRequestBuilders
				.post(uriVideos)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toString()))
		.andExpect(MockMvcResultMatchers
				.status()
				.is(HttpStatus.CREATED.value()));

	}
	
	@Test
	void deveRetornar200ParaListagem() throws Exception {
		Video video = new Video(
				"FALA DO BOLTZ EM LIVE E CASIMIRO SE REVOLTA | Cortes do Casimito",
				"Live do dia 28/05/2021 para o dia 29/05/2021",
				"https://youtu.be/Ok6d04UFwQ",
				category);
		
		viedeoRepository.save(video);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get(uriVideos))
			    .andExpect(MockMvcResultMatchers.status().isOk());
	}

}
