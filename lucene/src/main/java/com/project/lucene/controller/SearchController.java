package com.project.lucene.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.lucene.service.SearchService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/search")
@AllArgsConstructor
public class SearchController {
	
	private SearchService searchService;
	
	@GetMapping(value = "/page")
	private ResponseEntity<List<String>> getAllProjects(@RequestParam(defaultValue = "") String searchTerm){
		return new ResponseEntity<>(searchService.retrieveSearchResults(searchTerm),
				HttpStatus.OK);
	}
	
}
