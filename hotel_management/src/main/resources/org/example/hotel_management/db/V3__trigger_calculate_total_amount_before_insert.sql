CREATE TRIGGER calculate_total_amount_before_insert
    BEFORE INSERT ON invoices
    FOR EACH ROW
BEGIN
    declare v_total_service_charge double default 0;
	declare v_price_per_hours double;
    declare v_total_usage_time double default 0;
	declare v_check_in datetime default null;
    declare v_check_out datetime default null;

    select coalesce(SUM(total_price), 0) into v_total_service_charge
    from booking_services
    where NEW.`booking_id` = booking_services.`booking_id`;

    if v_total_service_charge is not null then
		set NEW.`total_service_charge` = v_total_service_charge;
end if;

select `price_per_hours`, `check_in`, `check_out` into v_price_per_hours, v_check_in, v_check_out
from bookings as b
         join rooms as r on b.room_id = r.id
where b.`id` = NEW.`booking_id`;

IF v_check_in IS NOT NULL AND v_check_out IS NOT NULL THEN
        set v_total_usage_time = timestampdiff(minute, v_check_in, v_check_out) / 60.0;
        if v_total_usage_time < 0 then
            set v_total_usage_time = 0;
end if;
end if;

    set NEW.`total_room_charge` = v_price_per_hours * v_total_usage_time;

    set NEW.`total_amount` = COALESCE(NEW.`total_room_charge`,0) + COALESCE(NEW.`total_service_charge`,0);
END