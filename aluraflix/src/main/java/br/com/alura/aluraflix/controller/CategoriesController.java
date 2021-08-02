package br.com.alura.aluraflix.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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

@RestController
@RequestMapping(value = "categorias")
public class CategoriesController {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private VideoRepository videosRepository;

	@GetMapping
	@Cacheable(value = "listCategories")
	public Page<Category> list(@PageableDefault(page = 0, size = 5) Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDto> detail(@PathVariable Long id) {
		Optional<Category> optional = categoryRepository.findById(id);
		
		if (optional.isPresent()) {
			return ResponseEntity.ok(new CategoryDto(optional.get()));
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = {"listCategories", "listVideosByCategory"}, allEntries = true)
	public ResponseEntity<CategoryDto> register(@RequestBody @Valid CategoryForm form,
			UriComponentsBuilder uriBuilder) {
		
		Category category = form.converter();
		categoryRepository.save(category);

		URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(category.getId()).toUri();
		return ResponseEntity.created(uri).body(new CategoryDto(category));
	}

	@PutMapping(value = "/{id}")
	@Transactional
	@CacheEvict(value = {"listCategories", "listVideosByCategory"}, allEntries = true)
	public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody @Valid UpdateCategoryForm form) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			Category category = form.update(id, categoryRepository);
			return ResponseEntity.ok(new CategoryDto(category));
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

	@DeleteMapping(value = "/{id}")
	@Transactional
	@CacheEvict(value = {"listCategories", "listVideosByCategory"}, allEntries = true)
	public ResponseEntity<ErrorMessageDto> remove(@PathVariable Long id) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			categoryRepository.delete(optional.get());
			return ResponseEntity.ok(new ErrorMessageDto("The category was removed"));
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

	@GetMapping(value = "/{id}/videos")
	@Cacheable(value = "listVideosByCategory")
	public Page<VideoDto> listVideosByCategory(
			@PathVariable Long id,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		Optional<Category> optional = categoryRepository.findById(id);

		if (optional.isPresent()) {
			Page<Video> videos = videosRepository.findAllByCategory(optional.get(), pageable);
			return VideoDto.converter(videos);
		}
		throw new EntityNotFoundException(ExceptionMessages.CATEGORY_NOT_FOUND);
	}

}
