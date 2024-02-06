package Repository_Implements;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import Models.Asta_Ribasso;
import Repository.Asta_Repository;
import Repository.Asta_Ribasso_Repository;


@Service
public class Asta_Ribasso_Implementation implements Asta_Ribasso_Repository{

	@Autowired 
	private Asta_Repository astaRep;
	
	@Override
	public Asta creaAstaRibasso(Asta asta) {
		
		if(!(asta instanceof Asta_Ribasso))
			throw new IllegalArgumentException("L'oggetto Asta deve essere un'istanza di AstaRibasso");
		
		Asta_Ribasso astarib = (Asta_Ribasso) asta;
		
		if(astarib.getPrezzo() <= 0)
			throw new IllegalArgumentException("Il prezzo deve essere maggiore di zero!");
		
		if(astarib.getDecremento() <= 0)
			throw new IllegalArgumentException("L'importo per ciascun decremento deve essere maggiore di zero!");
		
		if(astarib.getMinimo() <= 0)
			throw new IllegalArgumentException("Il prezzo minimo segreto deve essere maggiore di zero!");
			
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime timer = now.plusHours(1);
		astarib.setTimer(timer);
		
		if(now.isAfter(timer))
			throw new IllegalArgumentException("Il timer è scaduto per questa Asta Ribasso!");
		
		return astaRep.save(astarib);
	}
	
	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Asta_Ribasso> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Asta_Ribasso> entities) {
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
	public Asta_Ribasso getOne(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Asta_Ribasso getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Asta_Ribasso getReferenceById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asta_Ribasso> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asta_Ribasso> findAllById(Iterable<Integer> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Asta_Ribasso> findById(Integer id) {
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
	public void delete(Asta_Ribasso entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Integer> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Asta_Ribasso> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Asta_Ribasso> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Asta_Ribasso> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public <S extends Asta_Ribasso> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Asta_Ribasso> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Asta_Ribasso> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <S extends Asta_Ribasso, R> R findBy(Example<S> example,
			Function<FetchableFluentQuery<S>, R> queryFunction) {
		// TODO Auto-generated method stub
		return null;
	}

}
