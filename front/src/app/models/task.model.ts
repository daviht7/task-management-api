import { SubTask } from "./sub-task.model";

export class Task {
  id: string;
  title: string;
  subTasks: SubTask[];

  constructor(id: string, title: string, subTasks: SubTask[]) {
    this.id = id;
    this.title = title;
    this.subTasks = subTasks;
  }

}