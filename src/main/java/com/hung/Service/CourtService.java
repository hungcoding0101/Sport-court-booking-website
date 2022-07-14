package com.hung.Service;

import java.util.List;

import com.hung.Models.Court;

public interface CourtService {

	public List<Court> findAll();
	public Court find(String code);
	public Court findEagerly(String code) ;
	public List<Court> findByProperty(String propertyName, Object value);
	public void save(Court c);
	public void delete(String code);
	public void update(Court c);
}
