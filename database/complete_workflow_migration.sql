-- Treatment Workflow Migration Script
-- Implements business rules: 1 Doctor:N Patients, 1 Patient:1 Active Plan, Access Control

USE [Infertility_Treatment];
GO

PRINT '=== TREATMENT WORKFLOW MIGRATION ===';

-- Step 1: Add treating_doctor_id to treatment_plan
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_plan') AND name = 'treating_doctor_id')
BEGIN
    ALTER TABLE treatment_plan ADD treating_doctor_id UNIQUEIDENTIFIER;
    PRINT '✓ Added treating_doctor_id field';
END

-- Step 2: Add workflow fields
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_plan') AND name = 'is_locked')
    ALTER TABLE treatment_plan ADD is_locked BIT DEFAULT 0;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_plan') AND name = 'version_number')
    ALTER TABLE treatment_plan ADD version_number INT DEFAULT 1;

-- Step 3: Business Rule - 1 Patient = 1 Active Plan
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID('treatment_plan') AND name = 'UK_one_active_plan_per_patient')
BEGIN
    CREATE UNIQUE NONCLUSTERED INDEX UK_one_active_plan_per_patient 
    ON treatment_plan (patient_id)
    WHERE status = 'active';
    PRINT '✓ Added constraint: 1 patient = 1 active plan';
END

-- Step 4: Foreign Key for treating doctor
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_treatment_plan_treating_doctor')
BEGIN
    ALTER TABLE treatment_plan ADD CONSTRAINT FK_treatment_plan_treating_doctor 
    FOREIGN KEY (treating_doctor_id) REFERENCES users(user_id);
    PRINT '✓ Added FK: treating_doctor_id → users';
END

-- Step 5: Enhance treatment_schedule table
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_schedule') AND name = 'doctor_id')
    ALTER TABLE treatment_schedule ADD doctor_id UNIQUEIDENTIFIER;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_schedule') AND name = 'patient_id')
    ALTER TABLE treatment_schedule ADD patient_id UNIQUEIDENTIFIER;

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_schedule') AND name = 'milestone_type')
    ALTER TABLE treatment_schedule ADD milestone_type NVARCHAR(50);

IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('treatment_schedule') AND name = 'appointment_deadline')
    ALTER TABLE treatment_schedule ADD appointment_deadline DATETIME2;

-- Step 6: Create audit trail table
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'treatment_workflow_log')
BEGIN
    CREATE TABLE treatment_workflow_log (
        log_id UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
        treatment_plan_id UNIQUEIDENTIFIER NOT NULL,
        action_type NVARCHAR(50) NOT NULL,
        previous_status NVARCHAR(20),
        new_status NVARCHAR(20),
        performed_by NVARCHAR(255) NOT NULL,
        action_timestamp DATETIME2 DEFAULT GETDATE(),
        
        CONSTRAINT FK_workflow_log_treatment_plan 
        FOREIGN KEY (treatment_plan_id) REFERENCES treatment_plan(plan_id)
    );
    PRINT '✓ Created audit trail table';
END

-- Step 7: Update existing data
UPDATE treatment_plan 
SET treating_doctor_id = doctor_id 
WHERE treating_doctor_id IS NULL AND doctor_id IS NOT NULL;

UPDATE ts
SET doctor_id = tp.treating_doctor_id,
    patient_id = tp.patient_id
FROM treatment_schedule ts
INNER JOIN treatment_plan tp ON ts.plan_id = tp.plan_id
WHERE ts.doctor_id IS NULL OR ts.patient_id IS NULL;

PRINT '✓ Updated existing data';

-- Step 8: Create access control procedure
IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_check_treatment_plan_access')
    DROP PROCEDURE sp_check_treatment_plan_access;
GO

CREATE PROCEDURE sp_check_treatment_plan_access
    @plan_id UNIQUEIDENTIFIER,
    @user_id UNIQUEIDENTIFIER,
    @user_role NVARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @has_access BIT = 0;
    
    IF @user_role = 'CUSTOMER'
    BEGIN
        -- Patient can only view their own plans
        IF EXISTS (SELECT 1 FROM treatment_plan WHERE plan_id = @plan_id AND patient_id = @user_id)
            SET @has_access = 1;
    END
    ELSE IF @user_role = 'DOCTOR'
    BEGIN
        -- Doctor can only view plans for their patients
        IF EXISTS (SELECT 1 FROM treatment_plan WHERE plan_id = @plan_id AND treating_doctor_id = @user_id)
            SET @has_access = 1;
    END
    ELSE IF @user_role IN ('MANAGER', 'ADMIN')
    BEGIN
        -- Manager/Admin can view all plans
        IF EXISTS (SELECT 1 FROM treatment_plan WHERE plan_id = @plan_id)
            SET @has_access = 1;
    END
    
    SELECT @has_access as has_access;
END
GO

-- Step 9: Create validation procedure
IF EXISTS (SELECT * FROM sys.procedures WHERE name = 'sp_validate_treatment_plan_creation')
    DROP PROCEDURE sp_validate_treatment_plan_creation;
GO

CREATE PROCEDURE sp_validate_treatment_plan_creation
    @patient_id UNIQUEIDENTIFIER,
    @treating_doctor_id UNIQUEIDENTIFIER
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @is_valid BIT = 1;
    DECLARE @error_message NVARCHAR(500) = '';
    
    -- Check: Patient already has active plan?
    IF EXISTS (SELECT 1 FROM treatment_plan WHERE patient_id = @patient_id AND status = 'active')
    BEGIN
        SET @is_valid = 0;
        SET @error_message = 'Patient already has an active treatment plan';
    END
    
    SELECT @is_valid as is_valid, @error_message as error_message;
END
GO

-- Verification
PRINT '';
PRINT '=== VERIFICATION ===';

SELECT 'Enhanced Fields' as component, COUNT(*) as count
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'treatment_plan' 
AND COLUMN_NAME IN ('treating_doctor_id', 'is_locked', 'version_number');

SELECT 'Business Constraints' as component, COUNT(*) as count
FROM sys.indexes 
WHERE name = 'UK_one_active_plan_per_patient';

SELECT 'Stored Procedures' as component, COUNT(*) as count
FROM sys.procedures 
WHERE name IN ('sp_check_treatment_plan_access', 'sp_validate_treatment_plan_creation');

PRINT 'Migration completed successfully!';
PRINT 'Business rules implemented:';
PRINT '- 1 Doctor : N Patients';
PRINT '- 1 Patient : 1 Active Plan';
PRINT '- Access Control enforced';

GO 