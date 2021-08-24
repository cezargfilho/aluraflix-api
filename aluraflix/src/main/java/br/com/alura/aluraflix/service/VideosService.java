package br.com.alura.aluraflix.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.alura.aluraflix.config.validation.ErrorMessageDto;
import br.com.alura.aluraflix.controller.dto.VideoDto;
import br.com.alura.aluraflix.controller.form.UpdateVideoForm;
import br.com.alura.aluraflix.controller.form.VideoForm;
import br.com.alura.aluraflix.exception.EntityNotFoundException;
import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.CategoryRepository;
import br.com.alura.aluraflix.repository.VideoRepository;

@Service
public class VideosService {

	private VideoRepository videoRepository;

	private CategoryRepository categoryRepository;

	@Autowired
	public VideosService(
			VideoRepository videoRepository, CategoryRepository categoryRepository) {
		this.videoRepository = videoRepository;
		this.categoryRepository = categoryRepository;
	}

	public Page<VideoDto> listAll(String search, Pageable pageable) {
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

	public VideoDto detail(Long id) {
		Optional<Video> optional = videoRepository.findById(id);
		
		if (optional.isPresent()) {
			return new VideoDto(optional.get());
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

	@Transactional
	public VideoDto register(VideoForm form) {
		Video video = form.converter(categoryRepository);
		videoRepository.save(video);
		return new VideoDto(video);
	}

	@Transactional
	public VideoDto update(Long id, UpdateVideoForm form) {
		Optional<Video> optional = videoRepository.findById(id);

		if (optional.isPresent()) {
			Video video = form.atualizar(id, videoRepository, categoryRepository);
			return new VideoDto(video);
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

	@Transactional
	public ErrorMessageDto remove(Long id) {
		Optional<Video> optional = videoRepository.findById(id);

		if (optional.isPresent()) {
			videoRepository.delete(optional.get());
			return new ErrorMessageDto(ExceptionMessages.VIDEO_WAS_REMOVED);
		}
		throw new EntityNotFoundException(ExceptionMessages.VIDEO_NOT_FOUND);
	}

}
