package br.com.alura.aluraflix.controller.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alura.aluraflix.model.Category;

public class CategoryForm {

	@NotEmpty
	@Length(min = 5, max = 20)
	@JsonProperty(required = true)
	private String title;

	@NotEmpty
	@Length(min = 7, max = 7)
	@JsonProperty(required = true)
	private String color;
	
	public CategoryForm(String title, String color) {
		this.title = title;
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public String getColor() {
		return color;
	}

	public Category converter() {
		return new Category(this.title, this.color);
	}

}
