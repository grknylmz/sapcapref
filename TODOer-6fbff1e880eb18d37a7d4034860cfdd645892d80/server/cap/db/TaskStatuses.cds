namespace db;

using {sap.common.CodeList} from '@sap/cds/common';

entity TaskStatuses : CodeList {
  key code : String(1);
}

type TaskStatus : Association to one TaskStatuses;
