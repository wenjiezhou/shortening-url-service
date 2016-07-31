package com.jay.shorteningurlservice.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.jay.shorteningurlservice.api.reponse.ShorteningUrlResponse;
import com.jay.shorteningurlservice.api.request.ShorteningUrlRequest;
import com.jay.shorteningurlservice.domain.UrlMapping;
import com.jay.shorteningurlservice.repository.UrlMappingRepository;
import com.jay.shorteningurlservice.service.ShorteningUrlService;

@RestController
@RequestMapping("/shortener")
public class ShorteningUrlController {
	
	@Autowired
	UrlMappingRepository urlMappingRepository;
	
	@Autowired
	ShorteningUrlService shorteningUrlService;

	@RequestMapping(method = RequestMethod.POST, 
			consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	public ShorteningUrlResponse shorteringUrl(
			@RequestBody @Valid ShorteningUrlRequest shorteningUrlRequest) {
		ShorteningUrlResponse rv = new ShorteningUrlResponse();
		UrlMapping urlMapping = new UrlMapping();
		urlMapping.setLongUrl(shorteningUrlRequest.getUrl());
		urlMappingRepository.save(urlMapping);
		
		//generate a shortUrl code from db id
		String shortUrl = shorteningUrlService.generateShortUrl(urlMapping.getId());
		urlMapping.setShortUrl(shortUrl);
		urlMappingRepository.save(urlMapping);
		rv.setShortUrl(shortUrl);
		return rv;
	}
	
	@RequestMapping(value = "/{code}",
			method = RequestMethod.GET, 
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ModelAndView getOriginalUrl(
			@PathVariable(value = "code") String code) {
		// get db id
		Long id = shorteningUrlService.getIdByCode(code);
		UrlMapping urlMapping = urlMappingRepository.findOne(id);
		if (urlMapping != null) {
			return new ModelAndView("redirect:" + urlMapping.getLongUrl());
		}
		
		//TODO: we should redirect user to 404 error page;
		return new ModelAndView("redirect:" + "http://url.not.found.com");
	}
}
