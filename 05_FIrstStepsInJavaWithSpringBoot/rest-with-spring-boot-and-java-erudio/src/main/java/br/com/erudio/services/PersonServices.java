package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
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
	
	public List<PersonVO> findAll() throws Exception{
		logger.info("Finding all people");
		
		List<PersonVO> entitys = DozerMapper.parseListObjects(repository.findAll(),PersonVO.class);
	
		for(PersonVO vo : entitys) {
			vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		}
	
		return entitys;
	}

	public PersonVO findById(Long id) throws Exception{
		logger.info("Finding one person");
		
		Person p = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
		
		PersonVO vo = DozerMapper.parseObject(p, PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		
		return vo;
	}
	

	public PersonVO create(PersonVO person) throws Exception {
		logger.info("Creating a person");
		
		if(person == null) throw new RequiredObjectIsNullException();
		
		Person p = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repository.save(p), PersonVO.class); 
		
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	
	public PersonVO update(PersonVO person) throws Exception {
		logger.info("Updating one person");
		
		if(person == null) throw new RequiredObjectIsNullException();
		
		Person p = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
		
		p.setFirstName(person.getFirstName());
		p.setLastName(person.getLastName());
		p.setAddress(person.getAddress());
		p.setGender(person.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(p), PersonVO.class); 
		
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		
		return vo;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one person");
		
		Person p = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this id!"));
		
		repository.delete(p);
	}
	
	
}
