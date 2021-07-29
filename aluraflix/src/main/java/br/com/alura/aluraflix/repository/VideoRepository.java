package br.com.alura.aluraflix.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.aluraflix.model.Category;
import br.com.alura.aluraflix.model.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	@Query(value = "SELECT v FROM Video v JOIN FETCH v.category")
	List<Video> findAllVideosWithCategory();

	@Query(value = "SELECT v FROM Video v JOIN FETCH v.category WHERE v.id= :id")
	Video findVideoAndCategoryById(@Param("id") Long id);

	List<Video> findByTitle(@Param("title") String title);

	//@Query("SELECT v FROM Video v JOIN FETCH v.category WHERE  v.category.id = :id")
	List<Video> findAllByCategory(Category category);

	List<Video> findAllByCategoryId(Long id);

}
