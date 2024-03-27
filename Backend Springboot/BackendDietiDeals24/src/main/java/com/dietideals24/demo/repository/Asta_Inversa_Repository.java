package com.dietideals24.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Inversa;

@Repository
public interface Asta_Inversa_Repository extends CrudRepository<Asta_Inversa, Integer>{

	//Asta creaAstaInversa(Asta asta);
	
}
