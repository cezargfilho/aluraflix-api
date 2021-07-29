package br.com.alura.aluraflix.controller.form;

import static br.com.alura.aluraflix.utils.ValidationsUtils.isNotNullAndBlank;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.config.validation.NotBlankConstraint;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.repository.CategoryRepository;

public class UpdateCategoryForm {

	@NotBlankConstraint
	@Length(min = 5, max = 20)
	private String title;

	@NotBlankConstraint
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
		
		if (isNotNullAndBlank(this.title)) {category.setTitle(this.title);}
		if (isNotNullAndBlank(this.color)) {category.setColor(this.color);}
		
		return category;
	}

}
