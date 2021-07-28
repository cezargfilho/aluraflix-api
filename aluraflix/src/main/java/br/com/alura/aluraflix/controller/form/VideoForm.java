package br.com.alura.aluraflix.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;

public class VideoForm {

	@NotNull
	@NotEmpty
	@Length(min = 10, max = 100)
	private String title;

	@NotNull
	@NotEmpty
	@Length(min = 10, max = 50)
	private String description;

	@NotNull
	@NotEmpty
	@Length(min = 10, max = 60)
	private String url;
	
	@NotNull
	@NotEmpty
	private String categoryId;

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}
	
	public String getCategoryId() {
		return categoryId;
	}

	public Video converter(CategoryRepository categoryRepository) {
		Category category = categoryRepository.getById(Long.parseLong(this.categoryId));
		return new Video(this.title, this.description, this.url, category);
	}

}
