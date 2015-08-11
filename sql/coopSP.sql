delimiter //

create procedure addCoop
(
	in vcoop varchar(255),
    in vcoopShortCode varchar (31),
    in vaddress varchar (1027),
    in vsssEmployerNumber long,
    in vphicEmployerNumber long,
    in vhdmfEmployerNumber long,
    in vtinEmployerNumber long
)

begin
    insert into coop (coop, coopShortCode, address, sssEmployerNumber,
		phicEmployerNumber, hdmfEmployerNumber, tinEmployerNumber)
	values (vcoop, vcoopShortCode, vaddress, vsssEmployerNumber,
		vphicEmployerNumber, vhdmfEmployerNumber, vtinEmployerNumber);
end //


create procedure updateCoop
(
	in vid int,
    in vcoop varchar(255),
    in vcoopShortCode varchar(31),
    in vaddress varchar (1027),
    in vsssEmployerNumber long,
    in vphicEmployerNumber long,
    in vhdmfEmployerNumber long,
    in vtinEmployerNumber long
)

begin
	update coop
    set coop = vcoop, coopShortCode = vcoopShortCode, address = vaddress,
		sssEmployerNumber = vsssEmployerNumber,phicEmployerNumber = 
        vphicEmployerNumber, hdmfEmployerNumber = vhdmfEmployerNumber,
        tinEmployerNumber = vtinEmployerNumber
	where id = vid;
end //


create procedure deleteCoop
(
	in vid int
)

begin
	update coop
    set deletedFlag = true
    where id = vid;
end //

delimiter ;