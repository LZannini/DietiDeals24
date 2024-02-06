package Repository_Implements;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.stereotype.Service;

import Models.Asta;
import Models.Asta_Inversa;
import Repository.Asta_Inversa_Repository;
import Repository.Asta_Repository;

@Service
public class Asta_Inversa_Implementation implements Asta_Inversa_Repository{

	@Autowired
	private Asta_Repository astaRep;
	
	@Override
	public Asta creaAstaInversa(Asta asta) {
		
		if(!(asta instanceof Asta_Inversa))
		throw new IllegalArgumentException("L'oggetto Asta deve essere un'istanza di Asta_Inversa!");
		
		Asta_Inversa astainv = (Asta_Inversa) asta;
		
		if(astainv.getPrezzo()<= 0)
		throw new IllegalArgumentException("Il prezzo iniziale deve essere maggiore di zero!");
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime scadenza = astainv.getScadenza();
		if(scadenza != null && now.isAfter(scadenza))
			throw new IllegalArgumentException("La data di scadenza deve essere nel futuro!");
		
        /*INSERIRE NEL PARAMETRO IL FILE DELL'IMMAGINE
         * astainv.setFoto(byteArrayImmagine);
         */
		
		return astaRep.save(astainv);
	}
	
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Asta_Inversa> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Asta_Inversa> entities) {
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
	public Asta_Inversa getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Asta_Inversa getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Asta_Inversa getReferenceById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asta_Inversa> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asta_Inversa> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Asta_Inversa> findById(Integer id) {
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
	public void delete(Asta_Inversa entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Asta_Inversa> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Asta_Inversa> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Asta_Inversa> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends Asta_Inversa> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Inversa> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Asta_Inversa> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Asta_Inversa, R> R findBy(Example<S> example,
			Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

}
