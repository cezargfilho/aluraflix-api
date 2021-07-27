package br.com.alura.aluraflix.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import br.com.alura.aluraflix.controller.dto.VideoDto;
import br.com.alura.aluraflix.controller.form.UpdateVideoForm;
import br.com.alura.aluraflix.controller.form.VideoForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.VideoRepository;

@RestController
@RequestMapping(value = "/videos")
public class VideosController {

	@Autowired
	private VideoRepository videoRepository;

	@GetMapping
	public List<Video> list() {
		return videoRepository.findAll();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<VideoDto> detail(@PathVariable Long id) {
		Optional<Video> video = videoRepository.findById(id);
		if (video.isPresent()) {
			return ResponseEntity.ok(new VideoDto(video.get()));
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<VideoDto> register(@RequestBody @Valid VideoForm form, UriComponentsBuilder uriBuilder) {
		Video video = form.converter();
		videoRepository.save(video);

		URI uri = uriBuilder.path("/videos/{id}").buildAndExpand(video.getId()).toUri();
		return ResponseEntity.created(uri).body(new VideoDto(video));
	}

	@PutMapping(value = "/{id}")
	@Transactional
	public ResponseEntity<VideoDto> update(@PathVariable Long id, @RequestBody @Valid UpdateVideoForm form) {
		Optional<Video> optional = videoRepository.findById(id);

		if (optional.isPresent()) {
			Video video = form.atualizar(id, videoRepository);
			return ResponseEntity.ok(new VideoDto(video));
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

	@DeleteMapping(value = "/{id}")
	@Transactional
	public ResponseEntity<ErrorMessageDto> remove(@PathVariable Long id) {
		Optional<Video> optional = videoRepository.findById(id);

		if (optional.isPresent()) {
			videoRepository.delete(optional.get());

			return ResponseEntity.status(HttpStatus.OK).body(new ErrorMessageDto("The video has been removed"));
		}

		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

}
