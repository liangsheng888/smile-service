package org.flightythought.smile.appserver.database.repository;

import org.flightythought.smile.appserver.database.entity.DiseaseReasonTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseReasonTypeRepository extends JpaRepository<DiseaseReasonTypeEntity, Long> {
}
