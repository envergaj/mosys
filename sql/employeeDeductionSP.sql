delimiter //

create procedure addEmployeeDeduction
(
	in vemployeeNumber varchar(255),
    in vemployeeName varchar(255),
    in vprincipal double,
    in vamortization double,
    in vdeductionShortCode varchar (31)
)

begin
    insert into employeeDeduction (employeeNumber, employeeName,
		principal, amortization, deductionShortCode)
    values (vemployeeNumber, vemployeeName, vprincipal, vamortization,
		vdeductionShortCode);
end //


create procedure updateEmployeeDeduction
(
	in vid int,
	in vemployeeNumber varchar(255),
    in vemployeeName varchar(255),
    in vprincipal double,
    in vamortization double,
    in vdeductionShortCode varchar (31)
)

begin
	update employeeDeduction
    set employeeNumber = vemployeeNumber, employeeName = vemployeeName,
		principal = vprincipal, amortization = vamortization, 
        deductionShortCode = vdeductionshortCode
	where id = vid;
end //


create procedure deleteEmployeeDeduction
(
	in vid int
)

begin
	update employeeDeduction
    set deletedFlag = true
    where id = vid;
end //

delimiter ;