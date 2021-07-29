package br.com.alura.aluraflix.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.utils.ValidationsUtils;

public class VideoForm {

	@NotBlank
	@Length(min = 10, max = 100)
	private String title;

	@NotBlank
	@Length(min = 10, max = 50)
	private String description;

	@NotBlank
	@Length(min = 10, max = 60)
	private String url;

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

		if (ValidationsUtils.isNotNullAndBlank(this.categoryId)) {
			Optional<Category> optional = categoryRepository.findById(Long.parseLong(this.categoryId));
			if (optional.isPresent()) {
				return new Video(this.title, this.description, this.url, optional.get());
			}
			throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
		}
		Category category = categoryRepository.findByTitle("LIVRE");
		return new Video(this.title, this.description, this.url, category);
	}

}
