package Repository;

import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.data.jpa.repository.JpaRepository;

import Models.Utente;

public interface Utente_Repository extends JpaRepository<Utente, Integer>{
	
   Utente newRegistrazioneVenditore(Utente entity);
   
   Utente newRegistrazioneCompratore(Utente entity);
   
   Optional<Utente> findByEmail(String email);
   
   boolean checkLogin(String email, String password, String tipo);

   boolean existsByEmailAndTipo(String email,String tipo);
}