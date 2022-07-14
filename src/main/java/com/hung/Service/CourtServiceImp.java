package com.hung.Service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hung.DAO.CourtDAO;
import com.hung.Models.Court;

@Service
@Transactional
@EnableTransactionManagement
public class CourtServiceImp implements CourtService{

	@Autowired
	CourtDAO dao;
	
	@Override
	public List<Court> findAll() {
		return dao.findAll();
	}

	@Override
	public Court find(String code) {
		return dao.find(code);
	}

	@Override
	public Court findEagerly(String code) {
		return dao.findEagerly(code);
	}
	
	@Override
	public List<Court> findByProperty(String propertyName, Object value) {
		return dao.findByProperty(propertyName, value);
	}

	@Override
	public void save(Court c) {
		dao.save(c);
	}

	@Override
	public void delete(String code) {
		dao.delete(code);
	}

	@Override
	public void update(Court c) {
		dao.update(c);
	}

}
