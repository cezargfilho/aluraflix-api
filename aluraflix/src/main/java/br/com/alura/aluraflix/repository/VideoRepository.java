package br.com.alura.aluraflix.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.aluraflix.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
