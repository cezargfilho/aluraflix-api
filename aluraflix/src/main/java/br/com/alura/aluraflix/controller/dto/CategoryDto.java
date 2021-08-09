package br.com.alura.aluraflix.controller.dto;

import org.springframework.data.domain.Page;

import br.com.alura.aluraflix.model.Category;

public class CategoryDto {

	private Long id;

	private String title;

	private String color;

	public CategoryDto(Category category) {
		this.id = category.getId();
		this.title = category.getTitle();
		this.color = category.getColor();
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getColor() {
		return color;
	}
	
	public static Page<CategoryDto> converter(Page<Category> categories) {
		return categories.map(CategoryDto::new);
	}

}
