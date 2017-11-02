package com.publicratings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends PagingAndSortingRepository<Place, Long>, JpaRepository<Place, Long> {
	List<Place> findByIsFavorite(Integer isFavorite);
}