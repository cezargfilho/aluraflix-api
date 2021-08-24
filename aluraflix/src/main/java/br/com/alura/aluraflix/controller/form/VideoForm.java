package br.com.alura.aluraflix.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alura.aluraflix.config.validation.NotBlankConstraint;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.utils.ValidationsUtils;

public class VideoForm {

	@NotBlank
	@Length(min = 10, max = 100)
	@JsonProperty(required = true)
	private String title;

	@NotBlank
	@Length(min = 10, max = 50)
	@JsonProperty(required = true)
	private String description;

	@NotBlank
	@Length(min = 10, max = 60)
	@JsonProperty(required = true)
	private String url;

	@NotBlankConstraint
	@NumberFormat(style = Style.NUMBER)
	@JsonInclude(value = Include.NON_NULL)
	@JsonProperty(required = false)
	private String categoryId;
	
	@Deprecated
	public VideoForm() {}

	public VideoForm(String title, String description, String url, String categoryId) {
		this.title = title;
		this.description = description;
		this.url = url;
		this.categoryId = categoryId;
	}

	public VideoForm(String title, String description, String url) {
		this.title = title;
		this.description = description;
		this.url = url;
	}

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

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
