using { workflowtest2 as my } from '../db/schema';

@path : 'service/workflowtest2'
@requires : 'workflowtest2Manager'
service workflowtest2Service
{
    entity books as
        projection on my.books;
}
