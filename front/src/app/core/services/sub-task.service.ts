import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { CreateSubTaskDto } from 'src/app/dtos/create-sub-task.dto';
import { environment } from 'src/environments/environment';

const url = `${environment.API_MANAGEMENT_TASK}`;

@Injectable({
  providedIn: 'root',
})
export class SubTaskService {

  constructor(protected http: HttpClient) { }

  createSubTask(taskId: string, createSubTaskDto: CreateSubTaskDto): Observable<any> {
    return this.http.post(`${url}/task/${taskId}/sub-task`, createSubTaskDto).pipe(take(1));
  }

  updateSubTask(taskId: string, subTaskId: string, createSubTaskDto: CreateSubTaskDto): Observable<any> {
    return this.http.put(`${url}/task/${taskId}/sub-task/${subTaskId}`, createSubTaskDto).pipe(take(1));
  }

  deleteSubTask(taskId: string, subTaskId: string): Observable<any> {
    return this.http.delete(`${url}/task/${taskId}/sub-task/${subTaskId}`).pipe(take(1));
  }

  changeSubTaskIntoTask(subTaskId: string): Observable<any> {
    return this.http.put(`${url}/sub-task/${subTaskId}/change-sub-task-into-task`, {}).pipe(take(1));
  }

  changeTaskFromSubTask(subTaskId: string, taskId: string): Observable<any> {
    return this.http.put(`${url}/sub-task/${subTaskId}/task/${taskId}/change-task-from-sub-task`, {}).pipe(take(1));
  }

}
