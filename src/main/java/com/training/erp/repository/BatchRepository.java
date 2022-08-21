package com.training.erp.repository;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<Batch,Long> {
    @Query("SELECT batch FROM Batch batch LEFT JOIN  batch.trainers trainer WHERE trainer.id = ?1")
    List<Batch> findAllByTrainer(long trainerId);
    boolean existsByBatchName(String batch_name);
}
