package Repository_Implements;

import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.naming.AuthenticationException;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Models.Utente;
import Repository.Asta_Inversa_Repository;
import Repository.Asta_Repository;
import Repository.Asta_Ribasso_Repository;
import Repository.Asta_Silenziosa_Repository;
import Repository.Utente_Repository;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;

@Service
public class Utente_Implementation implements Utente_Repository{

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private Utente_Repository utente;
	@Autowired
	private AuthenticationManager authenticationmanager;
	
	@Override
    @Transactional
    public Utente newRegistrazioneVenditore(Utente venditore) {
        validateUtente(venditore);
        venditore.setTipo("Venditore");
        return saveAndFlush(venditore);
    }

    @Override
    @Transactional
    public Utente newRegistrazioneCompratore(Utente compratore) {
        validateUtente(compratore);
        compratore.setTipo("Compratore");
        return saveAndFlush(compratore);
    }
    
    
    private void validateUtente(Utente utente) {
    
        Set<ConstraintViolation<Utente>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(utente);

        if (!violations.isEmpty()) 
            throw new ConstraintViolationException(violations);
        

        // Altri controlli 
    }
    
    @Override
    public boolean checkLogin(String email, String password, String tipo) {
    	
    	try {
        org.springframework.security.core.Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        org.springframework.security.core.Authentication authenticated = authenticationmanager.authenticate(authentication);

        // Verifica il tipo di utente e assegna i ruoli corrispondenti
        if (tipo.equals("venditore")) {
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return true;
        } else if (tipo.equals("compratore")) {
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return true;
        } else 
        	return false;
            
    	}catch(AuthenticationCredentialsNotFoundException e) {
    		throw new AuthenticationCredentialsNotFoundException("Autenticazione fallita!");
    	}
        

    }
    
    
    public class WrongPasswordException extends RuntimeException {
        public WrongPasswordException(String message) {
            super(message);
        }
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
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
	public Optional<Utente> findByEmail(String email) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existsByEmailAndTipo(String email, String tipo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Utente getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
