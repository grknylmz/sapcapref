using app.data as data from '../db/data-model';
@requires: 'authenticated-user'
service DataService {
    entity Theses @(restrict: [
            { grant: ['READ','WRITE'], to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' }])
        as projection on data.Theses {
            *,
            students : redirected to Students2Theses
    };
    entity Courses @(restrict: [
            { grant: 'READ', to: 'Rank' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Admin''' } ]) 
        as projection on data.Courses;
    entity Faculties @(restrict: [
            { grant: 'READ', to: 'Rank' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Admin''' } ]) 
        as projection on data.Faculties;
    entity Students @(restrict: [
            { grant: 'READ', to: 'Rank' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Student''' } ]) 
        as projection on data.Students;
    entity Teachers @(restrict: [
            { grant: 'READ', to: 'Rank' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Teachers;
    entity Admins @(restrict: [
            { grant: 'READ', to: 'Rank' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Admin''' } ]) 
        as projection on data.Admins;
    entity ThesisText @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Student''' } ]) 
        as projection on data.ThesisText
    entity Courses2Theses @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Courses2Theses;
    entity Students2Theses @(restrict: [
            { grant: ['READ','WRITE'], to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' } ]) 
        as projection on data.Students2Theses {
            *,
            student : redirected to Students,
            thesis : redirected to Theses
    };
    entity Teachers2Theses @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Teachers2Theses;
    entity Tags @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Tags;
    entity Literatures @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Literatures;
    entity Prerequisites @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Prerequisites;
    entity Tags2Theses @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Tags2Theses;
    entity Literatures2Theses @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Literatures2Theses;
    entity Prerequisites2Theses @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' },
            { grant: 'WRITE', to: 'Rank', where: '$user.Rank = ''Teacher''' } ]) 
        as projection on data.Prerequisites2Theses;
    @readonly entity StudentCount @(restrict: [
            { grant: 'READ', to: 'Rank', where: '$user.Rank = ''Teacher'' OR $user.Rank = ''Student''' }])
        as SELECT from data.Theses LEFT JOIN data.Students2Theses on Theses.ID = Students2Theses.thesis.ID distinct {
            key Theses.ID, 
            Theses.max_students,
            Theses.title, 
            Theses.teachers,
            Theses.faculty,
            Theses.open,
            Theses.fromdate,
            Theses.todate,
            Theses.tags,
            Theses.courses,
            COUNT(Students2Theses.student.neptun) as count : Integer
        }
        WHERE archived = false
        GROUP BY Theses.ID, Theses.max_students, Theses.title, Theses.teachers.teacher,
            Theses.faculty,
            Theses.open,
            Theses.fromdate,
            Theses.todate,
            Theses.tags.tag,
            Theses.courses.course;

    type StatField {FIRST_NAME : String; LAST_NAME : String; NEPTUN : String; VALUE : Integer};
    function TeachersStatsWithDate(date : DateTime, operator : String(2)) returns many StatField;
    function TeachersStats() returns many StatField;
    function StudentsStatsWithDate(date : DateTime, operator : String(2)) returns many StatField;
    function StudentsStats() returns many StatField;
}