package br.com.alura.aluraflix.controller.form;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.model.Video;
import br.com.alura.aluraflix.repository.VideoRepository;

public class UpdateVideoForm {

	@Length(min = 10, max = 100)
	private String title;

	@Length(min = 10, max = 50)
	private String description;

	@Length(min = 10, max = 60)
	private String url;

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public Video atualizar(Long id, VideoRepository videoRepository) {
		Video video = videoRepository.getById(id);

		if (this.title != null)
			video.setTitle(this.title);
		if (this.description != null)
			video.setDescription(this.description);
		if (this.url != null)
			video.setUrl(this.url);

		return video;
	}

}
