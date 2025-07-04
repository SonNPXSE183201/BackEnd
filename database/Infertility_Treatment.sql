USE [master]
GO
/****** Object:  Database [Infertility_Treatment]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[appointment]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[appointmentProposal]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[blog]    Script Date: 6/27/2025 3:55:18 PM ******/
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
PRIMARY KEY CLUSTERED 
(
	[blog_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[clinical_Result]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[clinical_Result](
	[result_id] [uniqueidentifier] NOT NULL,
	[visit_id] [uniqueidentifier] NOT NULL,
	[result_type] [varchar](20) NOT NULL,
	[content] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[result_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[comment]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[doctor_work_schedule]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[doctorAssignment]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[email_verification_token]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[notification]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[password_reset_token]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[patient_Visit]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[patient_Visit](
	[visit_id] [uniqueidentifier] NOT NULL,
	[user_id] [uniqueidentifier] NOT NULL,
	[doctor_id] [uniqueidentifier] NOT NULL,
	[visit_date] [datetime] NOT NULL,
	[symptoms] [nvarchar](max) NULL,
	[physical_findings] [nvarchar](max) NULL,
	[diagnosis] [nvarchar](max) NULL,
	[notes] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[visit_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[profile]    Script Date: 6/27/2025 3:55:18 PM ******/
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
	[work_schedule] [nvarchar](max) NULL,
	[rating] [float] NULL,
	[case_count] [int] NULL,
	[notes] [nvarchar](max) NULL,
	[status] [varchar](20) NOT NULL,
	[marital_status] [nvarchar](100) NULL,
	[health_background] [nvarchar](max) NULL,
	[assigned_department] [nvarchar](100) NULL,
	[extra_permissions] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[profile_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[reminder_log]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[role]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[service]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[service_request]    Script Date: 6/27/2025 3:55:18 PM ******/
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
/****** Object:  Table [dbo].[treatment_Phase]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_Phase](
	[phase_id] [uniqueidentifier] NOT NULL,
	[service_id] [uniqueidentifier] NOT NULL,
	[phase_name] [nvarchar](255) NOT NULL,
	[phase_order] [int] NOT NULL,
	[description] [nvarchar](max) NULL,
	[expected_duration] [int] NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[phase_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_Phase_Status]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_Phase_Status](
	[status_id] [uniqueidentifier] NOT NULL,
	[treatment_plan_id] [uniqueidentifier] NOT NULL,
	[phase_id] [uniqueidentifier] NOT NULL,
	[status] [varchar](20) NOT NULL,
	[start_date] [datetime] NULL,
	[end_date] [datetime] NULL,
	[notes] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[status_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_Plan]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_Plan](
	[plan_id] [uniqueidentifier] NOT NULL,
	[visit_id] [uniqueidentifier] NOT NULL,
	[template_id] [uniqueidentifier] NOT NULL,
	[customized_steps] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[plan_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_Plan_Template]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_Plan_Template](
	[template_id] [uniqueidentifier] NOT NULL,
	[name] [nvarchar](255) NOT NULL,
	[description] [nvarchar](max) NULL,
	[steps] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[template_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_Result]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_Result](
	[result_id] [uniqueidentifier] NOT NULL,
	[schedule_step_id] [uniqueidentifier] NOT NULL,
	[summary] [nvarchar](max) NULL,
	[details] [nvarchar](max) NULL,
	[complication_note] [nvarchar](max) NULL,
	[file_url] [varchar](255) NULL,
	[status] [varchar](20) NOT NULL,
	[created_at] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[result_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[treatment_Schedule]    Script Date: 6/27/2025 3:55:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[treatment_Schedule](
	[schedule_id] [uniqueidentifier] NOT NULL,
	[plan_id] [uniqueidentifier] NOT NULL,
	[scheduled_date] [datetime] NOT NULL,
	[treatment_type] [varchar](50) NOT NULL,
	[room_id] [uniqueidentifier] NULL,
	[status] [varchar](20) NOT NULL,
	[notes] [nvarchar](max) NULL,
	[created_at] [datetime] NOT NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[schedule_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 6/27/2025 3:55:18 PM ******/
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
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'2cb18042-7c9f-4600-afa4-042fc9f247f3', N'15b7917c-0b7f-4d81-9409-153adfe7de01', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T08:00:00.000' AS DateTime), N'Phòng 302', N'Pending', CAST(N'2025-06-25T10:42:45.417' AS DateTime), CAST(N'2025-06-25T10:42:45.407' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'06318be0-1e04-4c13-b6d4-16e6b0b8d8bc', N'19d5c729-c558-4bcb-a68f-475051c1be5a', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-26T08:00:00.000' AS DateTime), N'Phòng 101', N'Pending', CAST(N'2025-06-25T14:00:05.527' AS DateTime), CAST(N'2025-06-25T14:00:05.517' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'f0928e31-01e2-4c4e-ae22-2b4b72d9aeff', N'b53f5f73-25b8-4e08-b8f7-2c083aaa80f2', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', CAST(N'2025-07-05T09:00:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-26T23:18:58.557' AS DateTime), CAST(N'2025-06-26T23:18:58.557' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'5ea5b4c5-abe5-4b93-b595-3a01fe6be168', N'34d17ff6-8d31-41aa-b259-f353770f44a2', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'c43077a6-bd00-4d45-a343-9471d351d611', CAST(N'2025-06-28T13:00:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-27T11:02:01.350' AS DateTime), CAST(N'2025-06-27T11:02:01.350' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'fafe9e52-557c-4c95-a7ae-3c059ce97674', N'63cf143d-ba50-48ba-b87e-e5a86c9837aa', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T09:00:00.000' AS DateTime), N'Phòng chờ', N'Pending', CAST(N'2025-06-25T11:35:32.057' AS DateTime), CAST(N'2025-06-25T11:35:32.050' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'c0e46063-b51d-406a-93f5-52369417962d', N'a0cca9b5-919c-43d1-aee6-aac2bc68edaa', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-28T08:00:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-25T14:22:53.173' AS DateTime), CAST(N'2025-06-25T14:22:53.157' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'96246dba-1c0a-417c-9838-56e5e03c5e07', N'0c925445-e15c-4e2a-b6e3-0dcb30933da6', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-26T09:00:00.000' AS DateTime), N'Phòng chờ', N'Pending', CAST(N'2025-06-25T14:05:16.800' AS DateTime), CAST(N'2025-06-25T14:05:16.793' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'0259095a-6db1-484e-a0e5-56f13153aa91', N'38e10781-b1a0-480a-bfdf-1792834a37e3', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-15T09:00:00.000' AS DateTime), N'Phòng 101', N'Pending', CAST(N'2025-06-25T16:56:09.377' AS DateTime), CAST(N'2025-06-25T16:56:09.377' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'c17261b5-c3db-4917-85cb-5951916ea58d', N'de0f1a7b-c268-4134-993e-d721c531ab29', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-06-27T12:30:00.000' AS DateTime), N'Phòng 302', N'Pending', CAST(N'2025-06-25T16:31:00.423' AS DateTime), CAST(N'2025-06-25T16:31:00.423' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'9cc53887-aa0b-46c5-b201-686d51e62ec5', N'50350dd1-bed9-437b-be5a-5ea8b5e52979', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'c43077a6-bd00-4d45-a343-9471d351d611', CAST(N'2025-06-28T10:00:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-27T11:11:29.577' AS DateTime), CAST(N'2025-06-27T11:11:29.570' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'ebf91e49-5a6c-4bb9-a5c9-856a0bf1604b', N'4a9db849-db38-49f2-b7e0-0151933882ee', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-15T09:00:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-26T23:49:55.710' AS DateTime), CAST(N'2025-06-26T23:49:55.710' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'5f1476da-9b0e-48e9-bbaf-a1da8f4988db', N'8365b410-bdfe-4948-999b-95396a73d3f8', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-10T09:00:00.000' AS DateTime), N'Phòng 101', N'Pending', CAST(N'2025-06-25T16:33:03.153' AS DateTime), CAST(N'2025-06-25T16:33:03.147' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'170a558e-7a90-437e-b1ee-a2b8099d349c', N'7d6083b2-3975-41a9-a95b-b837f6285572', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-09T10:00:00.000' AS DateTime), N'Phòng 101', N'Pending', CAST(N'2025-06-25T16:55:02.003' AS DateTime), CAST(N'2025-06-25T16:55:02.003' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'c3d81e70-531f-47a6-ba81-a61f4dce9aa5', N'4ba93858-fa6d-48b4-9cd3-d78a29a47e1b', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-06-28T12:57:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-27T10:42:07.410' AS DateTime), CAST(N'2025-06-27T10:42:07.410' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'2911d2ac-e9da-4b99-b559-a85a89020904', N'b6716ba2-8bb5-4624-b868-be00e60de408', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-09T11:00:00.000' AS DateTime), N'Phòng 301', N'Pending', CAST(N'2025-06-25T16:57:48.113' AS DateTime), CAST(N'2025-06-25T16:57:48.100' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'1b8bd057-cfa2-4ce5-a1df-b8bd99fb4807', N'0a3a50d2-112a-4e51-b833-48dee378839d', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-29T08:00:00.000' AS DateTime), N'Phòng chờ', N'Pending', CAST(N'2025-06-25T10:55:44.617' AS DateTime), CAST(N'2025-06-25T10:55:44.603' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'fc2345fe-ac59-43fa-b841-bd2e2789a070', N'3ec0436a-ef94-4a18-af5a-e6b990196d9c', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T11:00:00.000' AS DateTime), N'Phòng 302', N'Pending', CAST(N'2025-06-25T14:27:17.593' AS DateTime), CAST(N'2025-06-25T14:27:17.593' AS DateTime))
INSERT [dbo].[appointment] ([appointment_id], [request_id], [doctor_id], [customer_id], [appointment_time], [room], [check_in_status], [created_at], [updated_at]) VALUES (N'4d794470-deaf-4282-94d1-f2035ebc5f11', N'5a152d35-d2fc-4be0-840a-95df826a035d', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T10:00:00.000' AS DateTime), N'Phòng chờ', N'Pending', CAST(N'2025-06-25T14:26:15.350' AS DateTime), CAST(N'2025-06-25T14:26:15.343' AS DateTime))
GO
INSERT [dbo].[blog] ([blog_id], [author_id], [title], [content], [cover_image], [tags], [status], [view_count], [created_at], [updated_at]) VALUES (N'468f5c65-9b7c-4147-833d-381b780fd920', N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', N'string', N'string', N'string', N'string', N'draft', 0, CAST(N'2025-06-23T19:18:26.607' AS DateTime), NULL)
GO
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'6e0910b9-2ce7-4613-b6c4-1d6b3d7d82df', N'c26f738d-5906-4fb4-b63b-f6a326220002', 6, CAST(N'08:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'85451dfa-7010-43ed-8592-2b4e6efc66ca', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', 5, CAST(N'14:00:00' AS Time), CAST(N'17:00:00' AS Time), N'Phòng 102', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'5b220d3e-3e9f-4735-ab18-3c87d4e94f18', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', 5, CAST(N'08:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 202', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'9406ead4-750e-4b04-8ffd-3fd5df4a75c4', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', 6, CAST(N'08:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 101', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'c4915477-ba43-44c5-861a-512331068dbd', N'c26f738d-5906-4fb4-b63b-f6a326220002', 2, CAST(N'08:00:00' AS Time), CAST(N'10:00:00' AS Time), N'Phòng 301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'd80f809d-e8fd-46ed-a1fc-54efbfcde574', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', 2, CAST(N'10:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 202', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'4089b990-6f49-4631-8a62-788f2bc22d00', N'c26f738d-5906-4fb4-b63b-f6a326220002', 4, CAST(N'13:00:00' AS Time), CAST(N'17:00:00' AS Time), N'Phòng 301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'84b79735-4322-4f49-b0f8-839399a3168f', N'c26f738d-5906-4fb4-b63b-f6a326220002', 2, CAST(N'10:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 302', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'aec012ed-fb8c-41a9-a01f-88c3a83777b9', N'c26f738d-5906-4fb4-b63b-f6a326220002', 5, CAST(N'08:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 302', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'b581f9bd-37ba-4c9c-ad2a-8c102e846bce', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', 4, CAST(N'13:00:00' AS Time), CAST(N'17:00:00' AS Time), N'Phòng 201', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'26a52e5e-31a9-4725-be56-a476733b8318', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', 6, CAST(N'08:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 201', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'8b421252-1346-4660-9256-bc44faa21954', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', 4, CAST(N'08:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 101', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'185bfd00-4055-4959-88bb-bca95932a537', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', 2, CAST(N'08:00:00' AS Time), CAST(N'10:00:00' AS Time), N'Phòng 101', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'5c956f52-c514-4f38-b4ea-d70da6337c3b', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', 3, CAST(N'10:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 101', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'47fa5535-8c35-471d-b98d-e70b8365603e', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', 3, CAST(N'10:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 201', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'04bb9e20-64cb-4d06-baff-f6be5f176b1a', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', 2, CAST(N'10:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 102', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'2317cd2d-85ad-4394-8875-fa371f681e9a', N'c26f738d-5906-4fb4-b63b-f6a326220002', 3, CAST(N'10:00:00' AS Time), CAST(N'12:00:00' AS Time), N'Phòng 301', NULL, NULL)
INSERT [dbo].[doctor_work_schedule] ([schedule_id], [doctor_id], [day_of_week], [start_time], [end_time], [room], [effective_from], [effective_to]) VALUES (N'839ddf93-1465-49e2-9226-fec3973a5afd', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', 2, CAST(N'08:00:00' AS Time), CAST(N'10:00:00' AS Time), N'Phòng 201', NULL, NULL)
GO
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'77e59c88-4440-4417-b39b-1f64d556fe63', N'485f5310-9e17-49ce-98c3-77d0cea838ae', N'9a7a14c5-a8ba-45a2-9eba-60aff765cdf0', CAST(N'2025-06-21T21:00:04.627' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'e8298682-5b64-4fcd-bb90-1f998e5bee60', N'65170974-da61-4734-ad07-01be1bee6c06', N'b5aca21b-1568-443b-851e-fe5103a92ddc', CAST(N'2025-06-21T21:26:45.353' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'fa0ccd74-1873-4189-85d7-261470fcbfb7', N'4ad59ed1-7ff5-41dd-ba0e-8abf81cd554c', N'e1560b0d-b0ae-47a3-ba17-791ec2dfd5e8', CAST(N'2025-06-21T22:56:55.977' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'101c32d7-0c20-4032-b9ff-3ae33f453ecd', N'9594b4fc-310d-4d4d-92c6-cab33b4148a0', N'0776fe20-3783-4e4c-8bb1-cf726afa279e', CAST(N'2025-06-21T21:04:34.597' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'17d18956-77f3-4be6-9097-6d5bf909a2ec', N'75580bd8-788f-44df-a185-25f865a2bf3b', N'fbc133c4-fb56-4b94-a641-8dcc738902a4', CAST(N'2025-06-21T22:53:48.580' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'e9c22f5d-77a1-482b-9ecb-6f9f33de95fb', N'8381202b-f2a9-418b-80da-418eff413ead', N'8c58d2fe-f3ff-4570-ab12-ba996e545bd3', CAST(N'2025-06-21T21:21:29.897' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'08d28fc9-ded0-4089-8fff-874629097b61', N'bcf1c8ad-7165-4075-89e6-fd04ea189502', N'abc123-test-token', CAST(N'2025-06-21T20:57:07.623' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'1a31a234-6cd1-42ba-b6de-90c8fd15854f', N'f774d349-126f-4676-a7b0-9278f3b48b56', N'f0025271-6522-4ba8-abe8-243284414041', CAST(N'2025-06-21T21:33:26.953' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'8a465e89-4a12-4fcf-b7de-a7efa2c12374', N'21f3059f-acc2-4b8c-b4d6-319ec06138e2', N'038f13a2-e834-4959-954a-e00f4f563056', CAST(N'2025-06-21T22:32:32.340' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'2a5b01c1-cca7-4cba-9a82-c1c213897774', N'38d4440d-ce3a-405d-baa9-68b6fd814ab9', N'0a2a585e-7a25-434c-a3e4-54ebab94cad8', CAST(N'2025-06-21T21:16:47.210' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'1fa7d1e7-113a-485d-b3d8-d4556e3f3120', N'1a2a8e6a-b336-4ec6-9f36-0d9d7ccb7550', N'5a9457f7-3100-4e88-b5c4-3be43d3d66a9', CAST(N'2025-06-21T22:26:39.793' AS DateTime))
INSERT [dbo].[email_verification_token] ([id], [user_id], [token], [expiry_date]) VALUES (N'1202d2af-a4f9-405d-9486-ef82bd93f80f', N'efa6e5bc-4723-49b7-b89a-9acd6b4bf425', N'dce0a4b0-efaf-457c-9ed2-151f29401836', CAST(N'2025-06-21T21:24:34.227' AS DateTime))
GO
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'b883e127-9040-4a9a-b745-00bf43b18524', N'cbab7604-de1e-444a-9088-dc3e0ee038f2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'40f74dc9-9357-4854-8b0d-15c4647f7a9d', N'466782b6-0866-40f6-9778-630b1820e572', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'8b85f36a-9ad6-40d5-9846-18244cd4639d', N'75580bd8-788f-44df-a185-25f865a2bf3b', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'f46d1012-fe5b-4a28-9560-1b958d485aa4', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'IVF', N'MD, PhD', 12, N'[{"date":"2025-06-22","slots":["08:00","08:30","09:00"]},{"date":"2025-06-23","slots":["10:00","10:30"]}]', NULL, NULL, N'Chuyên gia IVF với hơn 12 năm kinh nghiệm, thực hiện >1.000 ca thành công.', N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'75918d43-f823-422f-8fd0-219e124493ff', N'ccf0252d-9ece-4338-96e5-1d6870e90da3', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'3b4eba2b-fba1-4ec7-8c2c-26b223667da1', N'4ad59ed1-7ff5-41dd-ba0e-8abf81cd554c', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'f65f136d-c862-44a2-bee0-2e23b12407fa', N'65170974-da61-4734-ad07-01be1bee6c06', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'9cc9a800-f58b-47c6-95eb-3331fdd45dfb', N'21f3059f-acc2-4b8c-b4d6-319ec06138e2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'93417540-b2fb-4dba-a917-353f676d12d1', N'2bbe41a8-ad3a-4ae4-8ee7-168ad3b32eb4', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'3af51b8d-6df7-432f-89f3-3a0b2e6bd673', N'04d26db3-11a3-4515-b947-86c426156a34', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'de20d286-6fb7-4cdc-8f9c-3b584d0e7b8a', N'e733d522-e868-4fc2-8c93-27c78a9bfecf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'ACTIVE', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'a365e1b7-01dc-4934-a4d3-417086501a35', N'1a2a8e6a-b336-4ec6-9f36-0d9d7ccb7550', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'f68b472a-0dfe-4190-809e-41c0756fb3d5', N'33702a81-a9fc-4776-872b-e8b05f8666b8', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'93a4b435-cf4d-42d8-b7ae-4232a4534936', N'0d96ce0a-5e57-4185-b8da-2da2fa8bcc50', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'3d79cee7-b5fd-4155-a21a-4719b6d29d1f', N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'a732f8a3-a760-4a5f-9842-4788dd27c8c8', N'61a68481-ede7-40b7-9145-3fac798b1427', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'f7bb84a4-7378-4b10-b8f2-482d749a8b08', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'IUI', N'MD', 15, N'[{"date":"2025-06-22","slots":["08:00","08:30","09:00"]},{"date":"2025-06-23","slots":["10:00","10:30"]}]', NULL, NULL, N'Bác sĩ nam khoa & IUI, nổi tiếng với tỷ lệ thành công cao cho các cặp vợ chồng trẻ.', N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'260f3969-1021-4852-8cce-49ac99bd93a8', N'c43077a6-bd00-4d45-a343-9471d351d611', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'e6e1902f-3033-477d-93a3-51a5227a0f94', N'efa6e5bc-4723-49b7-b89a-9acd6b4bf425', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'b2538f68-09ec-4c05-b266-58231c372920', N'38d4440d-ce3a-405d-baa9-68b6fd814ab9', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'b67dbe79-5d1f-4f9d-9a77-58a3347d7f1b', N'50d719e0-32e2-4316-b98c-5fc0279e5208', N'Infertility Treatment', N'MD, PhD', 12, N'    {
      "mon": {"start": "08:00", "end": "12:00"},
      "wed": {"start": "13:00", "end": "17:00"},
      "fri": {"start": "08:00", "end": "12:00"}
    }
', 4.9, 150, N'Expert in IVF and IUI procedures', N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'd5a7307d-b5c9-4707-9fce-58b112e181e8', N'f9b11b9b-020b-4730-93c2-b725620d41df', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'ACTIVE', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'414eb462-3e26-445e-b83f-5fba22a1cc5f', N'83f50776-8d05-4aa6-a1d6-a8abdf3daa8a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'3df961ca-91f6-4787-8964-62210b713b17', N'7ae74b7b-a370-4a93-896d-62438f96929a', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'383a80ce-3871-4154-88d3-631ec9214990', N'9594b4fc-310d-4d4d-92c6-cab33b4148a0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'e191141e-47ed-429b-a386-6cdc8a2d4e23', N'a38b2df1-49b6-4fdc-bc48-84809ca78f46', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'6dab025e-7066-424c-9428-6d3ac1a2ad01', N'485f5310-9e17-49ce-98c3-77d0cea838ae', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'1a7c7b62-00cf-498a-bef4-72e53d2df3ee', N'cf9e4b3f-3de0-406a-a189-c262d72c114e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'afd7618d-9421-47c1-a255-75a74ac94181', N'6e1201b7-ff96-4a83-8a42-8e8c70ba3465', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'ACTIVE', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'2405669f-1dcd-4692-8b8a-866181fef36f', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'38af8099-cb67-4dac-b346-8efbf2776dfc', N'91d5a531-cf07-4356-9a19-b77ea995c1bc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'ACTIVE', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'545dd30d-f7df-4c9e-afc8-9190043c963f', N'4773de14-aedc-4cf7-a62c-00c01c7bc90b', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'5b7aeb68-e005-4532-af2a-97120d8e58ff', N'5f6db14e-02f7-469b-a82e-8a13dfe32141', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'32438559-8ea5-4ab9-acf1-9b7410a78810', N'29011606-73af-4e53-8057-3fddae85d112', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, N'Fertility Services', N'view_reports,assign_doctors')
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'61256a13-8fd8-446a-843e-a11dbb5f8b20', N'fb58e1c2-cf8f-4e5a-adcd-40b144b99bd5', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'25109dea-f91d-4d19-8fc9-a89be81b1ba2', N'85851bfc-4bc6-417e-9fb2-21d6a92cf4d2', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'36280b41-65c8-4d41-93e8-ab4a6647a4a9', N'f774d349-126f-4676-a7b0-9278f3b48b56', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'a915430c-0256-4567-8add-b60bed9fbeaa', N'f1227d29-03d0-4a26-a9eb-80c84028a514', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'41bb72e8-c544-4273-933d-b71e3bc4ebcb', N'75575614-7d72-48e9-b40d-812b4cb2437e', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'ACTIVE', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'93b93533-6c59-44fe-a1d4-b955f824054a', N'c794374a-2c13-43e0-93e2-949e722dc0fb', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'f8829d51-24c8-4633-a4b8-c72698854274', N'bcf1c8ad-7165-4075-89e6-fd04ea189502', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'fa96a36d-7ada-4b6a-92a5-cb153cb64727', N'145180dd-c06b-46f0-b7d0-cbc388211a1c', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'77b2da38-6368-402e-b11c-cf5800a69c52', N'3f4271af-9434-4ec5-80d2-fc352b30f853', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'f1644104-7052-4952-a11c-dc01481ed484', N'8381202b-f2a9-418b-80da-418eff413ead', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'5478426a-403c-4b1c-bf4d-e3fef0370d9f', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', N'IVF', N'MD', 8, N'[{"date":"2025-06-22","slots":["08:00","08:30","09:00"]},{"date":"2025-06-23","slots":["10:00","10:30"]}]', NULL, NULL, N'Quan tâm đặc biệt tới bệnh nhân nữ hiếm muộn, tư vấn tận tâm.', N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'403e7665-e3f9-4ad8-a19f-f15f87f19e33', N'63ff80ac-73d5-4413-a4d5-599dab7075dc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'57537ca5-ccb2-424f-8c17-f783a056e042', N'1d8c6d0a-6df4-41eb-8058-7b1209507e2f', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'cdcfdc74-3085-4f53-bb02-fc42168cbe63', N'9f3cdd1b-0c94-44b3-8666-7c344419affc', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'active', NULL, NULL, NULL, NULL)
INSERT [dbo].[profile] ([profile_id], [user_id], [specialty], [qualification], [experience_years], [work_schedule], [rating], [case_count], [notes], [status], [marital_status], [health_background], [assigned_department], [extra_permissions]) VALUES (N'bfb24133-3152-4ee6-876e-fc781f9a8157', N'f360bb4e-94b6-42cb-8de0-24f6e04ff9db', NULL, NULL, NULL, NULL, NULL, NULL, NULL, N'ACTIVE', NULL, NULL, NULL, NULL)
GO
INSERT [dbo].[reminder_log] ([reminder_id], [appointment_id], [channel], [reminder_time], [status]) VALUES (N'6a59d985-0d9d-40b4-8192-601231c80094', N'9cc53887-aa0b-46c5-b201-686d51e62ec5', N'EMAIL', CAST(N'2025-06-27T15:51:00.0000000' AS DateTime2), N'SENT')
INSERT [dbo].[reminder_log] ([reminder_id], [appointment_id], [channel], [reminder_time], [status]) VALUES (N'7197eb11-cbfb-4d74-89cc-85c319148853', N'9cc53887-aa0b-46c5-b201-686d51e62ec5', N'EMAIL', CAST(N'2025-06-27T15:51:00.0000000' AS DateTime2), N'SENT')
GO
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'f13e77bc-bede-4980-b614-0b1291b7c345', N'f9b11b9b-020b-4730-93c2-b725620d41df', N'DOCTOR', 2)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'f447cfb3-de3e-4f59-ba92-0caa8c2d0879', N'c794374a-2c13-43e0-93e2-949e722dc0fb', N'DOCTOR', 2)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'c226e40b-d463-47a3-8d7d-1084b122f3dd', N'85851bfc-4bc6-417e-9fb2-21d6a92cf4d2', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'98de941f-3f66-4f62-937a-1576532f4b87', N'fb58e1c2-cf8f-4e5a-adcd-40b144b99bd5', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'e4f3e76c-c04b-42f7-94ed-17bf16f3dc9f', N'0d96ce0a-5e57-4185-b8da-2da2fa8bcc50', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'66138906-6186-4ac3-8d83-1832f87abeec', N'9594b4fc-310d-4d4d-92c6-cab33b4148a0', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'9e595842-25f1-4075-b4f5-1e04fa5cc1ed', N'9f3cdd1b-0c94-44b3-8666-7c344419affc', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'6fedb49d-ec40-48a5-ac23-20cb7d003b02', N'3f4271af-9434-4ec5-80d2-fc352b30f853', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'c8484117-d009-45a2-98c5-23c3d4f1dab6', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'DOCTOR', 30)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'8860ac32-fad3-4439-a50a-318107fbe52d', N'c43077a6-bd00-4d45-a343-9471d351d611', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'58580f32-332d-49c8-bdac-3334b1308543', N'1d8c6d0a-6df4-41eb-8058-7b1209507e2f', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'3f445bc4-58d7-4df5-926d-3909bcb4fbcb', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', N'DOCTOR', 30)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'03faeb09-6b71-46ab-9131-39d5846ede07', N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'0efc9076-e510-4b2e-8e32-3c7005102c41', N'65170974-da61-4734-ad07-01be1bee6c06', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'814dccfb-42eb-40b7-b991-46a659cbde80', N'efa6e5bc-4723-49b7-b89a-9acd6b4bf425', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'21769b67-c76e-407f-a9d9-5491173b0586', N'145180dd-c06b-46f0-b7d0-cbc388211a1c', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'6ba96a81-3ea1-47ba-9a6c-5762fbcc6cb1', N'8381202b-f2a9-418b-80da-418eff413ead', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'9976f6e7-12ff-43ba-9bde-57b568b85029', N'e733d522-e868-4fc2-8c93-27c78a9bfecf', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'0c6c0b8c-a28e-40e3-8ab0-621c22204d8e', N'7ae74b7b-a370-4a93-896d-62438f96929a', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'93cec408-f068-4c11-9e23-6397415023e7', N'485f5310-9e17-49ce-98c3-77d0cea838ae', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'b230fea0-ca22-4ff7-a969-6650ba134ec2', N'bcf1c8ad-7165-4075-89e6-fd04ea189502', N'ADMIN', 4)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'446a5c94-374e-4f23-92c0-67690e3543bd', N'4ad59ed1-7ff5-41dd-ba0e-8abf81cd554c', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'f42f4f38-1788-4de5-8350-7db4d40138d2', N'63ff80ac-73d5-4413-a4d5-599dab7075dc', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'b166665b-bdaa-4da1-9003-7e8540f89223', N'6e1201b7-ff96-4a83-8a42-8e8c70ba3465', N'MANAGER', 3)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'd2ffa73a-2919-4fa5-bbcc-7eb2ba05503e', N'5f6db14e-02f7-469b-a82e-8a13dfe32141', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'0f418589-957a-4b12-9ad2-819b3feeac4a', N'1a2a8e6a-b336-4ec6-9f36-0d9d7ccb7550', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'27b4625b-1cf3-4083-b4e3-8801fc56989f', N'61a68481-ede7-40b7-9145-3fac798b1427', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'39fecfa3-ec00-46f2-9091-8a3f108abd29', N'f1227d29-03d0-4a26-a9eb-80c84028a514', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'd1390036-c783-453d-a10f-96d53ae32788', N'75575614-7d72-48e9-b40d-812b4cb2437e', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'b30d7631-0183-4765-966f-9b1cbeb4e2e5', N'4773de14-aedc-4cf7-a62c-00c01c7bc90b', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'd20d70f4-58fe-4026-95db-9c1e4e5b4e27', N'2bbe41a8-ad3a-4ae4-8ee7-168ad3b32eb4', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'1cf0daec-d010-42c9-b281-9c43743fb2e7', N'38d4440d-ce3a-405d-baa9-68b6fd814ab9', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'61ff6d5d-7b7c-4d3b-ac65-9f27980cb3f5', N'50d719e0-32e2-4316-b98c-5fc0279e5208', N'DOCTOR', 2)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'eb98dadd-184f-4330-8e63-a382182f1b86', N'75580bd8-788f-44df-a185-25f865a2bf3b', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'35366658-4121-48ff-93b7-a8cb5ba3a114', N'cbab7604-de1e-444a-9088-dc3e0ee038f2', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'aa3ab3c9-a117-4122-a3b3-ad8e341a9d23', N'cf9e4b3f-3de0-406a-a189-c262d72c114e', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'62f709f5-464f-401f-8843-aec655ad3cb2', N'91d5a531-cf07-4356-9a19-b77ea995c1bc', N'DOCTOR', 2)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'fba43435-84bb-49b2-9368-b1337581f60f', N'466782b6-0866-40f6-9778-630b1820e572', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'41965c62-d83d-4965-b027-b81ccb84da4f', N'f774d349-126f-4676-a7b0-9278f3b48b56', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'6fa3bf95-f100-4f7b-a971-b9561c81be61', N'83f50776-8d05-4aa6-a1d6-a8abdf3daa8a', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'fb390c9e-bfce-4d0a-ace4-bdad13613edc', N'21f3059f-acc2-4b8c-b4d6-319ec06138e2', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'c58346a1-3044-4379-a125-c877690b3515', N'f360bb4e-94b6-42cb-8de0-24f6e04ff9db', N'DOCTOR', 2)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'80647527-1cf8-48a3-bf26-d7f4b9b0d862', N'29011606-73af-4e53-8057-3fddae85d112', N'MANAGER', 3)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'2d094e36-1b27-47e5-a4e1-d9da4b30e102', N'a38b2df1-49b6-4fdc-bc48-84809ca78f46', N'MANAGER', 3)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'c889546c-4c24-4022-afcf-ddece4ec44b7', N'33702a81-a9fc-4776-872b-e8b05f8666b8', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'a05813e1-e470-4a0e-9344-e2e14cf3f7c6', N'ccf0252d-9ece-4338-96e5-1d6870e90da3', N'ADMIN', 4)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'980736a3-f791-4f8b-9dce-e62dc5c6a153', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'DOCTOR', 30)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'b6eca8ea-6a6c-4d99-80b9-f32523b3f14b', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', N'CUSTOMER', 1)
INSERT [dbo].[role] ([role_id], [user_id], [role_type], [role_level]) VALUES (N'dc225a9b-804e-48a8-9ce3-fa34f95396d1', N'04d26db3-11a3-4515-b947-86c426156a34', N'CUSTOMER', 1)
GO
INSERT [dbo].[service] ([service_id], [name], [description], [price], [estimated_duration], [created_at]) VALUES (N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1', N'IUI', N'Dịch vụ IUI dành cho cặp đôi vô sinh nhẹ', CAST(15000000.00 AS Numeric(38, 2)), 14, CAST(N'2025-06-16T17:54:03.603' AS DateTime))
INSERT [dbo].[service] ([service_id], [name], [description], [price], [estimated_duration], [created_at]) VALUES (N'735d0713-6e9b-4ced-9621-a30190faebf3', N'IVF', N'Dịch vụ hỗ trợ sinh sản IVF', CAST(50000000.00 AS Numeric(38, 2)), 30, CAST(N'2025-06-16T17:54:03.603' AS DateTime))
GO
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'4a9db849-db38-49f2-b7e0-0151933882ee', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-15T09:00:00.000' AS DateTime), N'', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-26T23:49:55.647' AS DateTime), CAST(N'2025-06-26T23:49:55.697' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'0c925445-e15c-4e2a-b6e3-0dcb30933da6', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-26T09:00:00.000' AS DateTime), N'Đặt lịch chọn bác sĩ thủ công', N'manual', N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', N'Scheduled', CAST(N'2025-06-25T14:05:16.770' AS DateTime), CAST(N'2025-06-25T14:05:16.780' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'18459e8e-a17f-4691-b888-1307c2a3b472', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-26T08:00:00.000' AS DateTime), N'Đặt lịch chọn bác sĩ thủ công', N'manual', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'PendingAssignment', CAST(N'2025-06-25T14:03:19.193' AS DateTime), NULL, N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'15b7917c-0b7f-4d81-9409-153adfe7de01', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T08:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-25T10:42:45.313' AS DateTime), CAST(N'2025-06-25T10:42:45.383' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'38e10781-b1a0-480a-bfdf-1792834a37e3', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-15T09:00:00.000' AS DateTime), N'', N'auto', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Scheduled', CAST(N'2025-06-25T16:56:09.347' AS DateTime), CAST(N'2025-06-25T16:56:09.360' AS DateTime), N'735d0713-6e9b-4ced-9621-a30190faebf3')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'b53f5f73-25b8-4e08-b8f7-2c083aaa80f2', N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', CAST(N'2025-07-05T09:00:00.000' AS DateTime), N'', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-26T23:18:58.493' AS DateTime), CAST(N'2025-06-26T23:18:58.540' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'19d5c729-c558-4bcb-a68f-475051c1be5a', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-26T08:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'auto', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Scheduled', CAST(N'2025-06-25T14:00:05.427' AS DateTime), CAST(N'2025-06-25T14:00:05.493' AS DateTime), N'735d0713-6e9b-4ced-9621-a30190faebf3')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'0a3a50d2-112a-4e51-b833-48dee378839d', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-29T08:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-25T10:55:44.487' AS DateTime), CAST(N'2025-06-25T10:55:44.570' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'50350dd1-bed9-437b-be5a-5ea8b5e52979', N'c43077a6-bd00-4d45-a343-9471d351d611', CAST(N'2025-06-28T10:00:00.000' AS DateTime), N'', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-27T11:11:29.487' AS DateTime), CAST(N'2025-06-27T11:11:29.540' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'8365b410-bdfe-4948-999b-95396a73d3f8', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-10T09:00:00.000' AS DateTime), N'Hahahaahaha', N'auto', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Scheduled', CAST(N'2025-06-25T16:33:03.057' AS DateTime), CAST(N'2025-06-25T16:33:03.133' AS DateTime), N'735d0713-6e9b-4ced-9621-a30190faebf3')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'5a152d35-d2fc-4be0-840a-95df826a035d', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T10:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'manual', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Scheduled', CAST(N'2025-06-25T14:26:15.267' AS DateTime), CAST(N'2025-06-25T14:26:15.330' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'a0cca9b5-919c-43d1-aee6-aac2bc68edaa', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-28T08:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-25T14:22:53.073' AS DateTime), CAST(N'2025-06-25T14:22:53.130' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'7d6083b2-3975-41a9-a95b-b837f6285572', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-09T10:00:00.000' AS DateTime), N'', N'auto', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Scheduled', CAST(N'2025-06-25T16:55:01.957' AS DateTime), CAST(N'2025-06-25T16:55:01.990' AS DateTime), N'735d0713-6e9b-4ced-9621-a30190faebf3')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'b6716ba2-8bb5-4624-b868-be00e60de408', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-07-09T11:00:00.000' AS DateTime), N'', N'manual', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-25T16:57:48.053' AS DateTime), CAST(N'2025-06-25T16:57:48.097' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'de0f1a7b-c268-4134-993e-d721c531ab29', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-06-27T10:00:00.000' AS DateTime), N'', N'manual', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-25T16:31:00.347' AS DateTime), CAST(N'2025-06-25T16:31:00.390' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'4ba93858-fa6d-48b4-9cd3-d78a29a47e1b', N'33702a81-a9fc-4776-872b-e8b05f8666b8', CAST(N'2025-06-28T11:00:00.000' AS DateTime), N'', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-27T10:42:07.330' AS DateTime), CAST(N'2025-06-27T10:42:07.393' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'63cf143d-ba50-48ba-b87e-e5a86c9837aa', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T09:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'auto', N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Scheduled', CAST(N'2025-06-25T11:35:31.973' AS DateTime), CAST(N'2025-06-25T11:35:32.030' AS DateTime), N'735d0713-6e9b-4ced-9621-a30190faebf3')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'3ec0436a-ef94-4a18-af5a-e6b990196d9c', N'3a85e9f9-a466-45c1-a276-2a2f6332d592', CAST(N'2025-06-27T11:00:00.000' AS DateTime), N'Đặt lịch tự động chọn bác sĩ', N'manual', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-25T14:27:17.573' AS DateTime), CAST(N'2025-06-25T14:27:17.580' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
INSERT [dbo].[service_request] ([request_id], [customer_id], [preferred_datetime], [note], [doctor_selection], [preferred_doctor_id], [status], [created_at], [updated_at], [service_id]) VALUES (N'34d17ff6-8d31-41aa-b259-f353770f44a2', N'c43077a6-bd00-4d45-a343-9471d351d611', CAST(N'2025-06-28T11:00:00.000' AS DateTime), N'', N'auto', N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Scheduled', CAST(N'2025-06-27T11:02:01.270' AS DateTime), CAST(N'2025-06-27T11:02:01.333' AS DateTime), N'82cb6fa6-ff24-4f0c-bade-00d5a3fa84f1')
GO
INSERT [dbo].[treatment_Phase] ([phase_id], [service_id], [phase_name], [phase_order], [description], [expected_duration], [created_at], [updated_at]) VALUES (N'56fe4248-ffde-4064-b172-0ae998bdacda', N'21e25410-b8eb-4180-96c5-d2d586137e0f', N'Xét nghiệm máu', 2, N'Xét nghiệm hormone và các chỉ số cơ bản', 2, CAST(N'2025-06-14T03:10:33.000' AS DateTime), NULL)
INSERT [dbo].[treatment_Phase] ([phase_id], [service_id], [phase_name], [phase_order], [description], [expected_duration], [created_at], [updated_at]) VALUES (N'4a114ea9-ac38-4680-8bb7-763a22ea0e42', N'21e25410-b8eb-4180-96c5-d2d586137e0f', N'Khám tổng quát', 1, N'Khám ban đầu và ghi nhận tiền sử', 1, CAST(N'2025-06-14T03:10:33.000' AS DateTime), NULL)
GO
INSERT [dbo].[treatment_Plan_Template] ([template_id], [name], [description], [steps], [created_at], [updated_at]) VALUES (N'b193d4c1-d9ee-41b4-a417-41210758b91e', N'Kế hoạch điều trị vô sinh cơ bản', N'Phác đồ dành cho trường hợp nhẹ', N'Khám tổng quát > Xét nghiệm máu > Siêu âm', CAST(N'2025-06-14T03:09:45.000' AS DateTime), NULL)
GO
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'4773de14-aedc-4cf7-a62c-00c01c7bc90b', N'Sơn Nguyễn', N'MALE', CAST(N'2002-06-07' AS Date), N'moponap380@coasah.com', N'0937634997', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-25T08:21:54.437' AS DateTime), CAST(N'2025-06-25T08:22:26.893' AS DateTime), N'$2a$10$UvBYiqZN3mxrq7CmkfyUX.DiByMB50XL.ygomkHjVFVi/xgT4.WP2', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'65170974-da61-4734-ad07-01be1bee6c06', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson20027@gmail.com', N'0937634799', N'string', N'string', CAST(N'2025-06-20T21:26:45.297' AS DateTime), NULL, N'$2a$10$2XTlRwPVL3Uht3IcBLE/9uG881BTPdRzCHhJ83TbJnXinXcJql8Jm', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'1a2a8e6a-b336-4ec6-9f36-0d9d7ccb7550', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tieuviem060@gmail.com', N'0937634773', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-20T22:26:39.670' AS DateTime), NULL, N'$2a$10$bmIAVJyjgCeWpcOcaeeZc.Nr5zV9Wn4xG81O2XQNwZMGGuSRl36/.', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'2bbe41a8-ad3a-4ae4-8ee7-168ad3b32eb4', N'Pham Le Hoang Phuc (K18 HCM)', N'MALE', CAST(N'1990-01-01' AS Date), N'phucplhse183189@fpt.edu.vn', NULL, NULL, N'https://example.com/default-avatar.png', CAST(N'2025-06-24T10:48:44.120' AS DateTime), NULL, N'GOOGLE_AUTH', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'ccf0252d-9ece-4338-96e5-1d6870e90da3', N'Admin User', N'MALE', CAST(N'1990-01-01' AS Date), N'admin@example.com', N'0123456789', N'Admin Address', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T22:09:45.427' AS DateTime), NULL, N'$2a$10$vHoWJL0Yde2Mb1SHYwpEjeETi9Jpm0IBmXZbZiUan9snbEepRVyl.', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'9a1f4d4d-12e4-4e11-ae50-1ea3e3111001', N'Dr. Nguyễn Thị Mai', N'FEMALE', CAST(N'1982-03-15' AS Date), N'mai.nguyen@example.com', N'0903000001', N'12 Lý Thường Kiệt, Hà Nội', NULL, CAST(N'2025-06-16T21:22:24.950' AS DateTime), NULL, N'12345', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'85851bfc-4bc6-417e-9fb2-21d6a92cf4d2', N'Sơn Nguyễn', N'MALE', CAST(N'2002-06-07' AS Date), N'deyex76127@decodewp.com', N'0937634999', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-25T08:17:21.063' AS DateTime), CAST(N'2025-06-25T08:17:58.997' AS DateTime), N'$2a$10$VJ2eXrKHh7f/J5Ly9dXCbuWz.ILjEB0w1PKQm8DeNlPwRa2sqP2du', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'f360bb4e-94b6-42cb-8de0-24f6e04ff9db', N'Nguyễn Văn B', N'MALE', CAST(N'1990-10-10' AS Date), N'nguyen1@example.com', N'0987654326', N'456 Đường Trần Hưng Đạo, Hà Nội', N'https://example.com/avatar-b.jpg', CAST(N'2025-06-18T00:50:45.160' AS DateTime), NULL, N'$2a$10$Cr.0vGlIj3L8jF2GAdumT.e9XWVx7LBmOCtEvNylxZacuz07Oycn.', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'75580bd8-788f-44df-a185-25f865a2bf3b', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tieuvie@gmail.com', N'0937634876', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-20T22:53:48.560' AS DateTime), NULL, N'$2a$10$TG1aQOCudSELAigyeFTCZer/sf8V3ko0TNwNXS2iato8cTD8.ioqe', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'e733d522-e868-4fc2-8c93-27c78a9bfecf', N'string', N'MALE', CAST(N'2025-06-17' AS Date), N'string', N'string', N'string', N'string', CAST(N'2025-06-18T00:43:56.263' AS DateTime), NULL, N'$2a$10$EDNsm4NFmHurtDyd.v.HdesqMJKHQdvmCZeAY4fVrtAlCcpeGez0e', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'3a85e9f9-a466-45c1-a276-2a2f6332d592', N'Sơn Nguyễn', N'FEMALE', CAST(N'2002-06-07' AS Date), N'magove1223@coasah.com', N'0937634544', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-25T08:29:55.063' AS DateTime), CAST(N'2025-06-25T08:35:38.490' AS DateTime), N'$2a$10$v6/NW2Mo0X.pso9Iz43NveRRH4oqYIGTGwDM7aZzUba1.0rhMVzba', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'0d96ce0a-5e57-4185-b8da-2da2fa8bcc50', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tukhuyet200002@gmail.com', N'0937634866', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-21T11:25:58.840' AS DateTime), CAST(N'2025-06-21T11:27:41.387' AS DateTime), N'$2a$10$hVtQ9ahvIemxjMGsQ3w1buSnolIjM3JtqKliEGqkMHPCojfTxUuUC', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'21f3059f-acc2-4b8c-b4d6-319ec06138e2', N'Nguyễn Phạm Xuân Sơn', N'FEMALE', CAST(N'2002-06-07' AS Date), N'tieuviem@gmail.com', N'0937634792', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-20T22:32:32.313' AS DateTime), NULL, N'$2a$10$aits/5EyzfU9LJdeiWVtX.YKYoA5IWKRtYIl6EVtpmDA4SZoLMwW2', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'61a68481-ede7-40b7-9145-3fac798b1427', N'Trần Thị Bích Ngọc', N'FEMALE', CAST(N'1995-08-15' AS Date), N'ngoctran@example.com', N'0912345679', N'123 Đường Hoa Hồng, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-18T00:31:10.603' AS DateTime), NULL, N'$2a$10$/rEnQQwjZpXt634T1WJ2secNR.nDRGFPpjTc1Kqj57PX2L0Ps1Kpm', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'29011606-73af-4e53-8057-3fddae85d112', N'Manager Jane', N'MALE', CAST(N'1983-09-10' AS Date), N'manager@example.com', N'0900000004', N'Manager Department', N'https://example.com/default-avatar.png', CAST(N'2025-06-23T19:06:55.337' AS DateTime), NULL, N'$2a$10$vbhozmlHnlYdezsnZiq6weSJl/nxY8bjSa02So6nspWAg8Nhzje5O', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'fb58e1c2-cf8f-4e5a-adcd-40b144b99bd5', N'Nguyễn Văn A', N'MALE', CAST(N'1990-05-20' AS Date), N'customer@example.com', N'0012345678', N'123 Đường ABC, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-18T00:24:01.657' AS DateTime), NULL, N'$2a$10$GGxjprxV13QHuxqy5EUvAeWjvKgYaPtUsQetxEuOdrbiQRBFGvEjS', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'8381202b-f2a9-418b-80da-418eff413ead', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson20027778@gmail.com', N'0937634748', N'string', N'string', CAST(N'2025-06-20T21:21:29.857' AS DateTime), NULL, N'$2a$10$9fPm7qYdVboutD5tt9kB4uHtWlKfVL81N2eEmd8sYyPyrEaUVBbZm', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'63ff80ac-73d5-4413-a4d5-599dab7075dc', N'Trần Thị Bích Ngọc', N'FEMALE', CAST(N'1995-08-15' AS Date), N'customer41@gmail.com', N'0912345697', N'123 Đường Hoa Hồng, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-18T01:13:15.470' AS DateTime), NULL, N'$2a$10$3rtkbo6EEB8/VmtybViypON4kfDkoRhAnBbhL9EeEExRIJqxAOiTq', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'50d719e0-32e2-4316-b98c-5fc0279e5208', N'Dr. John Doe', N'MALE', CAST(N'1985-03-15' AS Date), N'doctor@example.com', N'0900000003', N'Doctor''s Office', N'https://example.com/default-avatar.png', CAST(N'2025-06-23T19:06:55.157' AS DateTime), NULL, N'$2a$10$/wsAyxnDHjkwS1wzNR7R4.X.ICzuBD9HyAb8.pFI26onbjcXbHM9u', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'7ae74b7b-a370-4a93-896d-62438f96929a', N'string', N'MALE', CAST(N'2025-06-24' AS Date), N'ramig35781@decodewp.com', N'+84973484796', N'string', N'string', CAST(N'2025-06-24T16:17:19.403' AS DateTime), CAST(N'2025-06-24T16:35:04.577' AS DateTime), N'$2a$10$EpMuo3ijkncf.f3wi0HWVOOvh8nNSCdkYf1c6laxHdYh6RoprE2ou', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'466782b6-0866-40f6-9778-630b1820e572', N'Sơn Nguyễn', N'FEMALE', CAST(N'2002-06-07' AS Date), N'nafip90653@coasah.com', N'0937634764', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-24T22:30:18.967' AS DateTime), CAST(N'2025-06-24T22:30:49.780' AS DateTime), N'$2a$10$hFUBt58eD89D9q73SvTtB.ChEKzP87Ga21DBI3MFTvGzGcs6Zt0Je', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'38d4440d-ce3a-405d-baa9-68b6fd814ab9', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson2002777@gmail.com', N'0937634741', N'string', N'string', CAST(N'2025-06-20T21:16:47.170' AS DateTime), NULL, N'$2a$10$ld9CEmMmStG.Jidzf42h7eEHLDLIQN8QFh9lX43KrUMgq9D4Z6dBC', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'3fbbccd2-f6d8-4fa3-8b38-74c0ff990003', N'Dr. Phạm Thùy Linh', N'FEMALE', CAST(N'1990-07-22' AS Date), N'linh.pham@example.com', N'0903000003', N'99 Phạm Văn Đồng, Đà Nẵng', NULL, CAST(N'2025-06-16T21:22:24.950' AS DateTime), NULL, N'12345', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'485f5310-9e17-49ce-98c3-77d0cea838ae', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'customer12334@gmail.com', N'+84322173221', N'string', N'string', CAST(N'2025-06-20T21:00:04.583' AS DateTime), NULL, N'$2a$10$gTinkDcZgBzxgBKIYJeNzeKNPwmw2Gc6NwVWnQp0D1XSc7/My49B2', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'1d8c6d0a-6df4-41eb-8058-7b1209507e2f', N'Customer111', N'MALE', CAST(N'1995-05-20' AS Date), N'customer1@gmail.com', N'9123456789', N'123 Đường ABC, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-17T23:51:40.970' AS DateTime), NULL, N'$2a$10$p.jnYbfL1RdA41v4Fz0Y7eaYhOzD.IC20H2ZwB/SQx6FTS5ZMq06e', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'9f3cdd1b-0c94-44b3-8666-7c344419affc', N'Trần Thị Bích Ngọc', N'FEMALE', CAST(N'1995-08-15' AS Date), N'customer4@gmail.com', N'0912345677', N'123 Đường Hoa Hồng, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-18T00:32:46.383' AS DateTime), NULL, N'$2a$10$DFFq8QAN6Kkf5MmpbZv03eF2hKkVzRUBK8mtyBaMQ3pSZOgLOFc0y', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'f1227d29-03d0-4a26-a9eb-80c84028a514', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tukhuyet200244@gmail.com', N'0937634853', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-24T08:41:51.183' AS DateTime), CAST(N'2025-06-24T08:42:33.133' AS DateTime), N'$2a$10$S72uEfoNewBB3kGQfs6jpuM1mQ18oUhdUr5L/O5.LN75fvQCsDqJy', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'75575614-7d72-48e9-b40d-812b4cb2437e', N'taseno', N'MALE', CAST(N'2025-06-25' AS Date), N'taseno9512@decodewp.com', N'+84540507775', N'string', N'string', CAST(N'2025-06-25T15:46:47.260' AS DateTime), NULL, N'$2a$10$taARjIJZF8kJCfKOWrFZPegy8C8.fudi/VHzqdf34.nRGujemTKM.', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'a38b2df1-49b6-4fdc-bc48-84809ca78f46', N'Manager', N'MALE', CAST(N'1995-08-15' AS Date), N'manager@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:10:46.530' AS DateTime), NULL, N'$2a$10$WR9iqOYwViy8aeLUHYB1f.4r1qtburQOU/OwFPAxCnKTBqX45xCYW', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'04d26db3-11a3-4515-b947-86c426156a34', N'Nguyễn Văn A', N'MALE', CAST(N'1990-05-20' AS Date), N'nguyenvana@example.com', N'0212345678', N'123 Đường ABC, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-18T00:21:01.923' AS DateTime), NULL, N'$2a$10$ymIaU3FswCSRlOivyHQxAO/V6UvsO399NkXnhEXhy2HeMpiA5gif2', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'5f6db14e-02f7-469b-a82e-8a13dfe32141', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tukhuyet2002@gmail.com', N'0981811111', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-24T09:29:16.397' AS DateTime), CAST(N'2025-06-24T09:30:32.967' AS DateTime), N'$2a$10$bvU6KGpPaGhIBrvRXyk2IOdH6D8aSVwuFsUw8GUCKH6sqxdXorjKm', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'4ad59ed1-7ff5-41dd-ba0e-8abf81cd554c', N'Nguyễn Phạm Xuân Sơn', N'FEMALE', CAST(N'2002-06-07' AS Date), N'tieuviem07@gmail.com', N'0937634825', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-20T22:56:55.963' AS DateTime), NULL, N'$2a$10$ftBLdqohyp3HD4mkFD3oQulR039rC3/bc/3rlDfw2Nbc1SmPSQocW', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'6e1201b7-ff96-4a83-8a42-8e8c70ba3465', N'string', N'MALE', CAST(N'2025-06-24' AS Date), N'customer123@gmail.com', N'+84478372712', N'string', N'string', CAST(N'2025-06-24T09:58:31.040' AS DateTime), NULL, N'$2a$10$..jdPI6fIMouEIBr0UBXIu5Y1W8EJ0La7SrilpUooPfyGrksQQ.cu', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'3019df6c-f2e2-49f6-ad2c-90ff3f906e78', N'Customer', N'MALE', CAST(N'1995-08-15' AS Date), N'customer@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:09:20.000' AS DateTime), NULL, N'$2a$10$zAju5jOPqI6Y9yoyF4aXUuldacPu0bPHBR9O4yzA6.ccHSIiYPxoS', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'f774d349-126f-4676-a7b0-9278f3b48b56', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson2004@gmail.com', N'0937634761', N'string', N'string', CAST(N'2025-06-20T21:33:26.910' AS DateTime), NULL, N'$2a$10$HRc.WhkVKOGVl0cHS9t13u.gSpLoiNBIUkhBdA3M1NoJj2p3QygLC', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'c43077a6-bd00-4d45-a343-9471d351d611', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson20027774@gmail.com', N'0937634451', N'string', N'string', CAST(N'2025-06-20T21:42:37.050' AS DateTime), CAST(N'2025-06-24T16:47:37.280' AS DateTime), N'$2a$10$o1dHuoT2GV2bUgIfcrBjfOCC/Jtft7esyyFHMrDbO1SOqI0PJzAIy', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'c794374a-2c13-43e0-93e2-949e722dc0fb', N'Doctor', N'MALE', CAST(N'1995-08-15' AS Date), N'doctor@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:10:21.423' AS DateTime), NULL, N'$2a$10$./rR1Rp2zWKcN1zMgKvOfeq9n80s3O/3Zjq4k1YO3sV556JTiR2q6', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'efa6e5bc-4723-49b7-b89a-9acd6b4bf425', N'string', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson2002@gmail.com', N'0937634771', N'string', N'string', CAST(N'2025-06-20T21:24:34.183' AS DateTime), NULL, N'$2a$10$lgB3TCT6.ol4vsgZptc1JuHdkHTUAWfEZaJsNQPiQPte6jriid02m', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'83f50776-8d05-4aa6-a1d6-a8abdf3daa8a', N'SonNguyen', N'MALE', CAST(N'2002-06-07' AS Date), N'mebiy49250@coasah.com', N'0937634567', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-24T22:19:29.907' AS DateTime), CAST(N'2025-06-24T22:20:56.903' AS DateTime), N'$2a$10$JdQQgr0UyaPYSkPsA7fT7u2Fc5Ip7Yv40DbBjwuKJ4FvH6fqMM5Bm', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'f9b11b9b-020b-4730-93c2-b725620d41df', N'Nguyễn Văn B', N'MALE', CAST(N'1990-10-10' AS Date), N'nguyenb@example.com', N'0987654322', N'456 Đường Trần Hưng Đạo, Hà Nội', N'https://example.com/avatar-b.jpg', CAST(N'2025-06-18T00:46:22.563' AS DateTime), NULL, N'$2a$10$Wb9AgCS5k/0wSSOIwf3sU.FtO3Fb.FvWdu8SwZ.GCuIoYmJybcIWW', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'91d5a531-cf07-4356-9a19-b77ea995c1bc', N'Nguyễn Văn B', N'MALE', CAST(N'1990-10-10' AS Date), N'nguyen@example.com', N'0987654328', N'456 Đường Trần Hưng Đạo, Hà Nội', N'https://example.com/avatar-b.jpg', CAST(N'2025-06-18T00:47:58.880' AS DateTime), NULL, N'$2a$10$R0GtylHJg4D2W8RvaGCw9.qPnmshrUJxKMsEwtaikk..IqtX82CZ2', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'cf9e4b3f-3de0-406a-a189-c262d72c114e', N'Normal User', N'FEMALE', CAST(N'1995-05-05' AS Date), N'user@example.com', N'0987654321', N'User Address', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T22:09:45.563' AS DateTime), NULL, N'$2a$10$G6rd8Htehkc74dNi29/6rePus2yctun2jqqiOgv63aCWlXJI/aQdu', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'9594b4fc-310d-4d4d-92c6-cab33b4148a0', N'GmailTest', N'MALE', CAST(N'2025-06-20' AS Date), N'anhson200277745@gmail.com', N'0937634740', N'string', N'string', CAST(N'2025-06-20T21:04:34.577' AS DateTime), NULL, N'$2a$10$tIzW9dFCCFrO6f8B09FdMOYdlMxzTIMh2K5Eji0eLopGoDc7TUVbG', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'145180dd-c06b-46f0-b7d0-cbc388211a1c', N'string', N'MALE', CAST(N'2025-06-23' AS Date), N'biyer93572@coasah.com', N'0405453837', N'string', N'string', CAST(N'2025-06-23T19:33:23.570' AS DateTime), CAST(N'2025-06-23T19:34:51.537' AS DateTime), N'$2a$10$4BuWi/lRBs78I5Du3vTLJOt7BDzHwK54NCSDeIQHhYmslHPBUHN0C', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'cbab7604-de1e-444a-9088-dc3e0ee038f2', N'Nguyễn Phạm Xuân Sơn', N'MALE', CAST(N'2002-06-07' AS Date), N'tieuviem0607@gmail.com', N'0937634824', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-20T23:06:28.380' AS DateTime), CAST(N'2025-06-20T23:06:49.063' AS DateTime), N'$2a$10$giuF9t/eRcENPLxp9IoG7.SQUfJxhn8o7vfx9i6lLlCAlt3yW8ylC', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'33702a81-a9fc-4776-872b-e8b05f8666b8', N'snsnshj', N'MALE', CAST(N'2004-05-05' AS Date), N'solewa2307@decodewp.com', N'0937634555', N'Đồng Nai', N'https://example.com/default-avatar.png', CAST(N'2025-06-25T15:50:22.170' AS DateTime), CAST(N'2025-06-25T15:51:14.933' AS DateTime), N'$2a$10$N1yOlRYik4D8VWkuyYAFauRRBDKlKuun338pJT/7wjk2K39StQnDW', 1)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'c26f738d-5906-4fb4-b63b-f6a326220002', N'Dr. Trần Văn An', N'MALE', CAST(N'1979-11-08' AS Date), N'an.tran@example.com', N'0903000002', N'25 Nguyễn Đình Chiểu, TP.HCM', NULL, CAST(N'2025-06-16T21:22:24.950' AS DateTime), NULL, N'12345', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'3f4271af-9434-4ec5-80d2-fc352b30f853', N'Customer111', N'MALE', CAST(N'1995-05-20' AS Date), N'customer111@gmail.com', N'0123456789', N'123 Đường ABC, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-17T23:28:15.940' AS DateTime), NULL, N'$2a$10$7H80Ff.ZYIaFkEQG8ZgSnOTepD19sV/ngdvJIikA4u4W2ntjf49tK', 0)
INSERT [dbo].[users] ([user_id], [full_name], [gender], [date_of_birth], [email], [phone], [address], [avatar_url], [created_at], [updated_at], [password], [is_email_verified]) VALUES (N'bcf1c8ad-7165-4075-89e6-fd04ea189502', N'Admin', N'MALE', CAST(N'1995-08-15' AS Date), N'admin@gmail.com', N'0912345678', N'123 Lê Lợi, Quận 1, TP.HCM', N'https://example.com/default-avatar.png', CAST(N'2025-06-16T21:09:55.797' AS DateTime), NULL, N'$2a$10$AGuYff.tFD1Xei0tYvqLGuLLNrTxOZU5GKwxesDqrXNu8vk518ttq', 1)
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__email_ve__CA90DA7AAE257E29]    Script Date: 6/27/2025 3:55:19 PM ******/
ALTER TABLE [dbo].[email_verification_token] ADD UNIQUE NONCLUSTERED 
(
	[token] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ__password__B9BE370E804BC2A6]    Script Date: 6/27/2025 3:55:19 PM ******/
ALTER TABLE [dbo].[password_reset_token] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E6164EB082385]    Script Date: 6/27/2025 3:55:19 PM ******/
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
ALTER TABLE [dbo].[clinical_Result] ADD  DEFAULT (newid()) FOR [result_id]
GO
ALTER TABLE [dbo].[clinical_Result] ADD  DEFAULT (getdate()) FOR [created_at]
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
ALTER TABLE [dbo].[patient_Visit] ADD  DEFAULT (newid()) FOR [visit_id]
GO
ALTER TABLE [dbo].[patient_Visit] ADD  DEFAULT (getdate()) FOR [created_at]
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
ALTER TABLE [dbo].[treatment_Phase] ADD  DEFAULT (newid()) FOR [phase_id]
GO
ALTER TABLE [dbo].[treatment_Phase] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_Phase_Status] ADD  DEFAULT (newid()) FOR [status_id]
GO
ALTER TABLE [dbo].[treatment_Plan] ADD  DEFAULT (newid()) FOR [plan_id]
GO
ALTER TABLE [dbo].[treatment_Plan] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_Plan_Template] ADD  DEFAULT (newid()) FOR [template_id]
GO
ALTER TABLE [dbo].[treatment_Plan_Template] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_Result] ADD  DEFAULT (newid()) FOR [result_id]
GO
ALTER TABLE [dbo].[treatment_Result] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[treatment_Schedule] ADD  DEFAULT (newid()) FOR [schedule_id]
GO
ALTER TABLE [dbo].[treatment_Schedule] ADD  DEFAULT (getdate()) FOR [created_at]
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
ALTER TABLE [dbo].[clinical_Result]  WITH CHECK ADD FOREIGN KEY([visit_id])
REFERENCES [dbo].[patient_Visit] ([visit_id])
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
REFERENCES [dbo].[treatment_Schedule] ([schedule_id])
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
ALTER TABLE [dbo].[patient_Visit]  WITH CHECK ADD FOREIGN KEY([doctor_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[patient_Visit]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
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
ALTER TABLE [dbo].[treatment_Phase_Status]  WITH CHECK ADD FOREIGN KEY([phase_id])
REFERENCES [dbo].[treatment_Phase] ([phase_id])
GO
ALTER TABLE [dbo].[treatment_Phase_Status]  WITH CHECK ADD FOREIGN KEY([treatment_plan_id])
REFERENCES [dbo].[treatment_Plan] ([plan_id])
GO
ALTER TABLE [dbo].[treatment_Plan]  WITH CHECK ADD FOREIGN KEY([template_id])
REFERENCES [dbo].[treatment_Plan_Template] ([template_id])
GO
ALTER TABLE [dbo].[treatment_Plan]  WITH CHECK ADD FOREIGN KEY([visit_id])
REFERENCES [dbo].[patient_Visit] ([visit_id])
GO
ALTER TABLE [dbo].[treatment_Result]  WITH CHECK ADD FOREIGN KEY([schedule_step_id])
REFERENCES [dbo].[treatment_Schedule] ([schedule_id])
GO
ALTER TABLE [dbo].[treatment_Schedule]  WITH CHECK ADD FOREIGN KEY([plan_id])
REFERENCES [dbo].[treatment_Plan] ([plan_id])
GO
ALTER TABLE [dbo].[appointment]  WITH CHECK ADD CHECK  (([check_in_status]='Missed' OR [check_in_status]='CheckedIn' OR [check_in_status]='Pending'))
GO
ALTER TABLE [dbo].[appointmentProposal]  WITH CHECK ADD CHECK  (([status]='Expired' OR [status]='Rejected' OR [status]='Confirmed' OR [status]='Pending'))
GO
ALTER TABLE [dbo].[blog]  WITH CHECK ADD CHECK  (([status]='archived' OR [status]='published' OR [status]='draft'))
GO
ALTER TABLE [dbo].[clinical_Result]  WITH CHECK ADD CHECK  (([result_type]='Other' OR [result_type]='Text' OR [result_type]='Image' OR [result_type]='Lab'))
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
ALTER TABLE [dbo].[treatment_Phase_Status]  WITH CHECK ADD CHECK  (([status]='Cancelled' OR [status]='Completed' OR [status]='In Progress' OR [status]='Pending'))
GO
ALTER TABLE [dbo].[treatment_Result]  WITH CHECK ADD CHECK  (([status]='pending' OR [status]='failed' OR [status]='success'))
GO
ALTER TABLE [dbo].[treatment_Schedule]  WITH CHECK ADD CHECK  (([status]='Cancelled' OR [status]='Completed' OR [status]='Scheduled'))
GO
ALTER TABLE [dbo].[treatment_Schedule]  WITH CHECK ADD CHECK  (([treatment_type]='Lab Test' OR [treatment_type]='Ultrasound' OR [treatment_type]='IVF' OR [treatment_type]='IUI' OR [treatment_type]='Injection'))
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD CHECK  (([gender]='OTHER' OR [gender]='FEMALE' OR [gender]='MALE'))
GO
USE [master]
GO
ALTER DATABASE [Infertility_Treatment] SET  READ_WRITE 
GO
