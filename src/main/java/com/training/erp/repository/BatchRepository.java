package com.training.erp.repository;

import com.training.erp.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch,Long> {
    boolean existsByBatchName(String batch_name);
}
