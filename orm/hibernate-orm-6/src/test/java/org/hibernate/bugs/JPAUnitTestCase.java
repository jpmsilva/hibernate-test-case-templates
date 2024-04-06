package org.hibernate.bugs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Objects;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	private TestCasePreInsertListener preEventListener;

	private TestCasePostInsertListener postEventListener;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
		EventListenerRegistry registry = Objects.requireNonNull(sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class));
		preEventListener = new TestCasePreInsertListener();
		postEventListener = new TestCasePostInsertListener();
		registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(preEventListener);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(postEventListener);
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void saveOrUpdatePreTest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		Travel travel = new Travel();
		long id = entityManager.merge(travel).id;
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		travel = entityManager.find(Travel.class, id);
		ExpenseEntry expense = new ExpenseEntry();
		expense.travel = travel;
		FileData fileData = new FileData();
		fileData.expenses.add(expense);
		expense.supportingFiles.add(fileData);
		travel.expenses.add(expense);
		entityManager.unwrap(Session.class).saveOrUpdate(travel);
		entityManager.getTransaction().commit();
		entityManager.close();

		assertEquals(1, preEventListener.count);
	}

	@Test
	public void mergePreTest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		Travel travel = new Travel();
		long id = entityManager.merge(travel).id;
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		travel = entityManager.find(Travel.class, id);
		ExpenseEntry expense = new ExpenseEntry();
		expense.travel = travel;
		FileData fileData = new FileData();
		fileData.expenses.add(expense);
		expense.supportingFiles.add(fileData);
		travel.expenses.add(expense);
		entityManager.merge(travel);
		entityManager.getTransaction().commit();
		entityManager.close();

		assertEquals(1, preEventListener.count);
	}

	@Test
	public void saveOrUpdatePostTest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		Travel travel = new Travel();
		long id = entityManager.merge(travel).id;
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		travel = entityManager.find(Travel.class, id);
		ExpenseEntry expense = new ExpenseEntry();
		expense.travel = travel;
		FileData fileData = new FileData();
		fileData.expenses.add(expense);
		expense.supportingFiles.add(fileData);
		travel.expenses.add(expense);
		entityManager.unwrap(Session.class).saveOrUpdate(travel);
		entityManager.getTransaction().commit();
		entityManager.close();

		assertEquals(1, postEventListener.count);
	}

	@Test
	public void mergePostTest() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		entityManager.getTransaction().begin();
		Travel travel = new Travel();
		long id = entityManager.merge(travel).id;
		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();
		travel = entityManager.find(Travel.class, id);
		ExpenseEntry expense = new ExpenseEntry();
		expense.travel = travel;
		FileData fileData = new FileData();
		fileData.expenses.add(expense);
		expense.supportingFiles.add(fileData);
		travel.expenses.add(expense);
		entityManager.merge(travel);
		entityManager.getTransaction().commit();
		entityManager.close();

		assertEquals(1, postEventListener.count);
	}
}
