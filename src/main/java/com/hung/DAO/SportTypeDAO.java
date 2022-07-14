package com.hung.DAO;

import java.util.List;

import com.hung.Models.Court;
import com.hung.Models.Reservation;
import com.hung.Models.SportType;

public interface SportTypeDAO {

	public SportType find(String name);

	public SportType findEagerly(String name);
	public List<SportType> findAll();
	public List<SportType> findByProperty(String propertyName, Object value);
	public Long save(SportType sp);
	public void update(SportType sp);
	public void delete(String name);
}
