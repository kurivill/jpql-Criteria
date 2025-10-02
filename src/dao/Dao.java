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

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SalesEvent> cq = cb.createQuery(SalesEvent.class);

		Root<SalesEvent> root = cq.from(SalesEvent.class);

		cq.select(root)
		  .where(cb.lessThan(root.get("amount"), limit));

		TypedQuery<SalesEvent> query = em.createQuery(cq);
		List<SalesEvent> result = query.getResultList();
		
		em.getTransaction().commit();
		em.close();
		return result;
	}

	public void addServiceFee(double fee) {
		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<SalesEvent> cu = cb.createCriteriaUpdate(SalesEvent.class);
		Root<SalesEvent> root = cu.from(SalesEvent.class);

		//cu.set("amount", cb.sum(root.get("amount"), fee));
		cu.set("amount", cb.sum(root.get("amount"), cb.literal(fee)));

		int updated = em.createQuery(cu).executeUpdate();

		em.getTransaction().commit();
		em.close();

		System.out.println("Updated rows: " + updated);
	}

	public void deleteSalesEvents() {
		EntityManager em = creds_provider.getEmf().createEntityManager();
		em.getTransaction().begin();

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<SalesEvent> delete = cb.createCriteriaDelete(SalesEvent.class);
		Root<SalesEvent> root = delete.from(SalesEvent.class);

		int deleted = em.createQuery(delete).executeUpdate();

		em.getTransaction().commit();
		em.close();

		System.out.println("Deleted rows: " + deleted);
	}
	
}
