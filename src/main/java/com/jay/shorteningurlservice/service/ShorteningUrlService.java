package com.jay.shorteningurlservice.service;

public interface ShorteningUrlService {
	
	public String generateShortUrl(Long id);
	
	public Long getIdByCode(String code);
}
