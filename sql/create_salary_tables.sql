-- Tables for salary calculation module
-- Run this script against the qlns database

CREATE TABLE IF NOT EXISTS payrolls (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    month INT NOT NULL,
    year INT NOT NULL,
    total_gross DOUBLE DEFAULT 0,
    total_net DOUBLE DEFAULT 0,
    total_tax DOUBLE DEFAULT 0,
    total_insurance DOUBLE DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_payroll_month_year (month, year)
);

CREATE TABLE IF NOT EXISTS payroll_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payroll_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    basic_salary DOUBLE DEFAULT 0,
    allowance DOUBLE DEFAULT 0,
    actual_working_days INT DEFAULT 0,
    standard_working_days INT DEFAULT 0,
    overtime_hours INT DEFAULT 0,
    gross_salary DOUBLE DEFAULT 0,
    social_insurance DOUBLE DEFAULT 0,
    health_insurance DOUBLE DEFAULT 0,
    unemployment_insurance DOUBLE DEFAULT 0,
    taxable_income DOUBLE DEFAULT 0,
    income_tax DOUBLE DEFAULT 0,
    net_salary DOUBLE DEFAULT 0,
    FOREIGN KEY (payroll_id) REFERENCES payrolls(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS salary_parameters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    social_insurance_rate DOUBLE DEFAULT 0.08,
    health_insurance_rate DOUBLE DEFAULT 0.015,
    unemployment_insurance_rate DOUBLE DEFAULT 0.01,
    personal_deduction DOUBLE DEFAULT 11000000,
    dependent_deduction DOUBLE DEFAULT 4400000,
    overtime_rate DOUBLE DEFAULT 1.5,
    standard_working_days INT DEFAULT 26,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tax_brackets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    param_id BIGINT NOT NULL,
    min_income DOUBLE DEFAULT 0,
    max_income DOUBLE DEFAULT 0,
    tax_rate DOUBLE DEFAULT 0,
    FOREIGN KEY (param_id) REFERENCES salary_parameters(id) ON DELETE CASCADE
);
