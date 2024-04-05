package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FileData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long id;

  @ManyToMany(mappedBy = "supportingFiles", fetch = FetchType.LAZY)
  Set<ExpenseEntry> expenses = new HashSet<>();
}
