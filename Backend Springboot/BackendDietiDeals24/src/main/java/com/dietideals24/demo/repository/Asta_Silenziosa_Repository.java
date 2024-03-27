package com.dietideals24.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Silenziosa;

@Repository
public interface Asta_Silenziosa_Repository extends CrudRepository<Asta_Silenziosa, Integer>{

//	Asta creaAstaSilenziosa(Asta asta);
	
}
