delimiter //

create procedure addPosition
(
	in vposition varchar(255),
    in vpositionShortCode varchar (31)
)

begin
    insert into position (position, positionShortCode)
    values (vposition, vpositionShortCode);
end //


create procedure updatePosition
(
	in vid int,
	in vposition varchar(255),
    in vpositionShortCode varchar (31)
)

begin
	update position
    set position = vposition, positionShortCode = vpositionShortCode
	where id = vid;
end //


create procedure deletePosition
(
	in vid int
)

begin
	update position
    set deletedFlag = true
    where id = vid;
end //

delimiter ;