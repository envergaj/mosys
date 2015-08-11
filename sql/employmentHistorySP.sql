delimiter //

create procedure addEmploymentHistory
(
	in vemployeeNumber varchar(255),
    in vposition varchar (255),
    in vstartDate date,
    in vworkLocation varchar (255),
    in vcoop varchar (255),
    in vclientCompany varchar (255),
    in vendDate date
)

begin
    insert into employmentHistory (employeeNumber, position, startDate,
		workLocation, coop, clientCompany, endDate)
	values (vemployeeNumber, vposition, vstartDate, vworkLocation, vcoop,
		vclientCompany, vendDate);
end //


create procedure updateEmploymentHistory
(
	in vid int,
    in vendDate date
)

begin
	update employmentHistory
    set endDate = vendDate
	where id = vid;
end //


create procedure deleteEmploymentHistory
(
	in vid int
)

begin
	update employmentHistory
    set deletedFlag = true
    where id = vid;
end //

delimiter ;