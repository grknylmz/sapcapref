using {my.cave as cave} from '../db/schema';

service retailer @(
  path     : '/retailer',
  requires : 'authenticated-user'
) {

  @odata.draft.enabled
  entity Vin                                                      @(restrict : [{
    grant : '*',
    to    : 'admin'
  }, ])              as projection on cave.Vin actions {
    action postGoods(quantity :     Integer                       @title : '{i18n>quantity}');
    action order(quantity :         Integer                       @title : '{i18n>quantity}');
    action withdrawFromSale();
    action addGrapeVariety(cepage_ID : UUID ,pourcent : Integer @(assert.range : [
      1,
      100
    ])) returns                     Assemblage
  };

  event productAvailable {
    name     : String;
    vintage  : String(4);
    color    : String;
    quantity : Integer;
  }

  event productOrdered {
    name     : String;
    vintage  : String(4);
    color    : String;
    quantity : Integer;
  }

  extend projection Vin with {
    virtual null as postGoodsEnabled        : Boolean @Core.Computed  @UI.Hidden,
    virtual null as orderEnabled            : Boolean @Core.Computed  @UI.Hidden,
    virtual null as withdrawFromSaleEnabled : Boolean @Core.Computed  @UI.Hidden
  };

  entity Superficie  as projection on cave.Superficie;

  @odata.draft.enabled
  entity Cepage      as projection on cave.Cepage;

  entity Region      as projection on cave.Region;

  entity Assemblage  as projection on cave.Assemblage {
    * , vin : redirected to Vin, cepage : redirected to Cepage,

  };

  entity ColorCepage @(
    cds.redirection.target : false,
    odata.draft.enabled    : null,
    readonly
  )                  as projection on cave.ColorCepage;

  entity TypeBoisson @(
    cds.redirection.target : false,
    odata.draft.enabled    : null,
    readonly
  )                  as projection on cave.TypeBoisson;

  @readonly
  define view VinPerCepage as
    select from Assemblage {
      key cepage.name as name         : String,
      key count(
            vin.ID
          )           as cepage_count : Integer,

    }
    group by
      cepage.name
    order by
      name;

  @Aggregation : {ApplySupported : {
    $Type                  : 'Aggregation.ApplySupportedType',
    AggregatableProperties : [
      {
        $Type    : 'Aggregation.AggregatablePropertyType',
        Property : prix,
      },
      {
        $Type    : 'Aggregation.AggregatablePropertyType',
        Property : counter
      },
    ],
    GroupableProperties    : [
      millesime,
      color,
      categorie,
      status
    ],
    PropertyRestrictions   : false,
  }, }
  define view VinAnalytics as
    select from Vin {
      key ID,
          reference,
          name,
          devise,
          @Analytics           : {
            Dimension : true
          }
          @Aggregation.default : #COUNT_DISTINCT
          color.name  as color     : String,
          @Analytics           : {
            Dimension : true
          }
          @Aggregation.default : #COUNT_DISTINCT
          type        as categorie : String,
          @Analytics           : {
            Dimension : true
          }
          @Aggregation.default : #COUNT_DISTINCT
          annee       as millesime,
          @Analytics           : {
            Measure : true
          }
          @Aggregation.default : #SUM
          prix,
          @Analytics           : {
            Measure : true
          }
          @Aggregation.default : #SUM
          1           as counter   : Integer,
          @Analytics           : {
            Dimension : true
          }
          status.name as status    : String,
    }
    order by
      millesime,
      color;

  annotate VinAnalytics with @readonly;


  @Aggregation : {ApplySupported : {
    $Type                  : 'Aggregation.ApplySupportedType',
    PropertyRestrictions   : true,
    GroupableProperties    : [
      name,
      annee,
      superficie
    ],
    AggregatableProperties : [{
      $Type    : 'Aggregation.AggregatablePropertyType',
      Property : superficie,
    }, ],
  }, }
  define view SuperficieEvolution as
    select from Superficie {
      key to_cepage.name as name,
          @Analytics : {
            Dimension : true
          }
      key annee          as annee : String,
          @Analytics : {
            Measure : true
          }
          superficie
    }
    order by
      annee;

  entity LogOfDemand as projection on cave.LogOfDemand {
    * ,
    @Aggregation.default : #SUM
    1 as counter : Integer
  }
};
