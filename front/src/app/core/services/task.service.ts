import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { ConvertTaskIntoSubTaskDto } from 'src/app/dtos/convert-task-into-sub-task.dto';
import { CreateTaskDto } from 'src/app/dtos/create-task.dto';
import { Task } from 'src/app/models/task.model';
import { environment } from 'src/environments/environment';

const url = `${environment.API_MANAGEMENT_TASK}/task`;

@Injectable({
  providedIn: 'root',
})
export class TaskService {

  constructor(protected http: HttpClient) { }

  findAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(url).pipe(take(1));
  }

  createTask(createTaskDto: CreateTaskDto): Observable<Task> {
    return this.http.post<Task>(url, createTaskDto).pipe(take(1));
  }

  updateTask(taskId: string, createTaskDto: CreateTaskDto): Observable<any> {
    return this.http.put(`${url}/${taskId}`, createTaskDto).pipe(take(1));
  }

  deleteTask(taskId: string): Observable<any> {
    return this.http.delete(`${url}/${taskId}`).pipe(take(1));
  }

  changeTaskIntoSubTask(taskId: string, convertTaskIntoSubTaskDto: ConvertTaskIntoSubTaskDto): Observable<any> {
    return this.http.put(`${url}/${taskId}/change-task-into-sub-task`, convertTaskIntoSubTaskDto).pipe(take(1));
  }

}
