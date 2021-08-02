package br.com.alura.aluraflix.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.alura.aluraflix.utils.ValidationsUtils;

@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String title;

	@Column(length = 7, nullable = false)
	private String color;

	public Category() {
	}

	public Category(String title, String color) {
		this.title = title.toUpperCase();
		this.color = ValidationsUtils.validateColor(color);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = ValidationsUtils.validateColor(color);
	}

	public Long getId() {
		return id;
	}
	
	

}
