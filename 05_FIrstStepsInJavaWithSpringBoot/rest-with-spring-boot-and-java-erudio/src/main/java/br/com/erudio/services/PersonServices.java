package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repositories.PersonRepository;

@Service
public class PersonServices {;
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper mapper;
	
	public List<PersonVO> findAll() {
		logger.info("Finding all people");
	
		return DozerMapper.parseListObjects(repository.findAll(),PersonVO.class);
	}

	public PersonVO findById(Long id) {
		logger.info("Finding one person");
		
		Person p = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
		
		return DozerMapper.parseObject(p, PersonVO.class);
	}
	

	public PersonVO create(PersonVO person) {
		logger.info("Creating a person");
		
		Person p = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(p), PersonVO.class); 
		
		return vo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating a person with V2");
		
		Person p = mapper.convertVoToEntity(person);
		var vo = mapper.convertEntityToVo(repository.save(p)); 
		
		return vo;
	}
	
	public PersonVO update(PersonVO person) {
		logger.info("Updating one person");
		
		
		Person p = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
		
		p.setFirstName(person.getFirstName());
		p.setLastName(person.getLastName());
		p.setAddress(person.getAddress());
		p.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(p), PersonVO.class); 
		
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person");
		
		Person p = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
		
		repository.delete(p);
	}
	
	
}
