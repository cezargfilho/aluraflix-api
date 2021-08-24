package br.com.alura.aluraflix.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
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
import br.com.alura.aluraflix.controller.form.UpdateVideoForm;
import br.com.alura.aluraflix.controller.form.VideoForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.NotValidURLException;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles(value = "test")
class VideosControllerTest {
	
	private final ObjectMapper jsonMapper = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void returnsStatusCode201_whenInsertVideo() throws Exception {
		categoryRepository.save(new Category("LIVRE", "#aaaaaa"));
		VideoForm video = new VideoForm("an-test-title", "an-test-video-description", "https://youtube.com", "1");
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(video)))
		.andExpect(status().isCreated());
	}
	
	@Test
	void returnsStatusCode400_whenInsertVideo_thenMethodArgumentNotValid() throws Exception {
		VideoForm video = new VideoForm("", null, null, null);
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(video)))
		.andExpect(status()
				.isBadRequest())
		.andExpect(
				result -> assertTrue(
						result.getResolvedException() instanceof MethodArgumentNotValidException));
	}
	
	@Test
	void returnsStatusCode400_whenInsertVideoNotValidUrl() throws Exception {
		VideoForm video = new VideoForm("an-test-title", "an-test-video-description", "https://an-invalid-url", null);
		
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(video)))
		.andExpect(status()
				.isBadRequest())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof NotValidURLException))
		.andExpect(
	    		result -> assertEquals("Url is invalid",
	    		result.getResolvedException().getMessage()));
	}
	
	@Test
	void returnsStatusCode201_whenInsertVideoWithoutCateogry() throws Exception {
		categoryRepository.save(new Category("LIVRE", "#aaaaaa"));
		VideoForm video = new VideoForm("an-test-title", "an-test-video-description", "https://youtube.com");
		System.out.println(jsonMapper.writeValueAsString(video));
		mockMvc.perform(
				post("/videos")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(video)))
		.andExpect(status().isCreated());
	}
	
	@Test
	void returnsStatusCode200_whenListAll() throws Exception {
		Category category = categoryRepository.save(new Category("BACK-END", "#aaaaaa"));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", category));
		videoRepository.save(new Video("an-test-title-2", "an-test-video-description-2", "https://youtube.com", category));
		
		mockMvc.perform(
				get("/videos"))
		.andDo(print())
			    .andExpect(status().isOk())
			    .andExpect(content().contentType(APPLICATION_JSON))
			    .andExpect(jsonPath("$.content.[0].title", is("an-test-title-1")))
			    .andExpect(jsonPath("$.content.[0].description", is("an-test-video-description-1")))
			    .andExpect(jsonPath("$.content.[0].url", is("https://youtube.com")))
			    .andExpect(jsonPath("$.content.[1].title", is("an-test-title-2")))
			    .andExpect(jsonPath("$.content.[1].description", is("an-test-video-description-2")))
			    .andExpect(jsonPath("$.content.[1].url", is("https://youtube.com")));
	}
	
	@Test
	void returnsStatusCode200_whenListByTitle() throws Exception {
		Category category = categoryRepository.save(new Category("BACK-END", "#aaaaaa"));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", category));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-2", "https://youtube.com", category));

		mockMvc.perform(
				get("/videos?search=an-test-title-1"))
			    .andExpect(status().isOk())
			    .andExpect(content().contentType(APPLICATION_JSON))
			    .andExpect(jsonPath("$.content.[0].title", is("an-test-title-1")))
			    .andExpect(jsonPath("$.content.[1].title", is("an-test-title-1")));
	}
	
	@Test
	void returnsStatusCode404_whenListByTitleNotFound() throws Exception {
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", null));

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
		Category category = categoryRepository.save(new Category("BACK-END", "#aaaaaa"));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", category));
		
		mockMvc.perform(
				get("/videos/1"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON))
		.andExpect(jsonPath("$.title", is("an-test-title-1")))
	    .andExpect(jsonPath("$.description", is("an-test-video-description-1")))
	    .andExpect(jsonPath("$.url", is("https://youtube.com")));
	}
	
	@Test
	void returnsStatusCode200_whenRemoval() throws Exception {
		Category category = categoryRepository.save(new Category("BACK-END", "#aaaaaa"));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", category));
		
		ErrorMessageDto message = new ErrorMessageDto("Video was removed");
		
		mockMvc.perform(delete("/videos/1"))
		.andExpect(status().isOk())
		.andExpect(content().json(jsonMapper.writeValueAsString(message)));
	}
	
	@Test
	void returnsStatusCode404_whenBadRemoval() throws Exception {
		ErrorMessageDto message = new ErrorMessageDto("Video not found");
		
		mockMvc.perform(delete("/videos/512"))
		.andExpect(status().isNotFound())
		.andExpect(content().json(jsonMapper.writeValueAsString(message)))
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Video not found",
	    		result.getResolvedException().getMessage()));
	}
	
	@Test
	void returnsStatusCode200_whenUpdateVideo() throws Exception {
		Category category = categoryRepository.save(new Category("BACK-END", "#aaaaaa"));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", category));
		
		UpdateVideoForm form = new UpdateVideoForm("an-test-title-1", "an-test-video-description-1", null);
		
		mockMvc.perform(
				put("/videos/1")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(form)))
		.andExpect(status().isOk());
	}
	
	@Test
	void returnsStatusCode404_whenUpdateVideoNotFound() throws Exception {
		Category category = categoryRepository.save(new Category("BACK-END", "#aaaaaa"));
		videoRepository.save(new Video("an-test-title-1", "an-test-video-description-1", "https://youtube.com", category));
		
		UpdateVideoForm videoForm = new UpdateVideoForm("an-test-title-1", "an-test-video-description-1", null);
		
		mockMvc.perform(
				put("/videos/512")
				.contentType(APPLICATION_JSON)
				.content(jsonMapper.writeValueAsString(videoForm)))
		.andExpect(status()
				.isNotFound())
		.andExpect(
				result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
		.andExpect(
	    		result -> assertEquals("Video not found",
	    		result.getResolvedException().getMessage()));
	}

}
