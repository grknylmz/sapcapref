using BoxService from '../../srv/travel-service';

//
// annotations that control the behavior of fields and actions
//

// Workarounds for overly strict OData libs and clients
annotate cds.UUID with @Core.Computed  @odata.Type : 'Edm.String';

// Add fields to control enablement of action buttons on UI
extend projection BoxService.Box with {
  // REVISIT: shall be improved by omitting "null as"
  virtual null as acceptEnabled         : Boolean @UI.Hidden,
  virtual null as rejectEnabled         : Boolean @UI.Hidden,
 // virtual null as deductDiscountEnabled : Boolean @UI.Hidden,
}

annotate BoxService.Box with {
  BeginDate   @Common.FieldControl  : BoxStatus.fieldControl;
  EndDate     @Common.FieldControl  : BoxStatus.fieldControl;  
  to_Customer @Common.FieldControl  : BoxStatus.fieldControl;

} actions {
  rejectBox @(
    Core.OperationAvailable : in.rejectEnabled,
    Common.SideEffects.TargetProperties : [
      'in/BoxStatus_code',
      'in/acceptEnabled',
      'in/rejectEnabled'
    ],
  );
  acceptBox @(
    Core.OperationAvailable : in.acceptEnabled,
    Common.SideEffects.TargetProperties : [
      'in/BoxStatus_code',
      'in/acceptEnabled',
      'in/rejectEnabled'
    ],
  );
 /* deductDiscount @(
    Core.OperationAvailable : in.deductDiscountEnabled,
    Common.SideEffects.TargetProperties : ['in/deductDiscountEnabled'],
  );*/
}

annotate BoxService.Geraete with @UI.CreateHidden : to_Box.BoxStatus.createDeleteHidden;

annotate BoxService.Geraete {
  ConnectionID  @Common.FieldControl  : to_Box.BoxStatus.fieldControl;
  GeraeteStatus @Common.FieldControl  : to_Box.BoxStatus.fieldControl;
  to_Carrier    @Common.FieldControl  : to_Box.BoxStatus.fieldControl;
  to_Customer   @Common.FieldControl  : to_Box.BoxStatus.fieldControl;
};




