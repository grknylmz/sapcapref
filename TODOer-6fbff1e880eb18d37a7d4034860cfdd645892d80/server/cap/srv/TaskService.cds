using {db} from '../db';


service TaskService {

  entity Collections as projection on db.TaskCollections;

  @readonly
  entity Priorities  as projection on db.TaskPriorities excluding {
    descr
  }

  entity Tasks       as projection on db.Tasks {
    * , collection @mandatory, status @readonly
  } excluding {
    createdAt,
    createdBy,
    modifiedAt,
    modifiedBy
  } actions {
    action setToDone();
    action cancel();
    action reopen();
  }

  @readonly
  entity Statuses    as projection on db.TaskStatuses excluding {
    descr
  }

}
