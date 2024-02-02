package Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Models.Utente;

public interface Utente_Repository extends JpaRepository<Utente, Integer>{
	
   Utente register(Utente entity);
   
   Utente findByEmail(String email);
   
   boolean checkLogin(String email,String password,String tipo);

   boolean existsByEmailAndTipo(String email,String tipo);
}
