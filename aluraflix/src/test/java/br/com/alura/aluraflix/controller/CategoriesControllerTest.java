package br.com.alura.aluraflix.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CategoriesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private URI uri;

	private JSONArray arrayJson;

	@BeforeEach
	void init() throws JSONException {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("url", "http://youtube.com");
		jsonObject.put("categoryId", 1);
		jsonObject.put("id", 5);
		jsonObject.put("description", "Todos os gols da rodada do brasileirao 2021");
		jsonObject.put("title", "GOLS DA RODADA BRASILEIRAO");
		
		arrayJson = new JSONArray();
		arrayJson.put(jsonObject);
	}

	@Test
	void deveListarVideoPorIDCategoria() throws Exception {
		uri = new URI("/categorias/1/videos");

		mockMvc.perform(
				get(uri))
		.andDo(print())
		.andExpect(
				status().isOk())
		.andExpect(
				content().json(arrayJson.toString()));
	}

}
