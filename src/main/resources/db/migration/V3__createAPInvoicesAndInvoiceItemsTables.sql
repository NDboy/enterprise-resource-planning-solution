CREATE TABLE ap_invoices
(
    id             VARCHAR(255) NOT NULL,
    inv_num        VARCHAR(255) NULL,
    invoice_status VARCHAR(255) NULL,
    payment_mode   VARCHAR(255) NULL,
    invoicing_date date         NULL,
    due_date       date         NULL,
    gross_value    double       NOT NULL,
    employee_id    VARCHAR(255) NULL,
    partner_id     VARCHAR(255) NULL,
    CONSTRAINT pk_ap_invoices PRIMARY KEY (id)
);

ALTER TABLE ap_invoices
    ADD CONSTRAINT FK_AP_INVOICES_ON_EMPLOYEE FOREIGN KEY (employee_id) REFERENCES employees (id);

ALTER TABLE ap_invoices
    ADD CONSTRAINT FK_AP_INVOICES_ON_PARTNER FOREIGN KEY (partner_id) REFERENCES partners (id);

CREATE TABLE apinvoice_invoice_items
(
    apinvoice_id VARCHAR(255) NOT NULL,
    item_name    VARCHAR(255) NULL,
    net_price    DOUBLE       NULL,
    vat_rate     INT          NULL,
    vat_amount   DOUBLE       NULL,
    gross_price  DOUBLE       NULL
);

ALTER TABLE apinvoice_invoice_items
    ADD CONSTRAINT fk_apinvoice_invoiceitems_on_a_p_invoice FOREIGN KEY (apinvoice_id) REFERENCES ap_invoices (id);