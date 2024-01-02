package com.hospital.exception;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.service.EmailSendingServiceImpl;

@Component
public class NameRestTemplateResponseErrorHandler implements ResponseErrorHandler {
	ObjectMapper objectMapper = new ObjectMapper();
	@Autowired EmailSendingServiceImpl emailService;
	private Instant lastApiLimitExceedMailedAt;
	
	
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) 
      throws IOException {

        return (
          httpResponse.getStatusCode().is4xxClientError()
          || httpResponse.getStatusCode().is5xxServerError());
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) 
      throws IOException {

        if (httpResponse.getStatusCode().is5xxServerError()) {
        	NameExceptionDetails res = objectMapper.readValue(httpResponse.getBody(), NameExceptionDetails.class);
        	throw new NameException(res);
        	
        } else if (httpResponse.getStatusCode().is4xxClientError()) {
        	if(httpResponse.getStatusCode().value() == 429) {
        		Instant currentInstant = Instant.now();
        		boolean sendMail = false;
        		
        		if(lastApiLimitExceedMailedAt == null) {
        			lastApiLimitExceedMailedAt = currentInstant;
        			sendMail = true;
        		}else {
            		Duration duration = Duration.between(currentInstant, lastApiLimitExceedMailedAt);
            		if(duration.toMinutes() > (60*2)) {
            			sendMail = true;
            		}
        		}
        		if(sendMail)
        		emailService.sendSimpleMessage("admin@haarmk.com", "name.com API call limit exceded", "Name.com API call limit exceded.\nPlease contact at accountservices@name.com");
        	}
        	
        	NameExceptionDetails res = objectMapper.readValue(httpResponse.getBody(), NameExceptionDetails.class);
        	
        	throw new NameException(res);
            
        }
    }
}
