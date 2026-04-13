insert into system_profiles (name, members, status, created) values 
('Administrador de Sistema', '1', '1', now()),
('Director Jurídico y Fiduciario', '1', '1', now()),
('Subdirector Jurídico y Fiduciario', 1, '1', now()),
('Coordinador Mesa Fiduciaria', '1', '1', now()),
('Analista Mesa Fiduciaria', null, '1', now()),
('Coordinador Operación Fiduciario', '1', '1', now()),
('Analista Operación Fiduciario', null, '1', now()),
('Subdirector Jurídico', '1', '1', now()),
('Analista Jurídico', null, '1', now()),
('Oficial Seguridad Información', '1', '1', now()),
('Analista Seguridad Información', null, '1', now()),
('Gerente PLD', '1', '1', now()),
('Auxiliar PLD', null, '1', now()),
('Director General Adjunto', '1', '1', now()),
('Subdirector de Métodos y Procedimientos', '1', '1', now()),
('Gerente de Métodos y Procedimientos', '1', '1', now()),
('Subdirector de Soporte a Produccion y Proyectos', '1', '1', now()),
('Dirección de Operaciones', '1', '1', now()),
('Subdirector de Sistemas', '1', '1', now()),
('Analista de Sistemas', null, '1', now());

INSERT INTO catalog_persons (person_id, first_name, second_name, last_name, second_last_name, gender, rfc, curp, birth_date, created) values 
('1', 'Héctor', 'Giovanni', 'Rodríguez', 'Ramos', 1, 'XAXX000000XX0', 'XAXX000000XXXXXX00', '1990-01-19', '2025-06-04 21:49:48.000000'), 
('2', 'Claudia', '', 'Vázquez', 'Luna', '2', 'XAXX000000XX0', 'XAXX000000XXXXXX00', '1990-01-01', '2025-07-25 16:53:43.000000'), 
('3', 'Anaid', '', 'Santillán', 'Guzmán', '2', 'XAXX000000XX0', 'XAXX000000XXXXXX00', '1990-01-01', '2025-07-25 16:53:43.000000'),
('4', 'Katia', 'Georgina', 'Melo', 'Guzmán', '2', 'XAXX000000XX0', 'XAXX000000XXXXXX00', '1990-01-01', '2025-07-25 16:53:43.000000'),
('5', 'Guillermo', '', 'González', 'Pérez', '2', 'XAXX000000XX0', 'XAXX000000XXXXXX00', '1990-01-01', '2025-07-25 16:53:43.000000');

INSERT INTO system_users (user_id, profile_id, person_id, nickname, access,  email, status, created) VALUES 
('1', '1', '1', 'grodriguez', '$2a$12$JFGlwtVNANExutXEDwZFv.Oy2oXL//xvpy2LvMoLNYPKoN53m5gOO', 'grodriguez@bcbcasadebolsa.com', '1', '2025-06-04 21:49:48.000000'),
('2', '4', '2', 'cvazquez', '$2a$12$JFGlwtVNANExutXEDwZFv.Oy2oXL//xvpy2LvMoLNYPKoN53m5gOO', 'cvazquez@bcbcasadebolsa.com', '1', '2025-07-25 16:53:43.000000'),
('3', '6', '3', 'asantillan', '$2a$12$JFGlwtVNANExutXEDwZFv.Oy2oXL//xvpy2LvMoLNYPKoN53m5gOO', 'asantillan@bcbcasadebolsa.com', '1', '2025-07-25 16:53:43.000000'),
('4', '2', '4', 'kmelo', '$2a$12$JFGlwtVNANExutXEDwZFv.Oy2oXL//xvpy2LvMoLNYPKoN53m5gOO', 'kmelo@bcbcasadebolsa.com', '1', '2025-07-25 16:53:43.000000'),
('5', '3', '5', 'ggonzalez', '$2a$12$JFGlwtVNANExutXEDwZFv.Oy2oXL//xvpy2LvMoLNYPKoN53m5gOO', 'ggonzalez@bcbcasadebolsa.com', '1', '2025-07-25 16:53:43.000000');

insert into trust_trust_types (status, created, description, name) 
values ('1', now(), '', 'Fideicomiso de Administración');

insert into catalog_file_types (created, status, name) values 
(now(), '1', 'Identificación Oficial'),
(now(), '1', 'Comprobante de Domicilio');

insert into catalog_file_subtypes (created, status, name, mime_type, file_type_id) values
(now(), '1', 'Credencial de elector emitida por el Instituto Nacional Electoral (INE) o por un Instituto Electoral Estatal (INE).', '1', '1'),
(now(), '1', 'Pasaporte emitido por la Secretaría de Relaciones Exteriores (SRE).', '1', '1'),
(now(), '1', 'Cédula Profesional (nuevo formato).', '1', '1'),
(now(), '1', 'Cartilla del servicio militar nacional (10 años máximo de antigüedad).', '1', '1'),
(now(), '1', 'Comprobante de suministro de energía eléctrica.', '1', '2'),
(now(), '1', 'Comprobante de suministro de servicio telefónico (excepto telefonía celular).', '1', '2'),
(now(), '1', 'Comprobante de suministro de servicio de agua potable.', '1', '2'),
(now(), '1', 'Comprobante de suministro de servicio de gas natural.', '1', '2'),
(now(), '1', 'Comprobante de impuesto predial.', '1', '2'),
(now(), '1', 'Comprobante de arrendamiento registrado ante la autoridad fiscal,  vigente a la fecha de presentación por el Cliente.', '1', '2'),
(now(), '1', 'Estado de cuenta de institución bancaria (NO tienda departamental o comercial).', '1', '2'),
(now(), '1', 'Comprobante de inscripción ante el Registro Federal de Contribuyentes.', '1', '2');

insert into catalog_document_types (created, status, owner_type, person_type, foreign_status, name, description) values 
(now(), '1', '1', '1', '1', 'Formato KYC', 'Formato "Conocimiento del cliente" para Fideicomiso, con información debidamente completada y firma autógrafa'),
(now(), '1', '1', '1', '1', 'Aviso de privacidad', 'Aviso de privacidad con firma autógrafa'),
(now(), '1', '1', '1', '1', 'Identificación personal oficial', 'Copia Identificación personal oficial*, emitido por autoridad competente, vigente con fotografía, firma legibles y en su caso, domicilio.'),
(now(), '1', '1', '1', '1', 'Cédula de identificación fiscal', 'Copia de la cédula de identificación fiscal (RFC).'),
(now(), '1', '1', '1', '1', 'Clave Única de Registro de Población', 'Copia de la constancia de la Clave Única de Registro de Población (CURP). No será necesario si ésta aparece en identificación oficial.'),
(now(), '1', '1', '1', '1', 'Comprobante de domicilio', 'Del domicilio declarado en el contrato y en el formato de Conocimiento del Cliente). Cuando el domicilio manifestado en el contrato coincida con el de la credencial para votar, esta funcionará como el comprobante de domicilio.'),
(now(), '1', '1', '1', '1', 'Formato "Conocimiento del cliente" para Participantes', 'Formato "Conocimiento del cliente" para Participantes, con información debidamente completada y firma autógrafa"'),
(now(), '1', '2', '1', '1', 'Documento Apoderado', 'Copia certificada del documento expedido por fedatario público, que acredite las facultades conferidas al apoderado.'),
(now(), '1', '1', '2', '1', 'Comprobante registro público', 'Testimonio o copia certificada del instrumento público que acredite la legal existencia inscrito en el registro público.'),
(now(), '1', '1', '2', '1', 'Cédula de identificación fiscal', 'Copia de la cédula de identificación fiscal (RFC).'),
(now(), '1', '1', '2', '1', 'Comprobante de domicilio', 'Copia de comprobante de domicilio** (del domicilio declarado en el contrato y en el formato de Conocimiento del Cliente).'),
(now(), '1', '1', '2', '1', 'Formato KYC', 'Formato  "Conocimiento del cliente" para Participantes, con información debidamente completada y con firma autógrafa del apoderado.'),
(now(), '1', '1', '2', '1', 'Formato de Autocertificación (FATCA)', 'Formato de Autocertificación (FATCA) con información debidamente completada y con firma autógrafa del apoderado.'),
(now(), '1', '2', '2', '1', 'Documento Apoderado', 'Copia certificada del documento expedido por fedatario público, que acredite las facultades conferidas al apoderado.'),
(now(), '1', '2', '2', '1', 'Documento Apoderado 2', 'Presentar del Apoderado los documentos relacionados para Personas Físicas de nacionalidad mexicana o de nacionalidad extranjera');







