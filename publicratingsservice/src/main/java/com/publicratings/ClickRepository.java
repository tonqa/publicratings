package com.publicratings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClickRepository extends CrudRepository<Click, Long>, JpaRepository<Click, Long> {
	List<Click> findByPlaceIdIn(long[] place_ids);
}