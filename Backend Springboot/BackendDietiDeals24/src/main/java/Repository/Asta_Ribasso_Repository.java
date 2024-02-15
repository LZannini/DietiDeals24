package Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Models.Asta;
import Models.Asta_Ribasso;

@Repository
public interface Asta_Ribasso_Repository extends JpaRepository<Asta_Ribasso, Integer>{

	Asta creaAstaRibasso(Asta asta);
	
}
