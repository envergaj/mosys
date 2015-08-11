delimiter //

create procedure addEmployee
(
	in vemployeeNumber varchar(255),
	in vlastName varchar(255),
	in vfirstName varchar(255),
	in vmiddleName varchar(255),
	in vgender varchar(7),
	in vbirthDate date,
	in vcivilStatus varchar(7),
    in vposition varchar (255),
    in vcoop varchar (255),
    in vstartDate date,
	in vaddress varchar(1027),
	in vsssNumber long,
	in vpagibigNumber long,
	in vphilhealthNumber long,
	in vtinNumber long,
	in vcashcardNumber long,
    in vcontactNumber long,
	in vpicture varchar(255),
    in vclient varchar (255),
    in vworkLocation varchar (255)
)

begin
	insert into employee (employeeNumber, lastName, firstName, middleName, gender,
		birthDate, civilStatus, address, contactNumber, sssNumber, pagibigNumber,
		philhealthNumber, tinNumber, cashcardNumber, picture)
	values (vemployeeNumber, vlastName, vfirstName, vmiddleName, vgender,
		vbirthDate, vcivilStatus,vaddress, vcontactNumber, vsssNumber,
        vpagibigNumber, vphilhealthNumber, vtinNumber, vcashcardNumber,
        vpicture);
	
    insert into employmentHistory (employeeNumber, position, startDate,
		workLocation, coop, client)
	values (vemployeeNumber, vposition, vstartDate, vworkLocation, vcoop,
		vclient);
end //


create procedure updateEmployee
(
	in vid int,
    in vemployeeNumber varchar(255),
	in vlastName varchar(255),
	in vfirstName varchar(255),
	in vmiddleName varchar(255),
	in vgender varchar(7),
	in vbirthDate date,
	in vcivilStatus varchar(7),
	in vaddress varchar(1027),
	in vcontactNumber long,
	in vsssNumber long,
	in vpagibigNumber long,
	in vphilhealthNumber long,
	in vtinNumber long,
	in vcashcardNumber long,
	in vpicture varchar(255)
)

begin
	update employee
    set lastName = vlastname, firstName = vfirstName, middleName = vmiddleName,
		gender = vgender, birthDate = vbirthDate, civilStatus = vcivilStatus,
        address = vaddress, contactNumber = vcontactNumber, sssNumber = 
        vsssNumber, pagibigNumber = vpagibigNumber, philhealthNumber = 
        vphilhealthNumber, tinNumber = vtinNumber, cashcardNumber = 
        vcashcardNumber, picture = vpicture
	where id = vid;
end //


create procedure deleteEmployee
(
	in vid int
)

begin
	update employee
    set deletedFlag = true
    where id = vid;
end //

delimiter ;