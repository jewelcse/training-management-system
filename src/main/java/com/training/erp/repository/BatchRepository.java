package com.training.erp.repository;

import com.training.erp.entity.batches.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch,Long> {
    boolean existsByBatchName(String batch_name);
}
