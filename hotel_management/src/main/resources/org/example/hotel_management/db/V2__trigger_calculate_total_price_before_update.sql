CREATE TRIGGER calculate_total_price_before_update
    BEFORE UPDATE ON booking_services
    FOR EACH ROW
BEGIN
    DECLARE servicePrice double;

    SELECT price INTO servicePrice
    FROM services
    WHERE id = NEW.service_id;

    SET NEW.total_price = NEW.quantity * servicePrice;
END