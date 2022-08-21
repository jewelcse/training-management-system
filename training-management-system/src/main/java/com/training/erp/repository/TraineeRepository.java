package com.training.erp.repository;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Trainee;
import com.training.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    @Query(value = "SELECT * FROM trainees WHERE user_id = ?1",nativeQuery = true)
    Trainee findByUserId(Long id);
    Trainee findByUser(User user);
    @Query(value = "SELECT * FROM trainees WHERE batch_id IS NULL",nativeQuery = true)
    List<Trainee> getNotAssignedTrainees();
    List<Trainee> findAllByBatch(Batch batch);
}
