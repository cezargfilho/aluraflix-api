package br.com.alura.aluraflix.controller;

import java.net.URI;

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
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.service.CategoriesService;

@RestController
@RequestMapping(value = "categorias")
public class CategoriesController {
	
	@Autowired
	private CategoriesService categoriesService;

	@GetMapping
	@Cacheable(value = "listCategories")
	public Page<Category> list(@PageableDefault(page = 0, size = 5) Pageable pageable) {
		return categoriesService.listAll(pageable);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDto> detail(@PathVariable Long id) {
		CategoryDto dto = categoriesService.detail(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	@CacheEvict(value = {"listCategories", "listVideosByCategory"}, allEntries = true)
	public ResponseEntity<CategoryDto> register(@RequestBody @Valid CategoryForm form,
			UriComponentsBuilder uriBuilder) {
		
		CategoryDto dto = categoriesService.register(form);
		URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = {"listCategories", "listVideosByCategory"}, allEntries = true)
	public ResponseEntity<CategoryDto> update(@PathVariable Long id, 
			@RequestBody @Valid UpdateCategoryForm form) {
		
		CategoryDto dto = categoriesService.update(id, form);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping(value = "/{id}")
	@CacheEvict(value = { "listCategories", "listVideosByCategory" }, allEntries = true)
	public ResponseEntity<ErrorMessageDto> remove(@PathVariable Long id) {
		ErrorMessageDto dto = categoriesService.remove(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/{id}/videos")
	@Cacheable(value = "listVideosByCategory")
	public Page<VideoDto> listVideosByCategory(@PathVariable Long id,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {

		return categoriesService.listVideosByCategory(id, pageable);
	}

}
