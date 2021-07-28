package br.com.alura.aluraflix.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.alura.aluraflix.model.Video;

public class VideoDto {

	private Long id;

	private String title;

	private String description;

	private String url;

	private Long category;

	public VideoDto(Video video) {
		this.id = video.getId();
		this.title = video.getTitle();
		this.description = video.getDescription();
		this.url = video.getUrl();
		this.category = video.getCategory().getId();
	}

	public Long getId() {
		return id;
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

	public Long getCategory() {
		return category;
	}

	public static List<VideoDto> converter(Optional<List<Video>> optional) {
		List<VideoDto> dtos = new ArrayList<>();
		if (optional.isPresent()) {
			optional.get().forEach(v -> dtos.add(new VideoDto(v)));
			return dtos;
		}
		return dtos;
	}

}
