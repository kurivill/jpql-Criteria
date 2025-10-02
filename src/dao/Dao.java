package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import util.creds_provider;

import entity.*;
import java.util.*;

public class Dao {


	
	public void addRegister(Register reg) {
		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();
		
        em.persist(reg);
        
        em.getTransaction().commit();
        em.close();
	}
	
	public void addEvent(int eventNumber, int regNumber, double amount) {
		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();
		
        Register reg = em.find(Register.class, regNumber);
        SalesEvent evt = new SalesEvent(eventNumber, reg, amount);
        
        em.persist(evt);
        
        em.getTransaction().commit();
        em.close();	
	}
	
	public List<SalesEvent> retrieveSmallSales(double limit) {

		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();
		List<SalesEvent> result = null;
		// logic goes here
		String jpql = "select s from SalesEvent s where s.amount < :limit";
		TypedQuery<SalesEvent> q = em.createQuery(jpql, SalesEvent.class);
		q.setParameter("limit", limit);
		result = q.getResultList();
		
		em.getTransaction().commit();
		em.close();
		return result;
	}

	public void addServiceFee(double fee) {
		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();

		String jpql = "update SalesEvent s set s.amount = s.amount + :fee";
		int updated = em.createQuery(jpql)
				.setParameter("fee", fee)
				.executeUpdate();
		System.out.println("Updated rows: " + updated);
		em.getTransaction().commit();
		em.close();
	}

	public void deleteSalesEvents() {
		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();

		String jpql = "delete from SalesEvent s";
		int deleted = em.createQuery(jpql)
				.executeUpdate();
		System.out.println("Deleted rows: " + deleted);
		em.getTransaction().commit();
		em.close();
	}
	
}
