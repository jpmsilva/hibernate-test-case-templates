package org.hibernate.bugs;

import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;

public class TestCasePreInsertListener implements PreInsertEventListener {

  public int count = 0;

  @Override
  public boolean onPreInsert(PreInsertEvent event) {
    Object entity = event.getEntity();
    if (entity instanceof ExpenseEntry) {
      ExpenseEntry expenseEntry = (ExpenseEntry) entity;
      count = expenseEntry.supportingFiles.size();
    }

    return false;
  }
}
