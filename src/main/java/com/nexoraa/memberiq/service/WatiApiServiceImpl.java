package com.nexoraa.memberiq.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nexoraa.memberiq.entity.TemplateMessageRequest;

@Service
public class WatiApiServiceImpl implements WatiApiService {

	private final String WATI_API_URL = "https://app-server.wati.io/api/v1/sendTemplateMessage";
	private final String BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIxNTExY2ZiZi03YTY2LTQ2YmUtOGQxYy0zNjJjMDlmZWJjMzkiLCJ1bmlxdWVfbmFtZSI6Iml0ZGVlcGFrLjM1MzZAZ21haWwuY29tIiwibmFtZWlkIjoiaXRkZWVwYWsuMzUzNkBnbWFpbC5jb20iLCJlbWFpbCI6Iml0ZGVlcGFrLjM1MzZAZ21haWwuY29tIiwiYXV0aF90aW1lIjoiMDQvMTMvMjAyNSAwNzowOToyMCIsImRiX25hbWUiOiJ3YXRpX2FwcF90cmlhbCIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlRSSUFMIiwiZXhwIjoxNzQ1MTkzNjAwLCJpc3MiOiJDbGFyZV9BSSIsImF1ZCI6IkNsYXJlX0FJIn0.rKlIHkgW_5TYG8O5DM0VSrBEeoPreBxoD5ouyNsTlaM";

	public String sendTemplateMessage() {

		TemplateMessageRequest request = new TemplateMessageRequest();

		request.setBroadcast_name(BEARER_TOKEN);
		request.setPhone_number("9359499859");
		request.setTemplate_name(BEARER_TOKEN);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(BEARER_TOKEN);

		HttpEntity<TemplateMessageRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(WATI_API_URL, entity, String.class);

		return response.getBody();
	}

	@Override
	public String sendTemplateMessage(TemplateMessageRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}
