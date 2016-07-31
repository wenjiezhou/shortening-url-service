package com.jay.shorteningurlservice.service.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jay.shorteningurlservice.service.ShorteningUrlService;

@Service
public class ShorteningUrlServiceImpl implements ShorteningUrlService {
	@Value("${base.url:http://localhost:9024/shortener/}")
	private String baseUrl;
	public final static String baseContents = "ABCDEFGHIGKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	public final static Long base = 62L;
	
	@Override
	public String generateShortUrl(Long id) {
		List<Long> digits = new LinkedList<Long>();
		while (id > 0) {
			long remainder = id % base;
			digits.add(remainder);
			id = id / base;
		}
		Collections.reverse(digits);
		StringBuilder sb = new StringBuilder(baseUrl);
		for(Long digit : digits) {
			// the digit value is always less than 62, we can convert it to int without issue
			sb.append(baseContents.substring((int) (long) digit, (int) (digit + 1)));
		}
		return sb.toString();
	}
	
	@Override
	public Long getIdByCode(String code) {
		int len = code.length();
		long rv = 0;
		for(int i = 0; i < len; i++) {
			String s = code.substring(i, i+1);
			long pow = BigInteger.valueOf(base).pow(len - i - 1).longValue();
			rv += pow * baseContents.indexOf(s);
		}		
		return rv;
	}
}
