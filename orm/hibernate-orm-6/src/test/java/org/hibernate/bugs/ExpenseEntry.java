package org.hibernate.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ExpenseEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long id;

  @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
  Travel travel = null;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  Set<FileData> supportingFiles = new HashSet<>();
}
