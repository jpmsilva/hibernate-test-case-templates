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

	private TestCasePreInsertListener eventListener;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
		SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
		EventListenerRegistry registry = Objects.requireNonNull(sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class));
		eventListener = new TestCasePreInsertListener();
		registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(eventListener);
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	@Test
	public void saveOrUpdateTest() throws Exception {
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

		assertEquals(1, eventListener.count);
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void mergeTest() throws Exception {
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

		assertEquals(1, eventListener.count);
	}
}
