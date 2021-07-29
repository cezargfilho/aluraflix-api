package br.com.alura.aluraflix.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.aluraflix.model.Video;

public class VideoDto {

	private Long id;

	private String title;

	private String description;

	private String url;

	private Long categoryId;

	public VideoDto(Video video) {
		this.id = video.getId();
		this.title = video.getTitle();
		this.description = video.getDescription();
		this.url = video.getUrl();
		this.categoryId = video.getCategory().getId();
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

	public Long getCategoryId() {
		return categoryId;
	}

	public static List<VideoDto> converter(List<Video> videos) {
		List<VideoDto> dtos = new ArrayList<>();
		if (videos != null) {
			videos.forEach(v -> dtos.add(new VideoDto(v)));
			return dtos;
		}
		return dtos;
	}

}
