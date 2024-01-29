package Repository_Implements;

import java.util.List;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Models.Utente;
import Repository.Utente_Repository;

@Service
public class Utente_Implementation implements Utente_Repository{

	@Autowired
	private Utente_Repository utente;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Utente newRegistrazioneVenditore(Utente venditore) {
		
		if(utente.existsByEmailAndTipo(venditore.getEmail(),"Venditore")) {
			throw new EmailDuplicataException("Questa email è già in uso per un account Venditore!");
		}
		
		if(venditore.getPassword().length() < 4) {
			throw new IllegalArgumentException("La password deve contenere almeno 4 caratteri!");
		}
		
		if (!venditore.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
			throw new IllegalArgumentException("La password deve contenere almeno un carattere speciale!");
		}
		
		venditore.setPassword(passwordEncoder.encode(venditore.getPassword()));
		
		venditore.setTipo("Venditore");
		
		return utente.save(venditore);
	}
	
	public Utente newRegistrazioneCompratore(Utente compratore) {
		
		if(utente.existsByEmailAndTipo(compratore.getEmail(), "Compratore")) {
			throw new EmailDuplicataException("Questa email è già in uso per un account Compratore!");
		}
		
		if(compratore.getPassword().length() < 4) {
			throw new IllegalArgumentException("La password deve contenere almeno 4 caratteri!");
		}
		
		if (!compratore.getPassword().matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
			throw new IllegalArgumentException("La password deve contenere almeno un carattere speciale!");
		}
		
		compratore.setPassword(passwordEncoder.encode(compratore.getPassword()));
		
		compratore.setTipo("Compratore");
	    
		return utente.save(compratore);
	}
	
	public class EmailDuplicataException extends RuntimeException{
		public EmailDuplicataException(String message) {
			super(message);
		}
	}
	
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Utente> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Utente> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Utente getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente getReferenceById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Utente> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Utente> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Utente> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsById(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Utente entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Utente> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Utente> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Utente> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends Utente> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Utente> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Utente> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Utente, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Utente register(Utente entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsByEmailAndTipo(String email, String tipo) {
		// TODO Auto-generated method stub
		return false;
	}

}
