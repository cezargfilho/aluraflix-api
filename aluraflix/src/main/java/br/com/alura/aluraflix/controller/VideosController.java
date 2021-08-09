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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.aluraflix.config.validation.ErrorMessageDto;
import br.com.alura.aluraflix.controller.dto.VideoDto;
import br.com.alura.aluraflix.controller.form.UpdateVideoForm;
import br.com.alura.aluraflix.controller.form.VideoForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

@RestController
@RequestMapping(value = "/videos")
public class VideosController {

	@Autowired
	private VideoRepository videoRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping
	@Cacheable(value = "listVideos")
	public Page<VideoDto> list(
			@RequestParam(required = false) String search, 
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		
		if (search == null || search.trim().isEmpty()) {
			Page<Video> videos = videoRepository.findAllVideosWithCategory(pageable);		
			return videos.map(VideoDto::new);			
		} else {
			Page<Video> videos = videoRepository.findByTitle(search, pageable);
			if (videos.getNumberOfElements() != 0) {
				return VideoDto.converter(videos);				
			}
			throw new EntityNotFoundException(ExceptionMessages.TITLE_NOT_FOUND.replace("REPLACE", search));
		}		
	}

	@GetMapping(value = "/{id}")
	public Page<VideoDto> detail(
			@PathVariable Long id, 
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		
		Page<Video> videos = videoRepository.findById(id, pageable);
		if (videos.getNumberOfElements() != 0) {
			return VideoDto.converter(videos);
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

	@PostMapping
	@Transactional
	@CacheEvict(value = {"listVideos"}, allEntries = true)
	public ResponseEntity<VideoDto> register(@RequestBody @Valid VideoForm form, UriComponentsBuilder uriBuilder) {
		Video video = form.converter(categoryRepository);
		videoRepository.save(video);

		URI uri = uriBuilder.path("/videos/{id}").buildAndExpand(video.getId()).toUri();
		return ResponseEntity.created(uri).body(new VideoDto(video));
	}

	@PutMapping(value = "/{id}")
	@Transactional
	@CacheEvict(value = {"listVideos"}, allEntries = true)
	public ResponseEntity<VideoDto> update(
			@PathVariable Long id, @RequestBody @Valid UpdateVideoForm form) {
		Optional<Video> optional = videoRepository.findById(id);

		if (optional.isPresent()) {
			Video video = form.atualizar(id, videoRepository, categoryRepository);
			return ResponseEntity.ok(new VideoDto(video));
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

	@DeleteMapping(value = "/{id}")
	@Transactional
	@CacheEvict(value = {"listVideos"}, allEntries = true)
	public ResponseEntity<ErrorMessageDto> remove(@PathVariable Long id) {
		Optional<Video> optional = videoRepository.findById(id);

		if (optional.isPresent()) {
			videoRepository.delete(optional.get());
			return ResponseEntity.ok(new ErrorMessageDto(ExceptionMessages.VIDEO_WAS_REMOVED));
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

}
