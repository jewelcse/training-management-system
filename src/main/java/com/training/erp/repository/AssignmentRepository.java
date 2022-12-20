package com.training.erp.repository;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long>, JpaSpecificationExecutor<Assignment> {
    List<Assignment> findAllAssignmentByBatch(Batch batch);
    List<Assignment> findAllByCourse(Course course);

    //    @Query("SELECT trainer FROM Trainer trainer LEFT JOIN trainer.batches batch WHERE batch.id = ?1")
}
