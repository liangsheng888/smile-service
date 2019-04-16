package org.flightythought.smile.admin.database.repository;//package org.flightythought.smile.appserver.database.repository;

import org.flightythought.smile.admin.database.entity.RecoverCaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoverCaseRepository extends JpaRepository<RecoverCaseEntity, Long> {
}
