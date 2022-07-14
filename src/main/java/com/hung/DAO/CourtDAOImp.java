package com.hung.DAO;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hung.Models.Court;
import com.hung.Models.Court;

@Repository

@EnableTransactionManagement
public class CourtDAOImp implements CourtDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public Court find(String code) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<Court> CQ = CB.createQuery(Court.class);
		Root<Court> root = CQ.from(Court.class);
		CQ.select(root).where(CB.equal(root.get("code"), CB.parameter(String.class, "code")));
		
		Query query = session.createQuery(CQ);
		query.setParameter("code", code);
		return 	(Court) query.getSingleResult();
	}

	@Override
	public Court findEagerly(String code) {
		Session session = sessionFactory.getCurrentSession();
		Query<Court> query = session.createQuery("SELECT court FROM Court court LEFT JOIN FETCH court.sportType "
				+ "LEFT JOIN FETCH court.reservations "
				+ "where code = :code", Court.class);
		query.setParameter("code", code);

		Court result = null;
		try {
			result = query.getSingleResult();
		}catch (NoResultException e) {
			e.printStackTrace();
			Query<Court> newquery = session.createQuery("SELECT court FROM Court court JOIN FETCH court.sportType "
					+ "where code = :code", Court.class);
			newquery.setParameter("code", code);
			result = newquery.getSingleResult();
		}
		 	return result;
	}
	
	@Override
	public List<Court> findAll() {
		Session session = sessionFactory.getCurrentSession();
		Query<Court> query = session.createQuery("FROM Court", Court.class);
		return query.getResultList();
	}

	@Override
	public List<Court> findByProperty(String propertyName, Object value) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<Court> CQ = CB.createQuery(Court.class);
		Root<Court> root = CQ.from(Court.class);
		
		CQ.select(root).where(CB.equal(root.get(propertyName), value));
		
		Query query = session.createQuery(CQ);
		List<Court> result = query.getResultList();
		
		return result;
	}

	@Override
	public Long save(Court c) {
		Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(c);
	}

	@Override
	public void update(Court c) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(c);
		
	}

	@Override
	public void delete(String code) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete from Court where name = :name");
		query.setParameter("code", code);
		query.executeUpdate();
		
	}




}
