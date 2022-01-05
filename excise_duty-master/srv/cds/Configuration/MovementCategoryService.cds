using ExciseDutyModel from '../../../db/cds/ExciseDutyModel';
using from './MovementCategoryAnnotations';

service MovementCategoryService
{

  entity MovementCategory as projection on ExciseDutyModel.MovementCategory;

  entity MovementTypeMapping as projection on ExciseDutyModel.MovementTypeMapping;

}