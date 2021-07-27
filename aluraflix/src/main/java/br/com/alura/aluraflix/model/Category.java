package br.com.alura.aluraflix.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.alura.aluraflix.exception.ExceptionMessages;
import br.com.alura.aluraflix.exception.InvalidColorCodeException;

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
		this.title = title;
		this.color = validateColor(color);
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
		this.color = validateColor(color);
	}

	public Long getId() {
		return id;
	}
	
	public String validateColor(String color) {
		Pattern collorPattern = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
		Matcher matcher = collorPattern.matcher(color);
		if (matcher.matches()) {
			return color;			
		}
		throw new InvalidColorCodeException(ExceptionMessages.INVALID_COLOR_CODE);
	}

}
