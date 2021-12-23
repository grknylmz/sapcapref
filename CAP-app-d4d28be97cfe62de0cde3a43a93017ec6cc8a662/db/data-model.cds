namespace app.data;
using { sap, managed, cuid } from '@sap/cds/common';

aspect CommonProps {
    key neptun : String(6);
    first_name : String(255);
    last_name : String(255);
    email : String(255);
}

entity Theses : cuid, managed {
    courses : Association to many Courses2Theses on courses.thesis = $self;
    students : Association to many Students2Theses on students.thesis = $self;
    teachers : Association to many Teachers2Theses on teachers.thesis = $self;
    tags : Association to many Tags2Theses on tags.thesis = $self;
    literatures : Association to many Literatures2Theses on literatures.thesis = $self;
    prerequisites : Association to many Prerequisites2Theses on prerequisites.thesis = $self;
    title : String(526);
    max_students : Integer default 1;
    description : String;
    date : DateTime;
    fromdate : DateTime;
    todate : DateTime;
    open : Boolean default false;
    only_preferred : Boolean default false;
    archived : Boolean default false;
    faculty : Association to Faculties;
}

entity Courses : cuid {
    name : String(255);
    deadline : DateTime default $now;
    faculty : Association to Faculties;
}

entity Faculties : cuid {
    name : String(255);
}

entity Tags : cuid {
    name : String(255);
}

entity Literatures : cuid {
    literature : String(255);
}

entity Prerequisites : cuid {
    knowledge : String(255);
}

entity Students : CommonProps { 
    faculty : Association to Faculties;
    course : Association to Courses;
}

entity Teachers : CommonProps {
    faculty : Association to Faculties;
}

entity Admins : CommonProps { }

entity ThesisText : managed {
    key thesis : Association to Theses;
    text : LargeString;
}

entity Courses2Theses {
    key course : Association to Courses;
    key thesis : Association to Theses;
}
entity Students2Theses {
    key student : Association to Students;
    key thesis : Association to Theses;
    mod_count : Integer default 0;
    createdAt : Timestamp @cds.on.insert : $now;
    createdBy : String(255) @cds.on.insert : $user;
}
entity Teachers2Theses {
    key teacher : Association to Teachers;
    key thesis : Association to Theses;
}
entity Tags2Theses {
    key tag : Association to Tags;
    key thesis : Association to Theses;
}
entity Literatures2Theses {
    key literature : Association to Literatures;
    key thesis : Association to Theses;    
}
entity Prerequisites2Theses {
    key knowledge : Association to Prerequisites;
    key thesis : Association to Theses;
}