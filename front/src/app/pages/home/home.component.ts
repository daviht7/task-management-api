import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { SubTaskService } from 'src/app/core/services/sub-task.service';
import { TaskService } from 'src/app/core/services/task.service';
import { SubTask } from 'src/app/models/sub-task.model';
import { Task } from 'src/app/models/task.model';
import { showMessage } from 'src/app/utils/showMessage';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  isModalCreateNewSubTaskVisible = false;
  isModalEditTask = false;

  tasks: Task[] = [];

  formCreateTask: FormGroup;
  formEditTask: FormGroup;
  formCreateSubTask: FormGroup;

  selectedTaskId: string = "";
  selectedSubTaskId: string = "";

  currentAction = "";

  titleLabelSaveSubTask = "Cadastrar Sub Tarefa";

  paramId: string = "";

  constructor(private fb: FormBuilder,
    private messageService: MessageService,
    private taskService: TaskService,
    private subTaskService: SubTaskService,
    private route: ActivatedRoute,
    private router: Router) {

    this.paramId = this.route.snapshot.paramMap.get('taskId') || "";

    this.formCreateTask = this.fb.group({
      title: ['', Validators.required],
    });

    this.formEditTask = this.fb.group({
      title: ['', Validators.required],
    });

    this.formCreateSubTask = this.fb.group({
      title: ['', Validators.required],
    });

  }

  ngOnInit() {
    this.findAllTasks();
  }

  handleEditTask(task: Task) {
    this.paramId = task.id;
    this.formEditTask.setValue({
      title: task.title
    })
    this.isModalEditTask = true;
    this.router.navigate([`/edit/${task.id}`]);
  }

  handleCreateNewSubTask(taskId: string) {
    this.selectedTaskId = taskId;
    this.isModalCreateNewSubTaskVisible = true;
  }

  handleEditSubTask(taskId: string, subTask: SubTask) {
    this.titleLabelSaveSubTask = "Editar Sub Tarefa";
    this.selectedSubTaskId = subTask.id;
    this.selectedTaskId = taskId;
    this.isModalCreateNewSubTaskVisible = true;

    this.formCreateSubTask.setValue({
      title: subTask.title
    })

  }

  deleteTaskIdAndSubTaskIdAndFindAll() {
    this.selectedSubTaskId = "";
    this.selectedTaskId = "";
    this.findAllTasks();
  }

  dropTaskDeleteTaskSubTask() {

    if (this.selectedSubTaskId && this.selectedTaskId) {

      this.subTaskService.deleteSubTask(this.selectedTaskId, this.selectedSubTaskId)
        .subscribe({
          next: () => {
            showMessage(this.messageService, {
              severity: 'success',
              detail: 'Sub Tarefa Deletada com sucesso!'
            })
            this.deleteTaskIdAndSubTaskIdAndFindAll();
          }
        })

    } else if (this.selectedTaskId) {

      this.taskService.deleteTask(this.selectedTaskId)
        .subscribe({
          next: () => {
            showMessage(this.messageService, {
              severity: 'success',
              detail: 'Tarefa Deletada com sucesso!'
            })
            this.deleteTaskIdAndSubTaskIdAndFindAll();
          }
        })

    }

  }

  dropChangeSubTaskIntoTask() {

    if (!this.selectedSubTaskId) return;

    this.subTaskService.changeSubTaskIntoTask(this.selectedSubTaskId)
      .subscribe({
        next: () => {
          showMessage(this.messageService, {
            severity: 'success',
            detail: 'Sub Tarefa convertida em Tarefa com sucesso!'
          });
          this.selectedSubTaskId = "";
          this.selectedTaskId = "";
          this.findAllTasks();
        }
      })

  }

  deleteTaskIdAndSubTaskIdAndCurrentActionAndFindAll() {
    this.selectedSubTaskId = "";
    this.selectedTaskId = "";
    this.findAllTasks();
    this.currentAction = "";
  }

  dropTask(taskId: string) {

    if (this.selectedTaskId === taskId) return;

    if (this.currentAction === 'dragStartTask') {

      this.taskService.changeTaskIntoSubTask(taskId, { taskId: this.selectedTaskId })
        .subscribe({
          next: () => {
            showMessage(this.messageService, {
              severity: 'success',
              detail: 'Tarefa convertida em Sub-Tarefa com sucesso!'
            });
            this.deleteTaskIdAndSubTaskIdAndCurrentActionAndFindAll();
          }
        })

    } else if (this.currentAction === 'dragStartSubTask') {

      this.subTaskService.changeTaskFromSubTask(this.selectedSubTaskId, taskId)
        .subscribe({
          next: () => {
            showMessage(this.messageService, {
              severity: 'success',
              detail: 'Mudança de Tarefa da Sub Tarefa com sucesso!'
            });
            this.deleteTaskIdAndSubTaskIdAndCurrentActionAndFindAll();
          }
        })

    }

  }

  dragStartSubTask(subTaskId: string, taskId: string) {
    this.selectedSubTaskId = subTaskId;
    this.selectedTaskId = taskId;
    this.currentAction = "dragStartSubTask"
  }

  dragStartTask(taskId: string) {
    this.selectedSubTaskId = "";
    this.selectedTaskId = taskId;
    this.currentAction = "dragStartTask"
  }

  findAllTasks() {
    this.taskService.findAllTasks().subscribe({
      next: (res) => {
        this.tasks = res;

        if (this.paramId) {
          let task = this.tasks.find(x => x.id === this.paramId);
          if (task) {
            this.formEditTask.setValue({
              title: task.title
            })
            this.isModalEditTask = true;
          } else {
            this.router.navigate(['']);
          }
        }

      }
    })
  }

  handleAddNewSubTask() {

    const { title } = this.formCreateSubTask.value;

    if (this.selectedSubTaskId) {

      this.subTaskService.updateSubTask(this.selectedTaskId, this.selectedSubTaskId, { title })
        .subscribe({
          next: () => {
            this.isModalCreateNewSubTaskVisible = false;
            this.showMessageSuccess('Sub Tarefa Atualizada com sucesso!');
            this.findAllTasks();
            this.selectedSubTaskId = "";
            this.formCreateSubTask.reset();
            this.titleLabelSaveSubTask = "Cadastrar Sub Tarefa";
          }
        })

    } else {

      this.subTaskService.createSubTask(this.selectedTaskId, { title })
        .subscribe({
          next: () => {
            this.isModalCreateNewSubTaskVisible = false;
            this.showMessageSuccess('Sub Tarefa Cadastrada com sucesso!');
            this.findAllTasks();
            this.formCreateSubTask.reset();

          }
        })

    }

  }

  handleSaveTask() {

    if (this.formCreateTask.invalid) {
      showMessage(this.messageService, {
        severity: 'error',
        detail: 'O Título da Tarefa é obrigatório!'
      })
      return;
    }

    const { title } = this.formCreateTask.value;

    this.taskService.createTask({ title }).subscribe({
      next: (res) => {
        this.showMessageSuccess('Tarefa Cadastrada com sucesso!');
        this.router.navigate([`/edit/${res.id}`]);
        this.findAllTasks();
        this.isModalEditTask = true;
        this.paramId = res.id;
      }
    })

  }

  handleEditTaskModal() {

    if (this.formEditTask.invalid) {
      showMessage(this.messageService, {
        severity: 'error',
        detail: 'O Título da Tarefa é obrigatório!'
      })
      return;
    }

    const { title } = this.formEditTask.value;

    this.taskService.updateTask(this.paramId, { title }).subscribe({
      next: () => {
        this.showMessageSuccess('Tarefa Atualizada com sucesso!');
        this.formCreateTask.reset();
        this.findAllTasks();
        this.paramId = "";
        this.router.navigate(['']);
      }
    })

  }

  handleModalHide() {
    this.router.navigate(['']);
    this.paramId = "";
  }

  private showMessageSuccess(msg: string) {
    showMessage(this.messageService, {
      severity: 'success',
      detail: msg
    })
  }

}