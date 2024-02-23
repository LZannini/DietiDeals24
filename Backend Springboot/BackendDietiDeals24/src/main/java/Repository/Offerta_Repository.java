package Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Models.Asta;
import Models.Offerta;
import Models.Utente;

@Repository
public interface Offerta_Repository extends JpaRepository<Offerta, Integer>{
  
	 Offerta presentaOfferta(Utente utente,Asta asta,float importo);
}
