USE [master]
GO
/****** Object:  Database [Infertility_Treatment]    Script Date: 7/6/2025 5:04:07 AM ******/
CREATE DATABASE [Infertility_Treatment]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Infertility_Treatment', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLSERVER\MSSQL\DATA\Infertility_Treatment.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Infertility_Treatment_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLSERVER\MSSQL\DATA\Infertility_Treatment_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [Infertility_Treatment] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Infertility_Treatment].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Infertility_Treatment] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET ARITHABORT OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Infertility_Treatment] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Infertility_Treatment] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET  ENABLE_BROKER 
GO
ALTER DATABASE [Infertility_Treatment] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Infertility_Treatment] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET RECOVERY FULL 
GO
ALTER DATABASE [Infertility_Treatment] SET  MULTI_USER 
GO
ALTER DATABASE [Infertility_Treatment] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Infertility_Treatment] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Infertility_Treatment] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Infertility_Treatment] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Infertility_Treatment] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Infertility_Treatment] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'Infertility_Treatment', N'ON'
GO
ALTER DATABASE [Infertility_Treatment] SET QUERY_STORE = OFF
GO
USE [Infertility_Treatment]
GO
/****** Object:  Table [dbo].[appointment]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[appointment](
	[appointment_id] [uniqueidentifier] NOT NULL,
	[request_id] [uniqueidentifier] NOT NULL,
	[doctor_id] [uniqueidentifier] NOT NULL,
	[customer_id] [uniqueidentifier] NOT NULL,
	[appointment_time] [datetime] NOT NULL,
	[room] [nvarchar](100) NULL,
	[check_in_status] [varchar](20) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[appointment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[appointmentProposal]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[appointmentProposal](
	[proposal_id] [uniqueidentifier] NOT NULL,
	[request_id] [uniqueidentifier] NOT NULL,
	[proposed_datetime] [datetime] NOT NULL,
	[room] [nvarchar](100) NULL,
	[expiration_time] [datetime] NULL,
	[status] [varchar](20) NOT NULL,
	[created_at] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[proposal_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[blog]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[blog](
	[blog_id] [uniqueidentifier] NOT NULL,
	[author_id] [uniqueidentifier] NOT NULL,
	[title] [nvarchar](255) NOT NULL,
	[content] [nvarchar](max) NULL,
	[cover_image] [varchar](255) NULL,
	[tags] [nvarchar](max) NULL,
	[status] [varchar](20) NOT NULL,
	[view_count] [int] NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[approved_at] [datetime2](6) NULL,
	[approved_by] [uniqueidentifier] NULL,
PRIMARY KEY CLUSTERED 
(
	[blog_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[blog_images]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[blog_images](
	[blog_id] [uniqueidentifier] NOT NULL,
	[image_path] [varchar](255) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[clinical_result]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[clinical_result](
	[result_id] [uniqueidentifier] NOT NULL,
	[result_type] [varchar](20) NOT NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[created_by] [varchar](255) NULL,
	[updated_by] [varchar](255) NULL,
	[blood_pressure_diastolic] [int] NULL,
	[blood_pressure_systolic] [int] NULL,
	[blood_type] [varchar](10) NULL,
	[bmi] [numeric](4, 2) NULL,
	[completion_date] [datetime2](6) NULL,
	[creatinine] [numeric](4, 2) NULL,
	[diagnosis] [varchar](500) NULL,
	[diagnosis_code] [varchar](20) NULL,
	[doctor_id] [uniqueidentifier] NULL,
	[endometrial_thickness] [numeric](4, 1) NULL,
	[examination_date] [datetime2](6) NULL,
	[follicle_count_left] [int] NULL,
	[follicle_count_right] [int] NULL,
	[glucose] [numeric](4, 1) NULL,
	[heart_rate] [int] NULL,
	[height] [numeric](5, 2) NULL,
	[hemoglobin] [numeric](4, 1) NULL,
	[infertility_duration_months] [int] NULL,
	[is_completed] [bit] NULL,
	[next_appointment_date] [datetime2](6) NULL,
	[ovary_size_left] [numeric](4, 1) NULL,
	[ovary_size_right] [numeric](4, 1) NULL,
	[patient_id] [uniqueidentifier] NULL,
	[platelet_count] [int] NULL,
	[previous_treatments] [nvarchar](max) NULL,
	[recommendations] [nvarchar](max) NULL,
	[severity_level] [varchar](20) NULL,
	[temperature] [numeric](4, 1) NULL,
	[treatment_priority] [varchar](20) NULL,
	[ultrasound_findings] [nvarchar](max) NULL,
	[weight] [numeric](5, 2) NULL,
	[white_blood_cell] [numeric](6, 2) NULL,
	[amh_level] [numeric](6, 2) NULL,
	[attached_file_url] [varchar](500) NULL,
	[estradiol_level] [numeric](8, 2) NULL,
	[fsh_level] [numeric](6, 2) NULL,
	[lh_level] [numeric](6, 2) NULL,
	[notes] [nvarchar](max) NULL,
	[prolactin_level] [numeric](6, 2) NULL,
	[symptoms] [nvarchar](max) NULL,
	[symptoms_detail] [nvarchar](max) NULL,
	[testosterone_level] [numeric](6, 2) NULL,
	[appointment_id] [uniqueidentifier] NOT NULL,
 CONSTRAINT [PK_clinical_result] PRIMARY KEY CLUSTERED 
(
	[result_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[comment]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[comment](
	[comment_id] [uniqueidentifier] NOT NULL,
	[blog_id] [uniqueidentifier] NOT NULL,
	[user_id] [uniqueidentifier] NOT NULL,
	[content] [nvarchar](max) NOT NULL,
	[parent_id] [uniqueidentifier] NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[is_visible] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[comment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[doctor_work_schedule]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[doctor_work_schedule](
	[schedule_id] [uniqueidentifier] NOT NULL,
	[doctor_id] [uniqueidentifier] NOT NULL,
	[day_of_week] [int] NOT NULL,
	[start_time] [time](7) NOT NULL,
	[end_time] [time](7) NOT NULL,
	[room] [nvarchar](100) NOT NULL,
	[effective_from] [date] NULL,
	[effective_to] [date] NULL,
PRIMARY KEY CLUSTERED 
(
	[schedule_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[doctorAssignment]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[doctorAssignment](
	[assignment_id] [uniqueidentifier] NOT NULL,
	[request_id] [uniqueidentifier] NOT NULL,
	[assigned_doctor_id] [uniqueidentifier] NOT NULL,
	[assigned_by] [uniqueidentifier] NULL,
	[previous_doctor_id] [uniqueidentifier] NULL,
	[reason] [nvarchar](max) NULL,
	[assigned_at] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[assignment_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[email_verification_token]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[email_verification_token](
	[id] [uniqueidentifier] NOT NULL,
	[user_id] [uniqueidentifier] NULL,
	[token] [varchar](255) NOT NULL,
	[expiry_date] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[notification]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[notification](
	[notification_id] [uniqueidentifier] NOT NULL,
	[user_id] [uniqueidentifier] NOT NULL,
	[schedule_id] [uniqueidentifier] NULL,
	[notification_type] [varchar](20) NOT NULL,
	[message] [nvarchar](max) NULL,
	[send_time] [datetime] NOT NULL,
	[status] [varchar](20) NOT NULL,
	[config_reminder_time] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[notification_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[password_reset_token]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[password_reset_token](
	[id] [uniqueidentifier] NOT NULL,
	[token] [nvarchar](255) NOT NULL,
	[user_id] [uniqueidentifier] NOT NULL,
	[expiry_date] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[profile]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[profile](
	[profile_id] [uniqueidentifier] NOT NULL,
	[user_id] [uniqueidentifier] NOT NULL,
	[specialty] [nvarchar](255) NULL,
	[qualification] [nvarchar](255) NULL,
	[experience_years] [int] NULL,
	[rating] [float] NULL,
	[case_count] [int] NULL,
	[notes] [nvarchar](max) NULL,
	[status] [varchar](20) NOT NULL,
	[marital_status] [nvarchar](100) NULL,
	[health_background] [nvarchar](max) NULL,
	[assigned_department] [nvarchar](100) NULL,
	[extra_permissions] [nvarchar](max) NULL,
	[work_schedule] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[profile_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[reminder_log]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[reminder_log](
	[reminder_id] [uniqueidentifier] NOT NULL,
	[appointment_id] [uniqueidentifier] NULL,
	[channel] [varchar](255) NULL,
	[reminder_time] [datetime2](6) NULL,
	[status] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[reminder_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[role]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[role](
	[role_id] [uniqueidentifier] NOT NULL,
	[user_id] [uniqueidentifier] NOT NULL,
	[role_type] [varchar](20) NOT NULL,
	[role_level] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[role_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[service]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[service](
	[service_id] [uniqueidentifier] NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[description] [nvarchar](max) NULL,
	[price] [numeric](38, 2) NULL,
	[estimated_duration] [int] NULL,
	[created_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[service_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[service_request]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[service_request](
	[request_id] [uniqueidentifier] NOT NULL,
	[customer_id] [uniqueidentifier] NOT NULL,
	[preferred_datetime] [datetime] NULL,
	[note] [nvarchar](max) NULL,
	[doctor_selection] [varchar](10) NULL,
	[preferred_doctor_id] [uniqueidentifier] NULL,
	[status] [varchar](50) NOT NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[service_id] [uniqueidentifier] NULL,
PRIMARY KEY CLUSTERED 
(
	[request_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_plan]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_plan](
	[plan_id] [uniqueidentifier] NOT NULL,
	[template_id] [uniqueidentifier] NOT NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[created_by] [varchar](255) NULL,
	[updated_by] [varchar](255) NULL,
	[contraindications] [nvarchar](max) NULL,
	[current_phase] [uniqueidentifier] NULL,
	[doctor_id] [uniqueidentifier] NULL,
	[end_date] [datetime2](6) NULL,
	[estimated_cost] [numeric](12, 2) NULL,
	[estimated_duration_days] [int] NULL,
	[medication_plan] [nvarchar](max) NULL,
	[monitoring_schedule] [nvarchar](max) NULL,
	[patient_id] [uniqueidentifier] NULL,
	[plan_description] [nvarchar](max) NULL,
	[plan_name] [varchar](255) NULL,
	[risk_factors] [nvarchar](max) NULL,
	[start_date] [datetime2](6) NULL,
	[status] [varchar](20) NULL,
	[success_probability] [numeric](4, 2) NULL,
	[treatment_steps] [nvarchar](max) NULL,
	[treatment_type] [varchar](20) NULL,
	[is_locked] [bit] NULL,
	[version_number] [int] NULL,
	[treatment_cycle] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[plan_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_plan_template]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_plan_template](
	[template_id] [uniqueidentifier] NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[treatment_type] [nvarchar](20) NULL,
	[plan_name] [nvarchar](255) NULL,
	[plan_description] [nvarchar](max) NULL,
	[estimated_duration_days] [int] NULL,
	[estimated_cost] [decimal](12, 2) NULL,
	[treatment_steps] [nvarchar](max) NULL,
	[medication_plan] [nvarchar](max) NULL,
	[monitoring_schedule] [nvarchar](max) NULL,
	[success_probability] [decimal](4, 2) NULL,
	[risk_factors] [nvarchar](max) NULL,
	[contraindications] [nvarchar](max) NULL,
	[treatment_cycle] [int] NULL,
	[created_by] [nvarchar](255) NULL,
	[is_active] [bit] NULL,
	[status] [nvarchar](20) NULL,
	[updated_by] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[template_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_phase]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_phase](
	[phase_id] [uniqueidentifier] NOT NULL,
	[service_id] [uniqueidentifier] NOT NULL,
	[phase_name] [nvarchar](255) NOT NULL,
	[phase_order] [int] NOT NULL,
	[description] [nvarchar](max) NULL,
	[expected_duration] [int] NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[created_by] [varchar](255) NULL,
	[updated_by] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[phase_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_phase_status]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_phase_status](
	[status_id] [uniqueidentifier] NOT NULL,
	[treatment_plan_id] [uniqueidentifier] NOT NULL,
	[phase_id] [uniqueidentifier] NOT NULL,
	[status] [varchar](20) NOT NULL,
	[start_date] [datetime] NULL,
	[end_date] [datetime] NULL,
	[notes] [nvarchar](max) NULL,
	[created_by] [varchar](255) NULL,
	[updated_by] [varchar](255) NULL,
	[created_at] [datetime2](6) NULL,
	[updated_at] [datetime2](6) NULL,
PRIMARY KEY CLUSTERED 
(
	[status_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_result]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_result](
	[result_id] [uniqueidentifier] NOT NULL,
	[schedule_step_id] [uniqueidentifier] NOT NULL,
	[summary] [nvarchar](max) NULL,
	[details] [nvarchar](max) NULL,
	[complication_note] [nvarchar](max) NULL,
	[file_url] [varchar](255) NULL,
	[status] [varchar](20) NOT NULL,
	[created_at] [datetime] NOT NULL,
	[created_by] [varchar](255) NULL,
	[updated_by] [varchar](255) NULL,
	[completion_date] [datetime2](6) NULL,
	[complication_severity] [varchar](20) NULL,
	[complications] [nvarchar](max) NULL,
	[doctor_id] [varchar](255) NULL,
	[eggs_retrieved] [int] NULL,
	[embryo_quality] [varchar](20) NULL,
	[embryos_formed] [int] NULL,
	[embryos_transferred] [int] NULL,
	[fertilized_eggs] [int] NULL,
	[hcg_level] [numeric](6, 2) NULL,
	[hcg_test_date] [datetime2](6) NULL,
	[insemination_technique] [varchar](50) NULL,
	[insemination_time] [datetime2](6) NULL,
	[is_completed] [bit] NULL,
	[mature_eggs] [int] NULL,
	[medication_given] [nvarchar](max) NULL,
	[notes] [nvarchar](max) NULL,
	[outcome] [varchar](50) NULL,
	[outcome_date] [datetime2](6) NULL,
	[patient_id] [uniqueidentifier] NULL,
	[phase_id] [uniqueidentifier] NULL,
	[pregnancy_test_result] [varchar](20) NULL,
	[review_date] [datetime2](6) NULL,
	[reviewed_by] [varchar](255) NULL,
	[side_effects] [nvarchar](max) NULL,
	[sperm_count] [int] NULL,
	[sperm_morphology] [numeric](4, 1) NULL,
	[sperm_motility] [numeric](4, 1) NULL,
	[success_rate] [numeric](4, 2) NULL,
	[transfer_date] [datetime2](6) NULL,
	[transfer_technique] [varchar](50) NULL,
	[treatment_date] [datetime2](6) NULL,
	[treatment_plan_id] [uniqueidentifier] NULL,
	[ultrasound_fetal_heartbeat] [bit] NULL,
	[ultrasound_gestational_sac] [bit] NULL,
	[updated_at] [datetime2](6) NULL,
PRIMARY KEY CLUSTERED 
(
	[result_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_schedule]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_schedule](
	[schedule_id] [uniqueidentifier] NOT NULL,
	[plan_id] [uniqueidentifier] NOT NULL,
	[scheduled_date] [datetime] NOT NULL,
	[treatment_type] [varchar](50) NOT NULL,
	[room_id] [varchar](255) NULL,
	[status] [varchar](20) NOT NULL,
	[notes] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[created_by] [varchar](255) NULL,
	[updated_by] [varchar](255) NULL,
	[doctor_id] [uniqueidentifier] NULL,
	[patient_id] [uniqueidentifier] NULL,
	[completed_at] [datetime2](6) NULL,
	[deadline] [datetime2](6) NULL,
	[grace_period_days] [int] NULL,
	[step_name] [varchar](255) NOT NULL,
	[step_number] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[schedule_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_workflow_log]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_workflow_log](
	[log_id] [uniqueidentifier] NOT NULL,
	[treatment_plan_id] [uniqueidentifier] NOT NULL,
	[action_type] [nvarchar](50) NOT NULL,
	[previous_status] [nvarchar](20) NULL,
	[new_status] [nvarchar](20) NULL,
	[performed_by] [nvarchar](255) NOT NULL,
	[action_timestamp] [datetime2](7) NULL,
PRIMARY KEY CLUSTERED 
(
	[log_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[user_id] [uniqueidentifier] NOT NULL,
	[full_name] [nvarchar](100) NOT NULL,
	[gender] [varchar](10) NOT NULL,
	[date_of_birth] [date] NOT NULL,
	[email] [varchar](100) NOT NULL,
	[phone] [varchar](20) NULL,
	[address] [nvarchar](255) NULL,
	[avatar_url] [varchar](255) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
	[password] [varchar](255) NOT NULL,
	[is_email_verified] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'6e71da71-78af-4294-9576-0873487bf740', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', 5, CAST(N'07:00:00' AS Time), CAST(N'18:00:00' AS Time), N'301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'bb9594f3-50e9-46d5-93e6-266e9f8ee186', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', 6, CAST(N'07:00:00' AS Time), CAST(N'11:00:00' AS Time), N'301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'bb9594f3-50e9-46d5-93e6-266e9f8ee187', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', 4, CAST(N'07:00:00' AS Time), CAST(N'18:00:00' AS Time), N'301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'bb9594f3-50e9-46d5-93e6-266e9f8ee189', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', 3, CAST(N'07:00:00' AS Time), CAST(N'18:00:00' AS Time), N'301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1f', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', 1, CAST(N'07:00:00' AS Time), CAST(N'18:00:00' AS Time), N'301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'2d7e15c6-e476-4a6b-a4df-65bd6815a4a3', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', 2, CAST(N'07:00:00' AS Time), CAST(N'18:00:00' AS Time), N'301', NULL, NULL)
GO
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions], [work_schedule]) VALUES (N'a4972c5c-77a8-41d6-a401-f3a2898aad2f', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', N'IUI', N'MD', 10, NULL, NULL, N'Siêu Cute', N'active', NULL, NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'b230fea0-ca22-4ff7-a969-6650ba134ec2', N'bcf1c8ad-7165-4075-89e6-fd04ea189502', N'ADMIN', 4)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'5f96b07b-5771-4387-90da-6d0d1575b80b', N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', N'DOCTOR', 2)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'd2ffa73a-2919-4fa5-bbcc-7eb2ba05503e', N'5f6db14e-02f7-469b-a82e-8a13dfe32141', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'f774d349-126f-4676-a7b0-9278f3b48b56', N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'f774d349-126f-4676-a7b0-9278f3b48c55', N'a38b2df1-49b6-4fdc-bc48-84809ca78f46', N'MANAGER', 3)
GO
INSERT [dbo].[service] ([service_id], [name], [description], [price], [estimated_duration], [created_at]) VALUES (N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1', N'IUI', N'Dịch vụ IUI dành cho cặp đôi vô sinh nhẹ', CAST(15000000.00 AS Numeric(38, 2)), 14, CAST(N'2025-06-16T17:54:03.603' AS DateTime))
INSERT [dbo].[service] ([service_id], [name], [description], [price], [estimated_duration], [created_at]) VALUES (N'735d0713-6e9b-4ced-9621-a30190faebf3', N'IVF', N'Dịch vụ hỗ trợ sinh sản IVF', CAST(50000000.00 AS Numeric(38, 2)), 30, CAST(N'2025-06-16T17:54:03.603' AS DateTime))
GO
INSERT [dbo].[treatment_plan_template] ([template_id], [name], [description], [created_at], [updated_at], [treatment_type], [plan_name], [plan_description], [estimated_duration_days], [estimated_cost], [treatment_steps], [medication_plan], [monitoring_schedule], [success_probability], [risk_factors], [contraindications], [treatment_cycle], [created_by], [is_active], [status], [updated_by]) VALUES (N'a1a1a1a1-b2b2-c3c3-d4d4-e5e5e5e5e5e5', N'Template IUI Standard', N'Phác đồ thụ tinh nhân tạo trong tử cung chuẩn', CAST(N'2025-07-03T03:23:15.997' AS DateTime), CAST(N'2025-07-03T03:23:15.997' AS DateTime), N'IUI', N'Kế hoach IUI - Thụ tinh tử cung', N'Quy trình IUI 4 giai đoạn cho các trường hợp rối lọan rụng trứng, tinh trùng yếu nhỏ', 21, CAST(15000000.00 AS Decimal(12, 2)), N'[
        {
            "step": 1,
            "name": "Chuẩn bị",
            "duration": "3-5 ngày", 
            "description": "Khám sức khỏe, xét nghiệm máu, siêu âm đánh giá",
            "activities": ["Khám tổng quát", "Xét nghiệm hormone", "Siêu âm buồng trứng", "Tư vấn quy trình"]
        },
        {
            "step": 2,
            "name": "Kích thích rụng trứng",
            "duration": "10-14 ngày",
            "description": "Sử dụng thuốc kích thích buồng trứng và theo dõi",
            "activities": ["Tiêm FSH hàng ngày", "Siêu âm theo dõi nang trứng", "Xét nghiệm E2", "Tiêm HCG trigger"]
        },
        {
            "step": 3,
            "name": "Thụ tinh nhân tạo",
            "duration": "1 ngày",
            "description": "Lấy tinh trùng, xử lý và bơm vào tử cung",
            "activities": ["Lấy mẫu tinh", "Xử lý tinh trùng", "Thủ thuật IUI", "Nghỉ ngơi 30 phút"]
        },
        {
            "step": 4,
            "name": "Theo dõi kết quả",
            "duration": "14 ngày",
            "description": "Hỗ trợ hoàng thể và kiểm tra thai",
            "activities": ["Tiêm Progesterone", "Theo dõi triệu chứng", "Xét nghiệm beta-HCG", "Siêu âm xác nhận"]
        }
    ]', N'[
        {
            "phase": "Chuẩn bị",
            "medications": [
                {"name": "Acid Folic", "dosage": "5mg/ngày", "frequency": "1 lần/ngày", "duration": "từ trước điều trị"}
            ]
        },
        {
            "phase": "Kích thích",
            "medications": [
                {"name": "Gonal-F", "dosage": "75-150 IU/ngày", "frequency": "1 lần/ngày tối", "duration": "10-12 ngày"},
                {"name": "Pregnyl", "dosage": "5000-10000 IU", "frequency": "1 lần duy nhất", "duration": "trigger"}
            ]
        },
        {
            "phase": "Sau IUI", 
            "medications": [
                {"name": "Duphaston", "dosage": "10mg", "frequency": "2 lần/ngày", "duration": "14 ngày"}
            ]
        }
    ]', N'[
        {"day": 1, "activity": "Khám ban đầu", "type": "consultation"},
        {"day": 3, "activity": "Bắt đầu tiêm FSH", "type": "medication"},
        {"day": 8, "activity": "Siêu âm kiểm tra nang trứng", "type": "ultrasound"},
        {"day": 12, "activity": "Tiêm HCG trigger", "type": "trigger"},
        {"day": 14, "activity": "Thủ thuật IUI", "type": "procedure"},
        {"day": 28, "activity": "Xét nghiệm thai", "type": "test"}
    ]', CAST(60.00 AS Decimal(4, 2)), N'Nguy cơ đa thai thấp, OHSS nhẹ', N'Tắc ống dẫn trứng hoàn toàn, nội mạc tử cung mỏng <7mm', 1, N'system', 1, N'active', N'system')
INSERT [dbo].[treatment_plan_template] ([template_id], [name], [description], [created_at], [updated_at], [treatment_type], [plan_name], [plan_description], [estimated_duration_days], [estimated_cost], [treatment_steps], [medication_plan], [monitoring_schedule], [success_probability], [risk_factors], [contraindications], [treatment_cycle], [created_by], [is_active], [status], [updated_by]) VALUES (N'b2b2b2b2-c3c3-d4d4-e5e5-f6f6f6f6f6f6', N'Template IVF Standard', N'Phác đồ thụ tinh trong ống nghiệm chuẩn', CAST(N'2025-07-03T03:23:15.997' AS DateTime), CAST(N'2025-07-03T03:23:15.997' AS DateTime), N'IVF', N'Kế hoạch IVF - Thụ tinh ống nghiệm', N'Quy trình IVF 5 giai đoạn cho các trường hợp tắc ống dẫn trứng, vô sinh không rõ nguyên nhân', 35, CAST(80000000.00 AS Decimal(12, 2)), N'[
        {
            "step": 1,
            "name": "Chuẩn bị",
            "duration": "5-7 ngày",
            "description": "Khám toàn diện, xét nghiệm đầy đủ, tư vấn chi tiết",
            "activities": ["Khám sức khỏe tổng quát", "Xét nghiệm máu đầy đủ", "Siêu âm 3D", "Tư vấn kỹ thuật IVF", "Hướng dẫn tiêm thuốc"]
        },
        {
            "step": 2,
            "name": "Kích thích buồng trứng",
            "duration": "10-14 ngày",
            "description": "Kích thích đa nang trứng với liều thuốc cao",
            "activities": ["Tiêm FSH/LH", "Siêu âm theo dõi hàng ngày", "Xét nghiệm E2, LH", "Tiêm GnRH antagonist", "Tiêm HCG trigger"]
        },
        {
            "step": 3,
            "name": "Lấy trứng",
            "duration": "1 ngày", 
            "description": "Thủ thuật lấy trứng qua âm đạo có gây tê",
            "activities": ["Gây tê tại chỗ", "Thủ thuật lấy trứng", "Theo dõi sau thủ thuật", "Kháng sinh dự phòng"]
        },
        {
            "step": 4,
            "name": "Nuôi cấy phôi",
            "duration": "3-5 ngày",
            "description": "Thụ tinh IVF/ICSI và nuôi cấy phôi đến giai đoạn blastocyst",
            "activities": ["Thụ tinh IVF/ICSI", "Nuôi cấy ngày 1", "Đánh giá phôi ngày 3", "Nuôi đến blastocyst", "Đông lạnh phôi thừa"]
        },
        {
            "step": 5,
            "name": "Chuyển phôi & theo dõi",
            "duration": "16+ ngày",
            "description": "Chuyển phôi vào tử cung và theo dõi kết quả",
            "activities": ["Chuẩn bị nội mạc", "Chuyển phôi", "Hỗ trợ hoàng thể", "Xét nghiệm thai", "Siêu âm xác nhận"]
        }
    ]', N'[
        {
            "phase": "Chuẩn bị",
            "medications": [
                {"name": "Acid Folic", "dosage": "5mg/ngày", "frequency": "1 lần/ngày", "duration": "từ 1 tháng trước"},
                {"name": "Thuốc tránh thai", "dosage": "theo chỉ định", "frequency": "1 lần/ngày", "duration": "21 ngày"}
            ]
        },
        {
            "phase": "Kích thích",
            "medications": [
                {"name": "Gonal-F", "dosage": "225-300 IU/ngày", "frequency": "1 lần/ngày tối", "duration": "10-14 ngày"},
                {"name": "Menopur", "dosage": "150 IU/ngày", "frequency": "1 lần/ngày", "duration": "kết hợp với FSH"},
                {"name": "Cetrotide", "dosage": "0.25mg/ngày", "frequency": "1 lần/ngày sáng", "duration": "5-7 ngày"},
                {"name": "Pregnyl", "dosage": "10000 IU", "frequency": "1 lần duy nhất", "duration": "trigger"}
            ]
        },
        {
            "phase": "Sau chuyển phôi",
            "medications": [
                {"name": "Duphaston", "dosage": "10mg", "frequency": "2 lần/ngày", "duration": "12 tuần"},
                {"name": "Utrogestan", "dosage": "200mg", "frequency": "3 lần/ngày", "duration": "đặt âm đạo"},
                {"name": "Estrofem", "dosage": "2mg", "frequency": "2 lần/ngày", "duration": "12 tuần"}
            ]
        }
    ]', N'[
        {"day": 1, "activity": "Khám và tư vấn ban đầu", "type": "consultation"},
        {"day": 2, "activity": "Bắt đầu tiêm FSH", "type": "medication"},
        {"day": 6, "activity": "Siêu âm + xét nghiệm E2", "type": "monitoring"},
        {"day": 8, "activity": "Siêu âm + xét nghiệm E2", "type": "monitoring"},
        {"day": 10, "activity": "Siêu âm + E2 + bắt đầu Cetrotide", "type": "monitoring"},
        {"day": 12, "activity": "Siêu âm + E2 + LH", "type": "monitoring"},
        {"day": 14, "activity": "Tiêm HCG trigger", "type": "trigger"},
        {"day": 16, "activity": "Lấy trứng", "type": "procedure"},
        {"day": 19, "activity": "Đánh giá phôi ngày 3", "type": "embryo_check"},
        {"day": 21, "activity": "Chuyển phôi blastocyst", "type": "transfer"},
        {"day": 35, "activity": "Xét nghiệm beta-HCG", "type": "pregnancy_test"},
        {"day": 42, "activity": "Siêu âm xác nhận túi thai", "type": "ultrasound"}
    ]', CAST(65.00 AS Decimal(4, 2)), N'Nguy cơ OHSS trung bình-nặng, đa thai, chảy máu sau lấy trứng', N'Ung thư phụ khoa đang hoạt động, rối loạn đông máu nặng', 1, N'system', 1, N'active', N'system')
GO
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'26cb203c-ede4-4ae7-a6c1-2edfe9cf8f1a', N'Dr. Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2025-07-02' AS Date), N'anhson20027774@gmail.com', N'0937634679', N'Đồng Nai', N'string', CAST(N'2025-07-02T09:16:03.900' AS DateTime), NULL, N'$2a$10$ctVd9dhyqQY3AwSvN01qK.xcNcIUJk279AFZwOzZmrFIZOpF77o5.', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'a38b2df1-49b6-4fdc-bc48-84809ca78f46', N'Manager', N'MALE', CAST(N'1995-08-15' AS Date), N'manager@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:10:46.530' AS DateTime), NULL, N'$2a$10$WR9iqOYwViy8aeLUHYB1f.4r1qtburQOU/OwFPAxCnKTBqX45xCYW', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'5f6db14e-02f7-469b-a82e-8a13dfe32141', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tukhuyet2002@gmail.com', N'0981811111', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-24T09:29:16.397' AS DateTime), CAST(N'2025-06-24T09:30:32.967' AS DateTime), N'$2a$10$bvU6KGpPaGhIBrvRXyk2IOdH6D8aSVwuFsUw8GUCKH6sqxdXorjKm', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', N'Customer', N'MALE', CAST(N'1995-08-15' AS Date), N'customer@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:09:20.000' AS DateTime), NULL, N'$2a$10$zAju5jOPqI6Y9yoyF4aXUuldacPu0bPHBR9O4yzA6.ccHSIiYPxoS', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'bcf1c8ad-7165-4075-89e6-fd04ea189502', N'Admin', N'MALE', CAST(N'1995-08-15' AS Date), N'admin@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:09:55.797' AS DateTime), NULL, N'$2a$10$AGuYff.tFD1Xei0tYvqLGuLLNrTxOZU5GKwxesDqrXNu8vk518ttq', 1)
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__email_ve__CA90DA7AF5E8D951]    Script Date: 7/6/2025 5:04:07 AM ******/
ALTER TABLE [dbo].[email_verification_token] ADD UNIQUE NONCLUSTERED 
(
	[token] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ__password__B9BE370E4513655A]    Script Date: 7/6/2025 5:04:07 AM ******/
ALTER TABLE [dbo].[password_reset_token] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UK_one_active_plan_per_patient]    Script Date: 7/6/2025 5:04:07 AM ******/
CREATE UNIQUE NONCLUSTERED INDEX [UK_one_active_plan_per_patient] ON [dbo].[treatment_plan]
(
	[patient_id] ASC
)
WHERE ([status]='active')
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E61641E760430]    Script Date: 7/6/2025 5:04:07 AM ******/
ALTER TABLE [dbo].[users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[appointment] ADD  DEFAULT (newid()) FOR [appointment_id]
GO
ALTER TABLE [dbo].[appointment] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[appointmentProposal] ADD  DEFAULT (newid()) FOR [proposal_id]
GO
ALTER TABLE [dbo].[appointmentProposal] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[blog] ADD  DEFAULT (newid()) FOR [blog_id]
GO
ALTER TABLE [dbo].[blog] ADD  DEFAULT ((0)) FOR [view_count]
GO
ALTER TABLE [dbo].[blog] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[clinical_result] ADD  DEFAULT (newid()) FOR [result_id]
GO
ALTER TABLE [dbo].[clinical_result] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[comment] ADD  DEFAULT (newid()) FOR [comment_id]
GO
ALTER TABLE [dbo].[comment] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[comment] ADD  DEFAULT ((1)) FOR [is_visible]
GO
ALTER TABLE [dbo].[doctor_work_schedule] ADD  DEFAULT (newid()) FOR [schedule_id]
GO
ALTER TABLE [dbo].[doctorAssignment] ADD  DEFAULT (newid()) FOR [assignment_id]
GO
ALTER TABLE [dbo].[doctorAssignment] ADD  DEFAULT (getdate()) FOR [assigned_at]
GO
ALTER TABLE [dbo].[notification] ADD  DEFAULT (newid()) FOR [notification_id]
GO
ALTER TABLE [dbo].[profile] ADD  DEFAULT (newid()) FOR [profile_id]
GO
ALTER TABLE [dbo].[service] ADD  DEFAULT (newid()) FOR [service_id]
GO
ALTER TABLE [dbo].[service] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[service_request] ADD  DEFAULT (newid()) FOR [request_id]
GO
ALTER TABLE [dbo].[service_request] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_plan] ADD  DEFAULT (newid()) FOR [plan_id]
GO
ALTER TABLE [dbo].[treatment_plan] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_plan] ADD  DEFAULT ((0)) FOR [is_locked]
GO
ALTER TABLE [dbo].[treatment_plan] ADD  DEFAULT ((1)) FOR [version_number]
GO
ALTER TABLE [dbo].[treatment_plan_template] ADD  DEFAULT (newid()) FOR [template_id]
GO
ALTER TABLE [dbo].[treatment_plan_template] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_plan_template] ADD  DEFAULT ((1)) FOR [is_active]
GO
ALTER TABLE [dbo].[treatment_plan_template] ADD  DEFAULT ('draft') FOR [status]
GO
ALTER TABLE [dbo].[treatment_phase] ADD  DEFAULT (newid()) FOR [phase_id]
GO
ALTER TABLE [dbo].[treatment_phase] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_phase_status] ADD  DEFAULT (newid()) FOR [status_id]
GO
ALTER TABLE [dbo].[treatment_result] ADD  DEFAULT (newid()) FOR [result_id]
GO
ALTER TABLE [dbo].[treatment_result] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_schedule] ADD  DEFAULT (newid()) FOR [schedule_id]
GO
ALTER TABLE [dbo].[treatment_schedule] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_workflow_log] ADD  DEFAULT (newid()) FOR [log_id]
GO
ALTER TABLE [dbo].[treatment_workflow_log] ADD  DEFAULT (getdate()) FOR [action_timestamp]
GO
ALTER TABLE [dbo].[users] ADD  DEFAULT (newid()) FOR [user_id]
GO
ALTER TABLE [dbo].[users] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[users] ADD  DEFAULT ((0)) FOR [is_email_verified]
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD FOREIGN KEY([customer_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD FOREIGN KEY([doctor_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD FOREIGN KEY([request_id])
REFERENCES [dbo].[service_request] ([request_id])
GO
ALTER TABLE [dbo].[appointmentProposal]  WITH CHECK ADD FOREIGN KEY([request_id])
REFERENCES [dbo].[service_request] ([request_id])
GO
ALTER TABLE [dbo].[blog]  WITH CHECK ADD FOREIGN KEY([author_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[blog_images]  WITH CHECK ADD  CONSTRAINT [FKick7fmlc9g77g6yxscfssqqwu] FOREIGN KEY([blog_id])
REFERENCES [dbo].[blog] ([blog_id])
GO
ALTER TABLE [dbo].[blog_images] CHECK CONSTRAINT [FKick7fmlc9g77g6yxscfssqqwu]
GO
ALTER TABLE [dbo].[comment]  WITH CHECK ADD FOREIGN KEY([blog_id])
REFERENCES [dbo].[blog] ([blog_id])
GO
ALTER TABLE [dbo].[comment]  WITH CHECK ADD FOREIGN KEY([parent_id])
REFERENCES [dbo].[comment] ([comment_id])
GO
ALTER TABLE [dbo].[comment]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[doctor_work_schedule]  WITH CHECK ADD  CONSTRAINT [FK_Doctor_Schedule_User] FOREIGN KEY([doctor_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[doctor_work_schedule] CHECK CONSTRAINT [FK_Doctor_Schedule_User]
GO
ALTER TABLE [dbo].[doctorAssignment]  WITH CHECK ADD FOREIGN KEY([assigned_by])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[doctorAssignment]  WITH CHECK ADD FOREIGN KEY([request_id])
REFERENCES [dbo].[service_request] ([request_id])
GO
ALTER TABLE [dbo].[email_verification_token]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[notification]  WITH CHECK ADD FOREIGN KEY([schedule_id])
REFERENCES [dbo].[treatment_schedule] ([schedule_id])
GO
ALTER TABLE [dbo].[notification]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[password_reset_token]  WITH CHECK ADD  CONSTRAINT [fk_password_reset_user] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[password_reset_token] CHECK CONSTRAINT [fk_password_reset_user]
GO
ALTER TABLE [dbo].[profile]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[role]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[service_request]  WITH CHECK ADD FOREIGN KEY([customer_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[service_request]  WITH CHECK ADD  CONSTRAINT [FK_ServiceRequest_Service] FOREIGN KEY([service_id])
REFERENCES [dbo].[service] ([service_id])
GO
ALTER TABLE [dbo].[service_request] CHECK CONSTRAINT [FK_ServiceRequest_Service]
GO
ALTER TABLE [dbo].[treatment_plan]  WITH CHECK ADD FOREIGN KEY([template_id])
REFERENCES [dbo].[treatment_plan_template] ([template_id])
GO
ALTER TABLE [dbo].[treatment_phase_status]  WITH CHECK ADD FOREIGN KEY([phase_id])
REFERENCES [dbo].[treatment_phase] ([phase_id])
GO
ALTER TABLE [dbo].[treatment_phase_status]  WITH CHECK ADD FOREIGN KEY([treatment_plan_id])
REFERENCES [dbo].[treatment_plan] ([plan_id])
GO
ALTER TABLE [dbo].[treatment_result]  WITH CHECK ADD FOREIGN KEY([schedule_step_id])
REFERENCES [dbo].[treatment_schedule] ([schedule_id])
GO
ALTER TABLE [dbo].[treatment_schedule]  WITH CHECK ADD FOREIGN KEY([plan_id])
REFERENCES [dbo].[treatment_plan] ([plan_id])
GO
ALTER TABLE [dbo].[treatment_workflow_log]  WITH CHECK ADD  CONSTRAINT [FK_workflow_log_treatment_plan] FOREIGN KEY([treatment_plan_id])
REFERENCES [dbo].[treatment_plan] ([plan_id])
GO
ALTER TABLE [dbo].[treatment_workflow_log] CHECK CONSTRAINT [FK_workflow_log_treatment_plan]
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD CHECK  (([check_in_status]='Missed' OR [check_in_status]='CheckedIn' OR [check_in_status]='Pending'))
GO
ALTER TABLE [dbo].[appointmentProposal]  WITH CHECK ADD CHECK  (([status]='Expired' OR [status]='Rejected' OR [status]='Confirmed' OR [status]='Pending'))
GO
ALTER TABLE [dbo].[blog]  WITH CHECK ADD  CONSTRAINT [CK__blog__status__04E4BC85] CHECK  (([status]='archived' OR [status]='published' OR [status]='pending' OR [status]='draft'))
GO
ALTER TABLE [dbo].[blog] CHECK CONSTRAINT [CK__blog__status__04E4BC85]
GO
ALTER TABLE [dbo].[clinical_result]  WITH CHECK ADD CHECK  (([result_type]='Other' OR [result_type]='Text' OR [result_type]='Image' OR [result_type]='Lab'))
GO
ALTER TABLE [dbo].[notification]  WITH CHECK ADD CHECK  (([notification_type]='App' OR [notification_type]='Email'))
GO
ALTER TABLE [dbo].[notification]  WITH CHECK ADD CHECK  (([status]='Pending' OR [status]='Failed' OR [status]='Sent'))
GO
ALTER TABLE [dbo].[profile]  WITH CHECK ADD CHECK  (([status]='on_leave' OR [status]='inactive' OR [status]='active'))
GO
ALTER TABLE [dbo].[role]  WITH CHECK ADD CHECK  (([role_type]='Admin' OR [role_type]='Manager' OR [role_type]='Doctor' OR [role_type]='Customer'))
GO
ALTER TABLE [dbo].[service_request]  WITH CHECK ADD CHECK  (([doctor_selection]='Auto' OR [doctor_selection]='Manual'))
GO
ALTER TABLE [dbo].[service_request]  WITH CHECK ADD CHECK  (([status]='Cancelled' OR [status]='MissedAppointment' OR [status]='UnconfirmedExpired' OR [status]='Re-ProposeSchedule' OR [status]='Scheduled' OR [status]='PendingScheduleProposal' OR [status]='PendingAssignment'))
GO
ALTER TABLE [dbo].[treatment_phase_status]  WITH CHECK ADD CHECK  (([status]='Cancelled' OR [status]='Completed' OR [status]='In Progress' OR [status]='Pending'))
GO
ALTER TABLE [dbo].[treatment_result]  WITH CHECK ADD CHECK  (([status]='pending' OR [status]='failed' OR [status]='success'))
GO
ALTER TABLE [dbo].[treatment_schedule]  WITH CHECK ADD CHECK  (([status]='Cancelled' OR [status]='Completed' OR [status]='Scheduled'))
GO
ALTER TABLE [dbo].[treatment_schedule]  WITH CHECK ADD CHECK  (([treatment_type]='Lab Test' OR [treatment_type]='Ultrasound' OR [treatment_type]='IVF' OR [treatment_type]='IUI' OR [treatment_type]='Injection'))
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD CHECK  (([gender]='OTHER' OR [gender]='FEMALE' OR [gender]='MALE'))
GO
/****** Object:  StoredProcedure [dbo].[sp_check_treatment_plan_access]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_check_treatment_plan_access]
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
/****** Object:  StoredProcedure [dbo].[sp_validate_treatment_plan_creation]    Script Date: 7/6/2025 5:04:07 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_validate_treatment_plan_creation]
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
USE [master]
GO
ALTER DATABASE [Infertility_Treatment] SET  READ_WRITE 
GO
