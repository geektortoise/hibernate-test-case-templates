package org.hibernate.bugs;

import domain.People;
import domain.RollerCoaster;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hibernate.graph.GraphParser.parse;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;
	private TypedQuery<People> query;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		//set up some data
		People p = new People();
		p.setWeight(90);
		p.setZipCode("4000");

		RollerCoaster rollerCoasterForAll = new RollerCoaster();
		rollerCoasterForAll.setMaxWeight(200);
		rollerCoasterForAll.setZipCode("4000");

		RollerCoaster rollerCoasterForSome = new RollerCoaster();
		rollerCoasterForSome.setMaxWeight(100);
		rollerCoasterForSome.setZipCode("4000");

		RollerCoaster rollerCoasterForKids = new RollerCoaster();
		rollerCoasterForKids.setMaxWeight(50);
		rollerCoasterForKids.setZipCode("4000");

		entityManager.persist(p);
		entityManager.persist(rollerCoasterForAll);
		entityManager.persist(rollerCoasterForSome);
		entityManager.persist(rollerCoasterForKids);

		entityManager.getTransaction().commit();
	}

	@After
	public void destroy() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.createQuery("delete from People ").executeUpdate();
		entityManager.createQuery("delete from RollerCoaster ").executeUpdate();
		entityManager.getTransaction().commit();
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123TestFetchGraph() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<People> people;

		//requests and tests
		query = entityManager.createQuery("select distinct p from People p", People.class)
				.setHint("jakarta.persistence.fetchgraph",
						parse(People.class,"authorizedRollerCoasters",entityManager.unwrap(SessionImplementor.class))
				)
		;

		people = query.getResultList();
		assert people.size() == 1;
		assert people.get(0).getAuthorizedRollerCoasters().size() == 3;

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void hhh123TestFetchGraphWithAdditionalStaticCondition() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<People> people;

		//requests and tests
		entityManager
				.unwrap(Session.class)
				.enableFilter("onlyUnsafe")
		;

		query = entityManager.createQuery("select distinct p from People p", People.class)
				.setHint("jakarta.persistence.fetchgraph",
						parse(People.class,"authorizedRollerCoasters",entityManager.unwrap(SessionImplementor.class))
				)
		;

		people = query.getResultList();
		assert people.size() == 1;
		assert people.get(0).getAuthorizedRollerCoasters().size() == 1;

		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Test
	public void hhh123TestFetchGraphWithAdditionalConditionOnTwoEntities() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<People> people;

		//requests and tests
		entityManager
				.unwrap(Session.class)
				.enableFilter("onlyAuthorized")
		;

		query = entityManager.createQuery("select distinct p from People p", People.class)
				.setHint("jakarta.persistence.fetchgraph",
						parse(People.class,"authorizedRollerCoasters",entityManager.unwrap(SessionImplementor.class))
				)
		;

		people = query.getResultList();
		assert people.size() == 1;
		assert people.get(0).getAuthorizedRollerCoasters().size() == 2;

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
