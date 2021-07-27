package br.com.alura.aluraflix.controller.dto;

import br.com.alura.aluraflix.model.Video;

public class VideoDto {

	private Long id;

	private String title;

	private String description;

	private String url;

	private String category;

	public VideoDto(Video video) {
		this.id = video.getId();
		this.title = video.getTitle();
		this.description = video.getDescription();
		this.url = video.getUrl();
		this.category = video.getCategory().getTitle();
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

	public String getCategory() {
		return category;
	}

}
