package br.com.alura.aluraflix.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class VideoRepositoryTest {

	@Autowired
	private TestEntityManager em;

	@Autowired
	private VideoRepository videoRepository;

	private Category category;

	@BeforeEach
	void init() {
		Video video = new Video();
		video.setTitle("GOLS DA RODADA BRASILEIRAO");
		video.setDescription("Todos os gols da rodada do brasileirao 2021");
		video.setUrl("http://youtube.com");

		Video video2 = new Video();
		video2.setTitle("GOLS DA RODADA BRASILEIRAO");
		video2.setDescription("Todos os gols da rodada do brasileirao 2021");
		video2.setUrl("http://youtube.com");

		category = new Category();
		category.setTitle("COZINHA");
		category.setColor("#000000");

		em.persist(category);

		video.setCategory(category);
		video2.setCategory(category);

		em.persist(video);
		em.persist(video2);
	}

	@Test
	void deveCarregarVideoPorCategoria() {
		List<Video> list = videoRepository.findAllByCategory(category);

		assertNotNull(list);
		assertEquals(2, list.size());
		assertEquals("COZINHA", list.get(0).getCategory().getTitle());

	}

}
