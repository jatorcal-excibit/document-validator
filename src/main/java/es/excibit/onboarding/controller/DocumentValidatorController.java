package es.excibit.onboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.excibit.onboarding.pojo.Document;
import es.excibit.onboarding.services.DocumentService;

@RestController
public class DocumentValidatorController {

	@Autowired
	DocumentService documentService;
	
	@PostMapping("/document")
	public ResponseEntity<String> document(@RequestBody Document document) throws Exception {
		return documentService.addDocument(document);
	}
}
