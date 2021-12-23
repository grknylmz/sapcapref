namespace db;

using {sap.common.CodeList} from '@sap/cds/common';

entity TaskPriorities : CodeList {
  key code : Integer;
}

type TaskPriority : Association to one TaskPriorities;
