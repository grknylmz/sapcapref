namespace workflowtest2;

using sap.workflow from './WorkflowObject';

entity books
{
    key ID : UUID
        @Core.Computed;
    name : String;
    title : String;
}
