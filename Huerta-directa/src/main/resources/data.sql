INSERT INTO roles (rol_name)
SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE rol_name = 'ADMIN');

INSERT INTO roles (rol_name)
SELECT 'VENDEDOR' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE rol_name = 'VENDEDOR');

INSERT INTO users (email, name, password, role_id, creacion_date)
SELECT 'jjpp142007@gmail.com', 'Administrador Principal',
       '$2a$10$N.zmdr9zkMmazpVn3SE5muBRrTRCPmVQJFuiaCvTECGT2NqUo6pBC',
       (SELECT id_rol FROM roles WHERE rol_name = 'ADMIN'),
       CURRENT_DATE
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@huertadirecta.com');

INSERT INTO users (email, name, password, role_id, creacion_date)
SELECT 'admin2@huertadirecta.com', 'Administrador Secundario',
       '$2a$10$N.zmdr9zkMmazpVn3SE5muBRrTRCPmVQJFuiaCvTECGT2NqUo6pBC',
       (SELECT id_rol FROM roles WHERE rol_name = 'ADMIN'),
       CURRENT_DATE
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin2@huertadirecta.com');