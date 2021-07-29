package br.com.alura.aluraflix.controller.form;

import static br.com.alura.aluraflix.utils.ValidationsUtils.isNotNullAndBlank;

import java.util.Optional;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.config.validation.NotBlankConstraint;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

public class UpdateVideoForm {

	@NotBlankConstraint
	@Length(min = 10, max = 100)
	private String title;

	@NotBlankConstraint
	@Length(min = 10, max = 50)
	private String description;

	@NotBlankConstraint
	@Length(min = 10, max = 60)
	private String url;

	@NotBlankConstraint
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

	public Video atualizar(Long id, VideoRepository videoRepository, CategoryRepository categoryRepository) {
		Video video = videoRepository.getById(id);

		if (isNotNullAndBlank(this.categoryId)) {
			Optional<Category> optional = categoryRepository.findById(Long.parseLong(this.categoryId));
			if (optional.isPresent()) {
				video.setCategory(optional.get());
			} else {
				throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
			}
		}

		if (isNotNullAndBlank(this.title))
			video.setTitle(this.title);
		if (isNotNullAndBlank(this.description))
			video.setDescription(this.description);
		if (isNotNullAndBlank(this.url))
			video.setUrl(this.url);

		return video;
	}

}
