package com.dietideals24.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Offerta;
import com.dietideals24.demo.models.Utente;

@Repository
public interface OffertaRepository extends CrudRepository<Offerta, Integer>{
  
//	 Offerta presentaOfferta(Utente utente,Asta asta,float importo);
//	 List<Offerta> getOfferte(int id_asta);
	 @Query("SELECT o FROM Offerta o WHERE o.id_asta = :id_asta")
	 List<Offerta> trovaOfferte(int id_asta);
}
