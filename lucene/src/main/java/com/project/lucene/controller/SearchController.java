package com.project.lucene.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lucene.model.ResultItem;
import com.project.lucene.service.SearchService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
	
	private SearchService searchService;
	
	@GetMapping(value = "/page")
	private ResponseEntity<ResultItem> searchResults(
			@RequestParam(defaultValue = "") String searchTerm, 
			@RequestParam(defaultValue = "1") Integer pageNum, 
            @RequestParam(defaultValue = "3") Integer pageSize){
		return new ResponseEntity<>(searchService.retrieveSearchResults(searchTerm, pageNum, pageSize),
				HttpStatus.OK);
	}
	
}
