package com.jay.shorteningurlservice.repository;

import com.jay.shorteningurlservice.domain.UrlMapping;
import org.springframework.data.repository.CrudRepository;

public interface UrlMappingRepository extends CrudRepository<UrlMapping, Long>{
	
}
