package com.dietideals24.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Asta_Ribasso;

@Repository
public interface Asta_Ribasso_Repository extends CrudRepository<Asta_Ribasso, Integer>{

	/*Asta creaAstaRibasso(Asta asta);
	
	void decrementoPrezzo(Asta_Ribasso asta);*/
}
