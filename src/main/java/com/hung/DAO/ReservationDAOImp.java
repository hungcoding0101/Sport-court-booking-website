package com.hung.DAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.persistence.FetchType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.FetchStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import com.hung.Models.Court;
import com.hung.Models.Reservation;

@Repository
@Transactional
public class ReservationDAOImp implements ReservationDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> findAll(boolean fetchOrNot) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<Reservation> CQ = CB.createQuery(Reservation.class);
		Root<Reservation> root = CQ.from(Reservation.class);
		
		if(fetchOrNot == true) {
			root.fetch("player", JoinType.LEFT);
			root.fetch("court", JoinType.LEFT);
			root.fetch("sportType", JoinType.LEFT);
		}
		
		Order order1 = CB.asc(root.get("date"));
		Order order2 = CB.asc(root.get("StartTime"));
		Order[] orders = {order1, order2};
		
		CQ.select(root).orderBy(orders);
		
		Query query = session.createQuery(CQ);
		return query.getResultList();
	}
	
	@Override
	public Reservation findById(int id, boolean fetchOrNot) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<Reservation> CQ = CB.createQuery(Reservation.class);
		Root<Reservation> root = CQ.from(Reservation.class);
		
		if(fetchOrNot == true) {
			root.fetch("player", JoinType.LEFT);
			root.fetch("court", JoinType.LEFT);
			root.fetch("sportType", JoinType.LEFT);
		}
		
		CQ.select(root).where(CB.equal(root.get("id"), id));
		
		Query query = session.createQuery(CQ);
		List<Reservation> result = query.getResultList();
		if(result.isEmpty()) {return null;}
		return result.get(0);
	}
	

	@Override
		public List<Reservation> findByProperty(String propertyName, Object value, boolean trueIsAscending_falseIsDescending, boolean fetchOrNot) {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder();
			CriteriaQuery<Reservation> CQ = CB.createQuery(Reservation.class);
			Root<Reservation> root = CQ.from(Reservation.class);
			
			if(fetchOrNot == true) {
				root.fetch("player", JoinType.LEFT);
				root.fetch("court", JoinType.LEFT);
				root.fetch("sportType", JoinType.LEFT);
			}
			
			if(trueIsAscending_falseIsDescending == true) {CQ.orderBy(CB.asc(root.get(propertyName)));}
			else{CQ.orderBy(CB.desc(root.get(propertyName)));}
			
			CQ.select(root).where(CB.equal(root.get(propertyName), value));
			
			Query query = session.createQuery(CQ);
			List<Reservation> result = query.getResultList();		
			return result;
			
		}
	
	@Override
		public List<Reservation> findByProperties(Object[][] conditions, boolean fetchOrNot) throws IllegalArgumentException {
				Session session = sessionFactory.getCurrentSession();
				CriteriaBuilder CB = session.getCriteriaBuilder();
				CriteriaQuery<Reservation> CQ = CB.createQuery(Reservation.class);
				Root<Reservation> root = CQ.from(Reservation.class);
				
				if(fetchOrNot == true) {
					root.fetch("player", JoinType.LEFT);
					root.fetch("court", JoinType.LEFT);
					root.fetch("sportType", JoinType.LEFT);
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
			List<Reservation> result = query.getResultList();		
			return result;
		}
	
	@Override
		public Reservation findByCourt_Date_Time(Court court, LocalDate date, LocalTime StartTime, boolean fetchOrNot) {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder();
			CriteriaQuery<Reservation> CQ = CB.createQuery(Reservation.class);
			Root<Reservation> root = CQ.from(Reservation.class); 
			
			if(fetchOrNot == true) {
				root.fetch("player", JoinType.LEFT);
				root.fetch("court", JoinType.LEFT);
				root.fetch("sportType", JoinType.LEFT);
			}
			
			Predicate[] conditions = new Predicate[3];
			conditions[0] = CB.equal(root.get("court"), CB.parameter(Court.class, "court"));
			conditions[1] = CB.equal(root.get("date"), CB.parameter(LocalDate.class, "date"));
			conditions[2] = CB.equal(root.get("StartTime"), CB.parameter(LocalTime.class, "StartTime"));
			
			CQ.select(root).where(conditions);
			
			Query query = session.createQuery(CQ);
			query.setParameter("court", court);
			query.setParameter("date", date);
			query.setParameter("StartTime", StartTime);
			
			return (Reservation) query.getSingleResult();	
		}
	

	@Override
		public void save(Reservation theOne) {
			Session session = sessionFactory.getCurrentSession();
			session.persist(theOne);
		}
	
	@Override
		public Reservation update(Reservation reservation) {
			Session session = sessionFactory.getCurrentSession();
			return (Reservation) session.merge(reservation);	
		}
	
	@Override
	public int updateByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr, String targetField, Object newValue) {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder(); 
			CriteriaUpdate<Reservation> CD = CB.createCriteriaUpdate(Reservation.class);
			Root<Reservation> root = CD.from(Reservation.class);
		
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
			Reservation reservation = session.get(Reservation.class, id);	
			session.delete(reservation);
		}

	@Override
		public int deleteByProperties(Map<String, Object> conditions, boolean TrueIsAnd_FalseIsOr) {
			Session session = sessionFactory.getCurrentSession();
			CriteriaBuilder CB = session.getCriteriaBuilder(); 
			CriteriaDelete<Reservation> CD = CB.createCriteriaDelete(Reservation.class);
			Root<Reservation> root = CD.from(Reservation.class);
			
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
