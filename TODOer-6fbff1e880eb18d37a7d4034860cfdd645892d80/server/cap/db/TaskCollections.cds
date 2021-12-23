namespace db;

using {cuid} from '@sap/cds/common';
using {db.Tasks} from './Tasks';

entity TaskCollections : cuid {
  title         : String(40);
  color         : String(6);
  isDefault     : Boolean not null;
  assignedTasks : Association to many Tasks
                    on assignedTasks.collection = $self
}
