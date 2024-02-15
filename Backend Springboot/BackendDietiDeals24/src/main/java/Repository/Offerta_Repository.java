package Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Models.Offerta;

@Repository
public interface Offerta_Repository extends JpaRepository<Offerta, Integer>{

}
