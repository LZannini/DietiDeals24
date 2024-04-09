package com.dietideals24.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dietideals24.demo.models.Notifica;

import jakarta.transaction.Transactional;

public interface NotificaRepository extends CrudRepository<Notifica, Integer> {

	@Query("SELECT n FROM Notifica n WHERE n.id_utente = :id_utente")
	List<Notifica> trovaNotifiche(@Param("id_utente") int id_utente);
	
	@Query("SELECT n FROM Notifica n WHERE n.id_utente = :id_utente AND n.letta = true")
	List<Notifica> trovaNotificheLette(@Param("id_utente") int id_utente);
	
	@Query("SELECT n FROM Notifica n WHERE n.id_utente = :id_utente AND n.letta = false")
	List<Notifica> trovaNotificheNonLette(@Param("id_utente")int id_utente);
	
	@Transactional 
	@Modifying
	@Query("UPDATE Notifica n SET n.letta = true WHERE n.id = :id")
	void segnaNotificaComeLetta(@Param("id") int id);
	
	@Query("DELETE FROM Notifica n WHERE n.id = :id")
	void eliminaNotifica(@Param("id") int id);
	
	@Query("DELETE FROM Notifica n WHERE n.id_utente = :id_utente")
	void eliminaNotifiche(@Param("id_utente") int id_utente);
	
	@Query("DELETE FROM Notifica n WHERE n.id_utente = :id_utente AND n.letta = true")
	void eliminaNotificheLette(@Param("id_utente") int id_utente);
}
