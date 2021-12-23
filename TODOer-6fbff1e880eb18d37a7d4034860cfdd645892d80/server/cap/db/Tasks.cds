namespace db;

using {
  cuid,
  managed
} from '@sap/cds/common';
using {db.TaskCollections} from './TaskCollections';
using {db.TaskPriority} from './TaskPriorities';
using {db.TaskStatus} from './TaskStatuses';

entity Tasks : cuid, managed {
  title             : String(40);
  collection        : Association to one TaskCollections not null;
  status            : TaskStatus not null;
  priority          : TaskPriority;
  dueDate           : Date;
  dueTime           : Time;
  isPlannedForMyDay : Boolean not null;
}
