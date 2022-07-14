package com.hung.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hung.DAO.SportTypeDAO;
import com.hung.Models.Reservation;
import com.hung.Models.SportType;

@Service
@Transactional
@EnableTransactionManagement
public class SportTypeServiceImp implements SportTypeService{

	@Autowired
	SportTypeDAO dao;

	@Override
	public List<SportType> findAll() {
		return dao.findAll();
	}

	@Override
	public SportType find(String name) {
		return dao.find(name);
	}
	
	@Override
	public SportType findEagerly(String name) {
		return dao.findEagerly(name);
	}

	@Override
	public List<SportType> findByProperty(String propertyName, Object value) {
		return dao.findByProperty(propertyName, value);
	}

	@Override
	public void save(SportType sp) {
		dao.save(sp);
	}

	@Override
	public void delete(String name) {
		dao.delete(name);
	}

	@Override
	public void update(SportType sp) {
		dao.update(sp);
	}



}
