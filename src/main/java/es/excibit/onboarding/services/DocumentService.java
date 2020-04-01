package es.excibit.onboarding.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.excibit.onboarding.pojo.Document;

@Service
public class DocumentService {

	@Value("${documentvalidator.queue.name}")
	private String queueName;
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	/**
	 * Add Document to JMS
	 * 
	 * @param document
	 * 
	 * @return {@linkplain Document}
	 * 
	 * @throws Exception
	 */
	public ResponseEntity<String> addDocument(Document document) throws Exception {

		rabbitTemplate.convertAndSend(queueName, document);
		
		return new ResponseEntity<String>("Document added!", HttpStatus.OK);

	}

}
