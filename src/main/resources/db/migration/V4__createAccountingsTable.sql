CREATE TABLE accountings
(
    id              VARCHAR(255) NOT NULL,
    accounting_date date         NULL,
    employee_id     VARCHAR(255) NULL,
    invoice_status  VARCHAR(255) NULL,
    ap_invoice_id   VARCHAR(255) NULL,
    CONSTRAINT pk_accountings PRIMARY KEY (id)
);

ALTER TABLE accountings
    ADD CONSTRAINT FK_ACCOUNTINGS_ON_APINVOICE FOREIGN KEY (ap_invoice_id) REFERENCES ap_invoices (id);

ALTER TABLE accountings
    ADD CONSTRAINT FK_ACCOUNTINGS_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);