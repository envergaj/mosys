delimiter //

create procedure addWorkLocation
(
	in vworkLocation varchar(255),
    in vlocationShortCode varchar (31)
)

begin
    insert into workLocation (workLocation, locationShortCode)
    values (vworkLocation, vlocationShortCode);
end //


create procedure updateWorkLocation
(
	in vid int,
	in vworkLocation varchar(255),
    in vlocationShortCode varchar (31)
)

begin
	update workLocation
    set workLocation = vworkLocation, locationShortCode = vlocationShortCode
	where id = vid;
end //


create procedure deleteWorkLocation
(
	in vid int
)

begin
	update workLocation
    set deletedFlag = true
    where id = vid;
end //

delimiter ;