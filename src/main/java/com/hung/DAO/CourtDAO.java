package com.hung.DAO;

import java.util.List;

import com.hung.Models.Court;
import com.hung.Models.SportType;

public interface CourtDAO {

	public Court find(String code);
	public Court findEagerly(String code);
	public List<Court> findAll();
	public List<Court> findByProperty(String propertyName, Object value);
	public Long save(Court sp);
	public void update(Court sp);
	public void delete(String code);
}
