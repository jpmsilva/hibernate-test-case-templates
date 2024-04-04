package org.hibernate.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Travel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long id;

  @OneToMany(mappedBy = "travel", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  public Set<ExpenseEntry> expenses = new HashSet<>();
}
