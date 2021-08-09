package br.com.alura.aluraflix.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	@Query(value = "SELECT v FROM Video v JOIN v.category")
	Page<Video> findAllVideosWithCategory(Pageable pageable);

	@Query(value = "SELECT v FROM Video v JOIN FETCH v.category WHERE v.id= :id")
	Video findVideoAndCategoryById(@Param("id") Long id);

	Page<Video> findByTitle(@Param("title") String title, Pageable pageable);

	Page<Video> findAllByCategory(Category category, Pageable pageable);

	Page<Video> findById(Long id, Pageable pageable);

}
