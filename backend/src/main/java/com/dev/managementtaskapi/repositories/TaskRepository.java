package com.dev.managementtaskapi.repositories;

import com.dev.managementtaskapi.models.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    //@Query(value = "SELECT DISTINCT t FROM TaskModel t JOIN FETCH t.subTasks",nativeQuery = true)
    @Query(value = "SELECT DISTINCT t.* FROM tb_task t LEFT JOIN tb_subtask st on(t.id = st.task_id) order by t.creation_date asc", nativeQuery = true)
    List<TaskModel> findAllTasks();

}
