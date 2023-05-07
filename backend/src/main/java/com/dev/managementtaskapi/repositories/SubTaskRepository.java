package com.dev.managementtaskapi.repositories;

import com.dev.managementtaskapi.models.SubTaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTaskModel, UUID> {

    @Query(value = "SELECT st.* FROM tb_task t JOIN tb_subtask st on(t.id = st.task_id) where t.id = :taskId and st.id = :subTaskId", nativeQuery = true)
    Optional<SubTaskModel> findSubTaskModelByTaskAndSubTaskId(UUID taskId, UUID subTaskId);

    @Modifying
    @Query(value = "DELETE FROM tb_subtask where id = :subTaskId and task_id = :taskId", nativeQuery = true)
    void deleteSubTaskByTaskIdAndSubTaskId(UUID taskId, UUID subTaskId);

}
