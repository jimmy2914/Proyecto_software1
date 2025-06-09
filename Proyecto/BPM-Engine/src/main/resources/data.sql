-- Crear la tabla si no existe (opcional, si no usas JPA ddl-auto)
CREATE TABLE IF NOT EXISTS factura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(50) UNIQUE NOT NULL,
    nombre_cliente VARCHAR(100),
    documento_cliente VARCHAR(20),
    fecha_emision DATE,
    total NUMERIC(10,2),
    estado VARCHAR(20),
    metodo_pago VARCHAR(30),
    canal_venta VARCHAR(30),
    observaciones VARCHAR(255)
);

-- Verificar si existe FAC-001 y solo insertar si no existe
INSERT INTO factura (numero, nombre_cliente, documento_cliente, fecha_emision, total, estado, metodo_pago, canal_venta, observaciones)
SELECT 'FAC-001', 'Juan Perez', '12345678', DATE '2025-06-01', 150000, 'EMITIDA', 'Tarjeta', 'Online', 'Cliente frecuente'
WHERE NOT EXISTS (SELECT 1 FROM factura WHERE numero = 'FAC-001');

-- Verificar si existe FAC-002 y solo insertar si no existe
INSERT INTO factura (numero, nombre_cliente, documento_cliente, fecha_emision, total, estado, metodo_pago, canal_venta, observaciones)
SELECT 'FAC-002', 'Laura Gomez', '87654321', DATE '2025-06-02', 95000, 'EMITIDA', 'Efectivo', 'Presencial', NULL
WHERE NOT EXISTS (SELECT 1 FROM factura WHERE numero = 'FAC-002');

-- Verificar si existe FAC-003 y solo insertar si no existe
INSERT INTO factura (numero, nombre_cliente, documento_cliente, fecha_emision, total, estado, metodo_pago, canal_venta, observaciones)
SELECT 'FAC-003', 'Carlos Méndez', '11223344', DATE '2025-06-03', 325000, 'EMITIDA', 'Transferencia', 'Online', 'Pago corporativo'
WHERE NOT EXISTS (SELECT 1 FROM factura WHERE numero = 'FAC-003');

