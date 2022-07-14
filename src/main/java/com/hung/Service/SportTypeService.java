package com.hung.Service;

import java.util.List;

import com.hung.Models.Reservation;
import com.hung.Models.SportType;


public interface SportTypeService {

	public List<SportType> findAll();
	public SportType find(String name);
	public SportType findEagerly(String name);
	public List<SportType> findByProperty(String propertyName, Object value);
	public void save(SportType sp);
	public void delete(String name);
	public void update(SportType sp);
}
