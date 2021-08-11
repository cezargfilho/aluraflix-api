package br.com.alura.aluraflix.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles(value = "test")
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
	void givenVideos_whenSearchedByCategory() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Video> list = videoRepository.findAllByCategory(category, pageable);

		assertNotNull(list);
		assertEquals(2, list.getTotalElements());
	}

	@Test
	void givenVideos_whenSearchedAllWithCategory() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Video> page = videoRepository.findAllVideosWithCategory(pageable);

		assertFalse(page.getContent().isEmpty());
		assertEquals(2, page.getNumberOfElements());
	}

	@Test
	void givenVideos_whenSearchedByTitle() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Video> page = videoRepository.findByTitle("GOLS DA RODADA BRASILEIRAO", pageable);

		assertFalse(page.getContent().isEmpty());

		assertEquals(2, page.getNumberOfElements());
	}

	@Test
	void givenVideos_whenSearchedById() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Video> page = videoRepository.findById(1L, pageable);

		assertFalse(page.getContent().isEmpty());
		assertEquals(1, page.getNumberOfElements());
	}

}
