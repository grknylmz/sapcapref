using {my.cave as cave} from '../db/schema';

// Input validation
annotate cave.Vin with {
  ID        @Core.Computed  @UI.Hidden;
  reference @mandatory;
  name      @mandatory;
  type      @mandatory;
  annee     @mandatory;
  color     @mandatory;
};

// Input Assemblage
annotate cave.Assemblage with {
  ID          @Core.Computed  @UI.Hidden;
  cepage_name @mandatory;
};

// Input Cepage
annotate cave.Cepage with {
  name @mandatory;
};

// Input Superficie
annotate cave.Superficie with {
  ID          @Core.Computed  @UI.Hidden;
  to_cepage   @UI.Hidden;
  cepage_name @readonly  @UI.Hidden;
  annee       @mandatory;
  superficie  @mandatory;
};

// Input Cave
annotate cave.Cave with {
  ID         @Core.Computed;
  modifiedAt @UI.Hidden;
  modifiedBy @UI.Hidden;
  createdAt  @UI.Hidden;
  createdBy  @UI.Hidden;
};

// Input RetentionStatus
annotate cave.AvailabilityStatus with {
  criticality @UI.Hidden;
};

// Input RetentionStatus
annotate cave.RetentionStatus with {
  criticality @UI.Hidden;
};
