package Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Models.Asta;
import Models.Asta_Silenziosa;

@Repository
public interface Asta_Silenziosa_Repository extends JpaRepository<Asta_Silenziosa, Integer>{

	Asta creaAstaSilenziosa(Asta asta);
	
}
