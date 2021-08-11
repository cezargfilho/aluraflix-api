package br.com.alura.aluraflix.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.aluraflix.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByTitle(String string);

	Page<Category> findByTitle(String title, Pageable pageable);

	Page<Category> findById(Long id, Pageable pageable);

}
