package br.com.alura.aluraflix.controller.dto;

import br.com.alura.aluraflix.model.Video;

public class VideoDto {

	private Long id;

	private String title;

	private String description;

	private String url;

	public VideoDto(Video video) {
		this.id = video.getId();
		this.title = video.getTitle();
		this.description = video.getDescription();
		this.url = video.getUrl();
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

}
