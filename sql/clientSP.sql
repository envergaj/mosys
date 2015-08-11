delimiter //

create procedure addClient
(
	in vclient varchar(255),
    in vclientShortCode varchar (31)
)

begin
    insert into client (client, clientShortCode)
    values (vclient, vclientShortCode);
end //


create procedure updateClient
(
	in vid int,
	in vclient varchar(255),
    in vclientShortCode varchar (31)
)

begin
	update client
    set client = vclient, clientShortCode = vclientShortCode
	where id = vid;
end //


create procedure deleteClient
(
	in vid int
)

begin
	update client
    set deletedFlag = true
    where id = vid;
end //

delimiter ;