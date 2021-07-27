package br.com.alura.aluraflix.controller.form;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.repository.CategoryRepository;

public class UpdateCategoryForm {

	@Length(min = 5, max = 20)
	private String title;

	@Length(min = 7, max = 7)
	private String color;

	public String getTitle() {
		return title;
	}

	public String getColor() {
		return color;
	}

	public Category converter() {
		return new Category(this.title, this.color);
	}

	public Category update(Long id, CategoryRepository categoryRepository) {
		Category category = categoryRepository.getById(id);
		
		if (this.title != null && !this.title.isBlank()) {category.setTitle(this.title);}
		if (this.color != null && !this.color.isBlank()) {category.setColor(this.color);}
		
		return category;
	}

}
