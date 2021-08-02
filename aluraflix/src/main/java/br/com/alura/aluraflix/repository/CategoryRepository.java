package br.com.alura.aluraflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.aluraflix.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByTitle(String title);

}
