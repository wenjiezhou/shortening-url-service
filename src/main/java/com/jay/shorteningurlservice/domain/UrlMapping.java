package com.jay.shorteningurlservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="url_mapping")
public class UrlMapping {
	@Id
	@SequenceGenerator(name = "url_mapping_id_seq", sequenceName = "url_mapping_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_mapping_id_seq")
	private Long id;
	
	@Column(unique = true)
	private String shortUrl;
	
	@NotNull
	private String longUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}
	
}
