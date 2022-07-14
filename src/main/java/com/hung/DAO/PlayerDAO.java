package com.hung.DAO;

import java.util.List;
import java.util.Map;

import com.hung.Models.Player;
import com.hung.Models.Reservation;

public interface PlayerDAO {
	
	public Player findById(int id, boolean fetchOrNot);
	public Player findByNameAndPassword(String name, String password, boolean fetchOrNot);
	public List<Player> findAll(boolean fetchOrNot);
	public List<Player> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot);
	public List<Player> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException;
	public int save(Player theOne);
	public void reconnect(Player player);
	public Player update(Player player);
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField, Object newValue);
	public void delete(int id);
	public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr);
}
