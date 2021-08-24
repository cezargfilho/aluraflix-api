package br.com.alura.aluraflix.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.alura.aluraflix.config.validation.ErrorMessageDto;
import br.com.alura.aluraflix.controller.dto.CategoryDto;
import br.com.alura.aluraflix.controller.dto.VideoDto;
import br.com.alura.aluraflix.controller.form.CategoryForm;
import br.com.alura.aluraflix.controller.form.UpdateCategoryForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

@Service
public class CategoriesService {

	private CategoryRepository categoryRepository;

	private VideoRepository videosRepository;

	@Autowired
	public CategoriesService(CategoryRepository categoryRepository, VideoRepository videosRepository) {
		this.categoryRepository = categoryRepository;
		this.videosRepository = videosRepository;
	}

	public Page<Category> listAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	public CategoryDto detail(Long id) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			return new CategoryDto(optional.get());
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);

	}

	@Transactional
	public CategoryDto register(CategoryForm form) {
		Category category = form.converter();
		categoryRepository.save(category);
		return new CategoryDto(category);
	}

	@Transactional
	public CategoryDto update(Long id, UpdateCategoryForm form) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			Category category = form.update(id, categoryRepository);
			return new CategoryDto(category);
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

	@Transactional
	public ErrorMessageDto remove(Long id) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			categoryRepository.delete(optional.get());
			return new ErrorMessageDto("The category was removed");
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

	public Page<VideoDto> listVideosByCategory(Long id, Pageable pageable) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			Page<Video> videos = videosRepository.findAllByCategory(optional.get(), pageable);
			return VideoDto.converter(videos);
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

}
