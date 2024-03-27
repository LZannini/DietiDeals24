package com.dietideals24.demo.controllers;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dietideals24.demo.models.*;
import com.dietideals24.demo.repository.*;

@RestController
@RequestMapping("/api")
public class AppController {
	
	@GetMapping("/")
	public ResponseEntity<String> handledefault() {
	    String errorMessage = "Non si dice!";
	    return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
	}
}
