package Repository;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.data.jpa.repository.JpaRepository;

import Models.Asta;
import Models.Utente;

public interface Utente_Repository extends JpaRepository<Utente, Integer>{
	
   Utente newRegistrazioneVenditore(Utente entity);
   
   Utente newRegistrazioneCompratore(Utente entity);
   
   Optional<Utente> findByEmail(String email);
   
   Optional<Utente> findByUsername(String username);
   
   boolean checkLogin(String email, String password, String tipo);

   boolean existsByEmailAndTipo(String email,String tipo);
   
   List<Asta> ricercaAste(String categoria,String key);
}
