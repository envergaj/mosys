delimiter //

create procedure addDeduction
(
	in vdeduction varchar(255),
    in vdeductionShortCode varchar (31)
)

begin
    insert into deduction (deduction, deductionShortCode)
    values (vdeduction, vdeductionShortCode);
end //


create procedure updateDeduction
(
	in vid int,
	in vdeduction varchar(255),
    in vdeductionShortCode varchar (31)
)

begin
	update deduction
    set deduction = vdeduction, deductionShortCode = vdeductionShortCode
	where id = vid;
end //


create procedure deleteDeduction
(
	in vid int
)

begin
	update deduction
    set deletedFlag = true
    where id = vid;
end //

delimiter ;