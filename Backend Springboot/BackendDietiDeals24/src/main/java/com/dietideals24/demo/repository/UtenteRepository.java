package com.dietideals24.demo.repository;

import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dietideals24.demo.models.Asta;
import com.dietideals24.demo.models.Utente;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Integer>{
	
	@Query("SELECT u FROM Utente u WHERE u.email = :email AND u.password = :password")
    Optional<Utente> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
	
	@Query("SELECT u FROM Utente u WHERE u.email = :email AND u.id = :id")
    Optional<Utente> findByEmailAndId(@Param("email") String email, @Param("id") int id);
  /* Utente newRegistrazioneVenditore(Utente entity);
   
   Utente newRegistrazioneCompratore(Utente entity);
   
   Optional<Utente> findByEmail(String email);
   
   Optional<Utente> findByUsername(String username);
   
   boolean checkLogin(String email, String password, String tipo);

   boolean existsByEmailAndTipo(String email,String tipo);
   
   List<Asta> ricercaAste(String categoria,String key);
   
   List<Utente> newAsta(Utente u ,Asta asta,String tipo);*/
}
