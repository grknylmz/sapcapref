using { slah.db as my } from '../db/schema';


service BoxService @(path:'/processor', requires: 'admin') {

  entity Box as projection on my.Box actions {
    action createBoxByTemplate() returns Box;
    action rejectBox();     
    action acceptBox();
    action availableBox();
   // action deductDiscount( percent: Percentage not null ) returns Travel;
  };

  // Ensure all masterdata entities are available to clients
 // annotate my.MasterData with @cds.autoexpose @readonly;

  entity Geraete as projection on my.Geraete;
  entity Geraetetyp as projection on my.Geraetetyp; 
  entity GVerbindung as projection on my.GVerbindung;
  entity Patient as projection on my.Patient;   
  }



