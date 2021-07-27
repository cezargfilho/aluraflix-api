package br.com.alura.aluraflix.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.validator.routines.UrlValidator;

import br.com.alura.aluraflix.exception.NotValidURLException;

@Entity
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(length = 50, nullable = false)
	private String description;

	@Column(length = 60, nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	private Category category;

	public Video() {
	}

	public Video(String title, String description, String url) {
		this.title = title;
		this.description = description;
		this.url = isUrlValid(url);

	}

	private String isUrlValid(String url) {
		if (new UrlValidator().isValid(url)) {
			return url;
		}
		throw new NotValidURLException("URL passada é inválida");
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descricao) {
		this.description = descricao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = isUrlValid(url);
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		return Objects.equals(id, other.id);
	}

}
