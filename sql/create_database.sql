-- ============================================================
-- HR Management System (Quản lý nhân sự) — Complete Database
-- Database: qlns (MySQL 8+)
-- ============================================================
-- Usage:
--   mysql -u root -p < sql/create_database.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS qlns
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE qlns;

-- ============================================================
-- 1. USERS (tài khoản đăng nhập + thông tin cá nhân)
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id     INT             AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    email       VARCHAR(100)    DEFAULT NULL,
    role        VARCHAR(20)     NOT NULL DEFAULT 'EMPLOYEE',
    -- Thông tin cá nhân
    full_name           VARCHAR(100)    DEFAULT NULL,
    date_of_birth       DATE            DEFAULT NULL,
    gender              VARCHAR(10)     DEFAULT NULL,
    phone               VARCHAR(20)     DEFAULT NULL,
    citizen_id          VARCHAR(20)     DEFAULT NULL,
    address             VARCHAR(255)    DEFAULT NULL,
    basic_salary        DOUBLE          DEFAULT NULL,
    dependent_number    INT             DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 2. DEPARTMENTS (phòng ban)
-- ============================================================
CREATE TABLE IF NOT EXISTS departments (
    department_id   INT             AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    code            VARCHAR(20)     NOT NULL UNIQUE,
    manager_name    VARCHAR(100)    DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 3. EMPLOYEES (nhân viên)
-- ============================================================
CREATE TABLE IF NOT EXISTS employees (
    employee_id         INT             PRIMARY KEY,
    employee_code       VARCHAR(20)     NOT NULL UNIQUE,
    hometown            VARCHAR(100)    DEFAULT NULL,
    base_salary         DOUBLE          DEFAULT NULL,
    fixed_allowance     DOUBLE          DEFAULT NULL,
    bank_account        VARCHAR(50)     DEFAULT NULL,
    bank_name           VARCHAR(100)    DEFAULT NULL,
    bank_holder         VARCHAR(100)    DEFAULT NULL,
    tax_code            VARCHAR(20)     DEFAULT NULL,
    social_insurance    VARCHAR(20)     DEFAULT NULL,
    position            VARCHAR(100)    DEFAULT NULL,
    department_id       INT             DEFAULT NULL,
    start_date          VARCHAR(20)     DEFAULT NULL,
    contract_type       VARCHAR(50)     DEFAULT NULL,
    status              VARCHAR(20)     DEFAULT 'Đang làm việc',
    qualification       VARCHAR(100)    DEFAULT NULL,
    major               VARCHAR(100)    DEFAULT NULL,
    experience          VARCHAR(50)     DEFAULT NULL,
    FOREIGN KEY (employee_id)   REFERENCES users(user_id)          ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 4. SCHEDULES (lịch làm việc / công tác)
-- ============================================================
CREATE TABLE IF NOT EXISTS schedules (
    schedule_id     INT             AUTO_INCREMENT PRIMARY KEY,
    employee_id     INT             NOT NULL,
    date            VARCHAR(10)     NOT NULL,
    shift_type      VARCHAR(50)     DEFAULT NULL,
    start_time      VARCHAR(10)     DEFAULT NULL,
    end_time        VARCHAR(10)     DEFAULT NULL,
    status          VARCHAR(20)     DEFAULT NULL,
    event_name      VARCHAR(200)    DEFAULT NULL,
    location        VARCHAR(200)    DEFAULT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 5. CONTRACTS (hợp đồng lao động)
-- ============================================================
CREATE TABLE IF NOT EXISTS contracts (
    contract_id     INT             AUTO_INCREMENT PRIMARY KEY,
    employee_id     INT             NOT NULL,
    contract_code   VARCHAR(50)     NOT NULL UNIQUE,
    contract_type   VARCHAR(50)     NOT NULL,
    start_date      DATE            NOT NULL,
    end_date        DATE            DEFAULT NULL,
    base_salary     DOUBLE          NOT NULL DEFAULT 0,
    allowance       DOUBLE          DEFAULT 0,
    position        VARCHAR(100)    DEFAULT NULL,
    department_id   INT             DEFAULT NULL,
    status          VARCHAR(20)     DEFAULT 'Có hiệu lực',
    created_by      VARCHAR(100)    DEFAULT NULL,
    created_date    DATE            DEFAULT NULL,
    notes           TEXT            DEFAULT NULL,
    FOREIGN KEY (employee_id)   REFERENCES employees(employee_id)   ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 6. RECRUITMENT — JOB POSTINGS (tin tuyển dụng)
-- ============================================================
CREATE TABLE IF NOT EXISTS job_posting (
    job_id          INT             AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(200)    NOT NULL,
    description     TEXT            DEFAULT NULL,
    requirement     TEXT            DEFAULT NULL,
    quantity        INT             DEFAULT 1,
    salary          DOUBLE          DEFAULT 0,
    deadline        DATE            DEFAULT NULL,
    status          VARCHAR(50)     DEFAULT 'OPEN',
    created_date    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    created_by      INT             DEFAULT NULL,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 7. RECRUITMENT — CANDIDATES (ứng viên — standalone)
-- ============================================================
CREATE TABLE IF NOT EXISTS candidate (
    candidate_id    INT             AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100)    NOT NULL,
    gender          VARCHAR(20)     DEFAULT NULL,
    birthday        DATE            DEFAULT NULL,
    phone           VARCHAR(20)     DEFAULT NULL,
    email           VARCHAR(100)    DEFAULT NULL,
    address         TEXT            DEFAULT NULL,
    education       TEXT            DEFAULT NULL,
    experience      TEXT            DEFAULT NULL,
    cv_file         VARCHAR(500)    DEFAULT NULL,
    status          VARCHAR(50)     DEFAULT 'NEW',
    created_date    TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 8. RECRUITMENT — APPLICATION REVIEWS (đánh giá hồ sơ)
-- ============================================================
CREATE TABLE IF NOT EXISTS application_review (
    review_id       INT             AUTO_INCREMENT PRIMARY KEY,
    candidate_id    INT             NOT NULL,
    reviewer_id     INT             NOT NULL,
    score           DOUBLE          DEFAULT NULL,
    comment         TEXT            DEFAULT NULL,
    result          VARCHAR(50)     DEFAULT NULL,
    review_date     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidate(candidate_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id)  REFERENCES users(user_id)          ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 9. RECRUITMENT — INTERVIEW SCHEDULES (lịch phỏng vấn)
-- ============================================================
CREATE TABLE IF NOT EXISTS interview_schedule (
    schedule_id     INT             AUTO_INCREMENT PRIMARY KEY,
    candidate_id    INT             NOT NULL,
    interviewer     VARCHAR(255)    DEFAULT NULL,
    interview_date  DATETIME        DEFAULT NULL,
    location        VARCHAR(255)    DEFAULT NULL,
    note            TEXT            DEFAULT NULL,
    status          VARCHAR(50)     DEFAULT 'SCHEDULED',
    FOREIGN KEY (candidate_id) REFERENCES candidate(candidate_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 10. RECRUITMENT — INTERVIEW EVALUATIONS (đánh giá phỏng vấn)
-- ============================================================
CREATE TABLE IF NOT EXISTS interview_evaluation (
    evaluation_id   INT             AUTO_INCREMENT PRIMARY KEY,
    schedule_id     INT             NOT NULL,
    score           DOUBLE          DEFAULT NULL,
    comment         TEXT            DEFAULT NULL,
    result          VARCHAR(50)     DEFAULT NULL,
    evaluation_date TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (schedule_id) REFERENCES interview_schedule(schedule_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 11. ATTENDANCE — PERIODS (kỳ chấm công)
-- ============================================================
CREATE TABLE IF NOT EXISTS attendance_periods (
    id      BIGINT  AUTO_INCREMENT PRIMARY KEY,
    month   INT     NOT NULL,
    year    INT     NOT NULL,
    UNIQUE KEY uk_attendance_month_year (month, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 12. ATTENDANCE — DETAILS (chi tiết chấm công)
-- ============================================================
CREATE TABLE IF NOT EXISTS attendance_details (
    id                  BIGINT      AUTO_INCREMENT PRIMARY KEY,
    period_id           BIGINT      NOT NULL,
    employee_id         BIGINT      NOT NULL,
    employee_code       VARCHAR(20) DEFAULT NULL,
    employee_name       VARCHAR(100) DEFAULT NULL,
    actual_working_days INT         DEFAULT 0,
    standard_days       INT         DEFAULT 0,
    overtime_hours      INT         DEFAULT 0,
    late_count          INT         DEFAULT 0,
    early_count         INT         DEFAULT 0,
    unpaid_leave        INT         DEFAULT 0,
    paid_leave          INT         DEFAULT 0,
    status              VARCHAR(20) DEFAULT NULL,
    basic_salary        DOUBLE      DEFAULT 0,
    allowance           DOUBLE      DEFAULT 0,
    dependent_number    INT         DEFAULT 0,
    FOREIGN KEY (period_id) REFERENCES attendance_periods(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 13. SALARY — PAYROLLS (bảng lương)
-- ============================================================
CREATE TABLE IF NOT EXISTS payrolls (
    id              BIGINT      AUTO_INCREMENT PRIMARY KEY,
    month           INT         NOT NULL,
    year            INT         NOT NULL,
    total_gross     DOUBLE      DEFAULT 0,
    total_net       DOUBLE      DEFAULT 0,
    total_tax       DOUBLE      DEFAULT 0,
    total_insurance DOUBLE      DEFAULT 0,
    created_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_payroll_month_year (month, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 14. SALARY — PAYROLL DETAILS (chi tiết bảng lương)
-- ============================================================
CREATE TABLE IF NOT EXISTS payroll_details (
    id                      BIGINT  AUTO_INCREMENT PRIMARY KEY,
    payroll_id              BIGINT  NOT NULL,
    employee_id             BIGINT  NOT NULL,
    basic_salary            DOUBLE  DEFAULT 0,
    allowance               DOUBLE  DEFAULT 0,
    actual_working_days     INT     DEFAULT 0,
    standard_working_days   INT     DEFAULT 0,
    overtime_hours          INT     DEFAULT 0,
    gross_salary            DOUBLE  DEFAULT 0,
    social_insurance        DOUBLE  DEFAULT 0,
    health_insurance        DOUBLE  DEFAULT 0,
    unemployment_insurance  DOUBLE  DEFAULT 0,
    taxable_income          DOUBLE  DEFAULT 0,
    income_tax              DOUBLE  DEFAULT 0,
    net_salary              DOUBLE  DEFAULT 0,
    FOREIGN KEY (payroll_id) REFERENCES payrolls(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 15. SALARY — PARAMETERS (tham số tính lương)
-- ============================================================
CREATE TABLE IF NOT EXISTS salary_parameters (
    id                          BIGINT      AUTO_INCREMENT PRIMARY KEY,
    social_insurance_rate       DOUBLE      DEFAULT 0.08,
    health_insurance_rate       DOUBLE      DEFAULT 0.015,
    unemployment_insurance_rate DOUBLE      DEFAULT 0.01,
    personal_deduction          DOUBLE      DEFAULT 11000000,
    dependent_deduction         DOUBLE      DEFAULT 4400000,
    overtime_rate               DOUBLE      DEFAULT 1.5,
    standard_working_days       INT         DEFAULT 26,
    updated_at                  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 16. SALARY — TAX BRACKETS (biểu thuế thu nhập)
-- ============================================================
CREATE TABLE IF NOT EXISTS tax_brackets (
    id          BIGINT  AUTO_INCREMENT PRIMARY KEY,
    param_id    BIGINT  NOT NULL,
    min_income  DOUBLE  DEFAULT 0,
    max_income  DOUBLE  DEFAULT 0,
    tax_rate    DOUBLE  DEFAULT 0,
    FOREIGN KEY (param_id) REFERENCES salary_parameters(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- SAMPLE DATA
-- ============================================================

-- Phòng ban
INSERT IGNORE INTO departments (department_id, name, code, manager_name) VALUES
(1, 'Phòng Nhân sự', 'HR', 'Nguyễn Văn A'),
(2, 'Phòng Kế toán', 'ACC', 'Trần Thị B'),
(3, 'Phòng IT', 'IT', 'Lê Văn C'),
(4, 'Phòng Kinh doanh', 'SALE', 'Phạm Thị D');

-- Tài khoản Admin (password: admin123)
INSERT IGNORE INTO users (user_id, username, password, email, role, full_name) VALUES
(1, 'admin', 'admin123', 'admin@company.com', 'ADMIN', 'Quản trị viên'),
(2, 'hr01', '123456', 'hr@company.com', 'HR', 'Nhân viên HR'),
(3, 'acc01', '123456', 'acc@company.com', 'ACCOUNTANT', 'Nhân viên Kế toán'),
(4, 'emp01', '123456', 'emp@company.com', 'EMPLOYEE', 'Nguyễn Văn A'),
(5, 'emp02', '123456', 'emp2@company.com', 'EMPLOYEE', 'Trần Thị B');

-- Nhân viên mẫu
INSERT IGNORE INTO employees (employee_id, employee_code, position, department_id, base_salary, start_date, status) VALUES
(4, 'NV001', 'Nhân viên', 1, 8000000, '2024-01-01', 'Đang làm việc'),
(5, 'NV002', 'Nhân viên', 2, 7500000, '2024-02-01', 'Đang làm việc');
