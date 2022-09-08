package com.nanosoft.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nanosoft.model.Tutorial;

@Repository
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
	
//	List<Tutorial> findByPublished(Boolean published);
	List<Tutorial> findByTitleContaining(String title);
	List<Tutorial> findTutorialsByTagsId(Long tagId);

	@Query(value = "SELECT * FROM tutorials", nativeQuery = true)
	List<Tutorial> findAll();
	
	@Query(value = "SELECT * FROM tutorials t WHERE t.published=?1", nativeQuery = true)
	List<Tutorial> findByPublished(boolean isPublished);
	
	@Query(value = "SELECT * FROM tutorials t WHERE t.title LIKE %?1%", nativeQuery = true)
	List<Tutorial> findByTitleLike(String title);
	
	@Query(value = "SELECT * FROM tutorials t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', ?1,'%'))", nativeQuery = true)
	List<Tutorial> findByTitleLikeCaseInsensitive(String title);
	
	@Query(value = "SELECT * FROM tutorials t WHERE t.level >= ?1", nativeQuery = true)
	List<Tutorial> findByLevelGreaterThanEqual(int level);
	
	@Query(value = "SELECT * FROM tutorials t WHERE t.created_at >= ?1", nativeQuery = true)
	List<Tutorial> findByDateGreaterThanEqual(Date date);

//	@Query(value = "SELECT * FROM tutorials t WHERE t.level BETWEEN ?1 AND ?2", nativeQuery = true)
//	List<Tutorial> findByLevelBetween(int start, int end);
//
	@Query(value = "SELECT * FROM tutorials t WHERE t.created_at BETWEEN ?1 AND ?2", nativeQuery = true)
	List<Tutorial> findByDateBetween(Date start, Date end);

	@Query(value = "SELECT * FROM tutorials t WHERE t.published=:isPublished AND t.level BETWEEN :start AND :end", nativeQuery = true)
	List<Tutorial> findByLevelBetween(@Param("start") int start, @Param("end") int end, @Param("isPublished") boolean isPublished);
	
	@Query(value = "SELECT * FROM tutorials t ORDER BY t.level DESC", nativeQuery = true)
	List<Tutorial> findAllOrderByLevelDesc();
	
	@Query(value = "SELECT * FROM tutorials t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', ?1,'%')) ORDER BY t.level ASC", nativeQuery = true)
	List<Tutorial> findByTitleOrderByLevelAsc(String title);
	
	@Query(value = "SELECT * FROM tutorials t WHERE t.published=true ORDER BY t.created_at DESC", nativeQuery = true)
	List<Tutorial> findAllPublishedOrderByCreatedDesc();

	@Query(value = "SELECT * FROM tutorials t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', ?1,'%'))", nativeQuery = true)
	Page<Tutorial> findByTitleLike(String title, Pageable pageable);
	
	@Query(value = "SELECT * FROM tutorials t WHERE t.published=?1", nativeQuery = true)
	Page<Tutorial> findByPublished(boolean isPublished, Pageable pageable);
}