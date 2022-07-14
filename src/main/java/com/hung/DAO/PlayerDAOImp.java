package com.hung.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hung.Models.Player;
import com.hung.Models.Reservation;
import com.hung.Service.PlayerServiceImp;
import com.hung.Models.Player;

@Repository
@EnableTransactionManagement
public class PlayerDAOImp implements PlayerDAO, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PlayerDAOImp.class);
	private String name;
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Player findById(int id, boolean fetchOrNot) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<Player> CQ = CB.createQuery(Player.class);
		Root<Player> root = CQ.from(Player.class);
		
		if(fetchOrNot == true) {
			root.fetch("reservations", JoinType.LEFT);
		}
		
		CQ.select(root).where(CB.equal(root.get("id"), id));
		
		Query query = session.createQuery(CQ);
		List<Player> result = query.getResultList();
		if(result.isEmpty()) {return null;}
		return result.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
		public List<Player> findAll(boolean fetchOrNot) {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder();
			CriteriaQuery<Player> CQ = CB.createQuery(Player.class);
			Root<Player> root = CQ.from(Player.class);
			
			if(fetchOrNot == true) {
				root.fetch("reservations", JoinType.LEFT);
			}
			
			CQ.select(root);
			Query query = session.createQuery(CQ);
			return query.getResultList();
		}

	@SuppressWarnings("unchecked")
	@Cacheable(cacheNames = "player")
	@Override
		public Player findByNameAndPassword(String name, String password, boolean fetchOrNot) {
		System.out.println("\nHERE: FINDING PLAYER\n");
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder();
			CriteriaQuery<Player> CQ = CB.createQuery(Player.class);	
			Root<Player> root = CQ.from(Player.class); 
			
			if(fetchOrNot == true) {
				root.fetch("reservations", JoinType.LEFT);
			}
			
			Predicate name_restriction = CB.equal(root.get("name"), CB.parameter(String.class, "name"));
			Predicate password_restriction = CB.equal(root.get("password"), CB.parameter(String.class, "password"));
			
			CQ.select(root).where(CB.and(name_restriction, password_restriction));
			
			Query query = session.createQuery(CQ);
			query.setParameter("name", name);
			query.setParameter("password", password);
			List<Player> result = query.getResultList();
			if(result.isEmpty()) {return null;}
			return result.get(0);
		}
	
	@Override
		public List<Player> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending,
																	boolean fetchOrNot) {
		
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder();
			CriteriaQuery<Player> CQ = CB.createQuery(Player.class);
			Root<Player> root = CQ.from(Player.class);
			
			if(fetchOrNot == true) {
				root.fetch("reservations", JoinType.LEFT);
			}
			
			if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
			else{CQ.orderBy(CB.desc(root.get(propertyName)));}
			
			CQ.select(root).where(CB.equal(root.get(propertyName), value));
			
			Query query = session.createQuery(CQ);
			return query.getResultList();
		}
	
	
	@Override
		public List<Player> findByProperties(Object[][] conditions, boolean fetchOrNot){
		
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder();
			CriteriaQuery<Player> CQ = CB.createQuery(Player.class);
			Root<Player> root = CQ.from(Player.class);
			
			if(fetchOrNot == true) {
				root.fetch("reservations", JoinType.LEFT);
			}
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			List<Order> orders = new ArrayList<Order>();
			String propertyName = "";
			Object value = null;
			org.hibernate.criterion.Order order = null;
			
			for(Object[] condition: conditions) {
				if(!(condition[0] instanceof String) || condition[0] == null || condition[0].equals("") || condition[1] == null) {
					continue;
				}	
				 propertyName = (String) condition[0];
				 value = condition[1];
				 order = (org.hibernate.criterion.Order) condition[2];
				 
				predicates.add(CB.equal(root.get(propertyName), value));
				
				if(order.equals(org.hibernate.criterion.Order.asc(propertyName))) {
					orders.add(CB.asc(root.get(propertyName)));
				}
				else {orders.add(CB.desc(root.get(propertyName)));}
		}
		
		CQ.orderBy(orders);
		CQ.select(root).where(predicates.toArray(new Predicate[] {}));
		
		Query query = session.createQuery(CQ);
		return query.getResultList();
	}
		
	
	@Override
	public int save(Player theOne) {
		Session session = sessionFactory.getCurrentSession();	
		return (int) session.save(theOne);
	}
	
	@Override
		public void reconnect(Player player) {
			Session session = sessionFactory.getCurrentSession();	
			session.refresh(player);
		}
	
	@Override
	public Player update(Player player) {
		Session session = sessionFactory.getCurrentSession();
		return (Player) session.merge(player);
	}

	@Override
		public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField,
															Object newValue) {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder(); 
			CriteriaUpdate<Player> CD = CB.createCriteriaUpdate(Player.class);
			Root<Player> root = CD.from(Player.class);
		
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			conditions.forEach((s,o) -> {
				Predicate predicate = CB.equal(root.get(s), o);
				predicates.add(predicate);
			});
			
			Predicate finalPredicate;
			
			if(TrueIsAnd_FalseIsOr == true) {finalPredicate = CB.and(predicates.toArray(new Predicate[] {}));}
			else {finalPredicate = CB.or(predicates.toArray(new Predicate[] {}));}
			
			CD.set(targetField, newValue);
			CD.where(finalPredicate);
			return session.createQuery(CD).executeUpdate();
	}
	
	@Override
	public void delete(int id) {
		Session session = sessionFactory.getCurrentSession();
		Player reservation = session.get(Player.class, id);	
		session.delete(reservation);		
	}

	
	
	@Override
		public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
		
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder(); 
			CriteriaDelete<Player> CD = CB.createCriteriaDelete(Player.class);
			Root<Player> root = CD.from(Player.class);
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			conditions.forEach((s,o) -> {
				Predicate predicate = CB.equal(root.get(s), o);
				predicates.add(predicate);
			});
			
			Predicate finalPredicate;
			if(TrueIsAnd_FalseIsOr == true) {finalPredicate = CB.and(predicates.toArray(new Predicate[] {}));}
			else {finalPredicate = CB.or(predicates.toArray(new Predicate[] {}));}
			
			CD.where(finalPredicate);
			return session.createQuery(CD).executeUpdate();
	}

}
