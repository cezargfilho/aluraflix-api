package br.com.alura.aluraflix.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.aluraflix.controller.dto.CategoryDto;
import br.com.alura.aluraflix.controller.form.CategoryForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.repository.CategoryRepository;

@RestController
@RequestMapping(value = "categorias")
public class CategoriesController {

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	public List<Category> list() {
		return categoryRepository.findAll();
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
	public ResponseEntity<CategoryDto> register(@RequestBody @Valid CategoryForm form , UriComponentsBuilder uriBuilder) {
		Category category = form.converter();
		categoryRepository.save(category);
		
		URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(category.getId()).toUri();
		return ResponseEntity.created(uri).body(new CategoryDto(category));
	}

}
