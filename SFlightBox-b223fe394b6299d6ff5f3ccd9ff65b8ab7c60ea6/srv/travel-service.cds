using { sap.fe.cap.travel as my } from '../db/schema';


service BoxService @(path:'/processor', requires: 'admin') {

  entity Box as projection on my.Box actions {
    action createBoxByTemplate() returns Box;
    action rejectBox();
    action acceptBox();
    //action deductDiscount( percent: Percentage not null ) returns Box;
  };

  // Ensure all masterdata entities are available to clients
  annotate my.MasterData with @cds.autoexpose @readonly;
}


