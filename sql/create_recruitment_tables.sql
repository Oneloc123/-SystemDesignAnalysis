-- =====================================================
-- Recruitment Management Module - Database Tables
-- Compatible with existing qlns database and users table
-- =====================================================

CREATE DATABASE IF NOT EXISTS qlns;
USE qlns;

-- 1. Job Posting table
-- Created by HR/Admin users for job listings
CREATE TABLE IF NOT EXISTS job_posting (
    job_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    requirement TEXT,
    quantity INT DEFAULT 1,
    salary DOUBLE,
    deadline DATE,
    status VARCHAR(50) DEFAULT 'OPEN',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- 2. Candidate table (standalone, NOT linked to users table)
-- Candidates submit CVs without needing a login account
CREATE TABLE IF NOT EXISTS candidate (
    candidate_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    gender VARCHAR(20),
    birthday DATE,
    phone VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    education TEXT,
    experience TEXT,
    cv_file VARCHAR(500),
    status VARCHAR(50) DEFAULT 'NEW',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Application Review table
-- HR users review candidates and record scores/comments
CREATE TABLE IF NOT EXISTS application_review (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    candidate_id INT NOT NULL,
    reviewer_id INT NOT NULL,
    score DOUBLE,
    comment TEXT,
    result VARCHAR(50),
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidate(candidate_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id)
);

-- 4. Interview Schedule table
-- Schedules interviews for approved candidates
CREATE TABLE IF NOT EXISTS interview_schedule (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    candidate_id INT NOT NULL,
    interviewer VARCHAR(255),
    interview_date DATETIME,
    location VARCHAR(255),
    note TEXT,
    status VARCHAR(50) DEFAULT 'SCHEDULED',
    FOREIGN KEY (candidate_id) REFERENCES candidate(candidate_id)
);

-- 5. Interview Evaluation table
-- Records interview results (pass/fail) with scores and comments
CREATE TABLE IF NOT EXISTS interview_evaluation (
    evaluation_id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id INT NOT NULL,
    score DOUBLE,
    comment TEXT,
    result VARCHAR(50),
    evaluation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (schedule_id) REFERENCES interview_schedule(schedule_id)
);
