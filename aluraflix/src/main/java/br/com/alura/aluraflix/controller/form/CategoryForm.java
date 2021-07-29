package br.com.alura.aluraflix.controller.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.com.alura.aluraflix.model.Category;

public class CategoryForm {

	@NotEmpty
	@Length(min = 5, max = 20)
	private String title;

	@NotEmpty
	@Length(min = 7, max = 7)
	private String color;

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
