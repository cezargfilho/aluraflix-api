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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.aluraflix.config.validation.ErrorMessageDto;
import br.com.alura.aluraflix.controller.dto.VideoDto;
import br.com.alura.aluraflix.controller.form.UpdateVideoForm;
import br.com.alura.aluraflix.controller.form.VideoForm;
import br.com.alura.aluraflix.service.VideosService;

@RestController
@RequestMapping(value = "/videos")
public class VideosController {
	
	@Autowired
	private VideosService videosService;

	@GetMapping
	@Cacheable(value = "listVideos")
	public Page<VideoDto> list(
			@RequestParam(required = false) String search, 
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		
		return videosService.listAll(search, pageable);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<VideoDto> detail(@PathVariable Long id) {
		VideoDto dto = videosService.detail(id);
		return ResponseEntity.ok(dto);
	}

	@PostMapping
	@CacheEvict(value = {"listVideos"}, allEntries = true)
	public ResponseEntity<VideoDto> register(
			@RequestBody @Valid VideoForm form, UriComponentsBuilder uriBuilder) {

		VideoDto dto = videosService.register(form);
		URI uri = uriBuilder.path("/videos/{id}").buildAndExpand(dto .getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	@CacheEvict(value = { "listVideos" }, allEntries = true)
	public ResponseEntity<VideoDto> update(
			@PathVariable Long id, @RequestBody @Valid UpdateVideoForm form) {

		VideoDto dto = videosService.update(id, form);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping(value = "/{id}")
	@CacheEvict(value = { "listVideos" }, allEntries = true)
	public ResponseEntity<ErrorMessageDto> remove(@PathVariable Long id) {
		ErrorMessageDto dto = videosService.remove(id);
		return ResponseEntity.ok(dto);
	}

}
