package org.hibernate.bugs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public long id;
}
