package com.lipskii.ski_jumping_system.dao;

import com.lipskii.ski_jumping_system.entity.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification,Integer>, JpaSpecificationExecutor<Qualification> {
}
