package Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Models.Asta;
import Models.Asta_Inversa;

@Repository
public interface Asta_Inversa_Repository extends JpaRepository<Asta_Inversa, Integer>{

	Asta creaAstaInversa(Asta asta);
	
}
