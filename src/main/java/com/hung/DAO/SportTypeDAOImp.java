package com.hung.DAO;

import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.IdentifierEqExpression;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hung.Models.Court;
import com.hung.Models.Player;
import com.hung.Models.Reservation;
import com.hung.Models.SportType;
import com.hung.Models.SportType;

@Repository

@EnableTransactionManagement
public class SportTypeDAOImp implements SportTypeDAO{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<SportType> findAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<SportType> CQ = CB.createQuery(SportType.class);
		Root<SportType> root = CQ.from(SportType.class);
		
		CQ.orderBy(CB.asc(root.get("id"))).select(root);
		Query<SportType> query = session.createQuery(CQ);
		return query.getResultList();
	}

	@Override
	public SportType find(String name) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<SportType> CQ = CB.createQuery(SportType.class);
		Root<SportType> root = CQ.from(SportType.class);
		CQ.select(root).where(CB.equal(root.get("name"), CB.parameter(String.class, "name")));
		
		Query query = session.createQuery(CQ);
		query.setParameter("name", name);
		return 	(SportType) query.getSingleResult();
	}
	

	@Override
	public SportType findEagerly(String name) {
		Session session = sessionFactory.getCurrentSession();
//		EntityGraph entityGraph = session.getEntityGraph("SportType.court.forview");
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<SportType> CQ = CB.createQuery(SportType.class);
		Root<SportType> root = CQ.from(SportType.class);
		root.fetch("courts", JoinType.INNER);
		CQ.select(root).where(CB.equal(root.get("name"), CB.parameter(String.class, "name")));
		
		Query query = session.createQuery(CQ);
		query.setParameter("name", name);
//		query.setHint("javax.persistence.loadgraph", entityGraph);
		return 	(SportType) query.getSingleResult();
	}

	@Override
	public List<SportType> findByProperty(String propertyName, Object value) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder CB = session.getCriteriaBuilder();
		CriteriaQuery<SportType> CQ = CB.createQuery(SportType.class);
		Root<SportType> root = CQ.from(SportType.class);
		
		CQ.select(root).where(CB.equal(root.get(propertyName), value));
		
		Query query = session.createQuery(CQ);
		List<SportType> result = query.getResultList();	
		return result;
	}

	@Override
	public Long save(SportType sp) {
		Session session = sessionFactory.getCurrentSession();
		return (Long) session.save(sp);
	}

	@Override
	public void update(SportType sp) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(sp);
	}

	@Override
	public void delete(String name) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("delete from SportType where name = :name");
		query.setParameter("name", name);
		query.executeUpdate();
	}






}
