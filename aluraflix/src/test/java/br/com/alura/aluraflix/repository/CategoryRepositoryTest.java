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

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles(value = "test")
class CategoryRepositoryTest {

	@Autowired
	private TestEntityManager em;

	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	void init() {
		Category category = new Category();
		category.setTitle("COZINHA");
		category.setColor("#000000");

		em.persist(category);
	}

	@Test
	void givenCategory_whenSearchedByTitle() {
		Category category = categoryRepository.findByTitle("COZINHA");

		assertNotNull(category);
		assertEquals("COZINHA", category.getTitle());
	}

	@Test
	void givenPage_whenSearchedByTitle() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Category> page = categoryRepository.findByTitle("COZINHA", pageable);

		assertFalse(page.getContent().isEmpty());
		assertEquals(1, page.getNumberOfElements());
	}

}
