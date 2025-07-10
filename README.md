# 🌟 FertiCare - Fertility Treatment Management System

> **Hệ thống quản lý điều trị vô sinh toàn diện**  
> Project SWP391 - Comprehensive fertility treatment support platform

---

## 📋 **Tổng quan dự án**

FertiCare là hệ thống quản lý điều trị vô sinh hiện đại, hỗ trợ toàn bộ quy trình từ đăng ký khám, chẩn đoán, lập phác đồ điều trị đến theo dõi kết quả IUI/IVF.

### ⚡ **Công nghệ sử dụng**

**Frontend:**

- ⚛️ React 18 + Vite
- 🎨 Modern CSS & Responsive Design
- 📱 Mobile-friendly Interface

**Backend:**

- ☕ Spring Boot 3.2.5
- 🔐 Spring Security + JWT
- 📊 SQL Server Database
- 📨 Email Integration (Gmail SMTP)
- 🔄 Scheduled Tasks & Workflow Automation

---

## 🏗️ **Cấu trúc dự án**

```
Fertility/
├── src/                          # 🖥️ Frontend (React + Vite)
│   ├── components/               # UI Components
│   ├── pages/                    # Application Pages
│   ├── api/                      # API Integration
│   └── ...
├── ferticare-back/              # 🔧 Backend (Spring Boot)
│   ├── src/main/java/           # Java Source Code
│   │   └── com/ferticare/ferticareback/
│   │       ├── projectmanagementservice/
│   │       │   ├── treatmentmanagement/    # 🩺 Treatment Workflow
│   │       │   ├── usermanagement/         # 👥 User Management
│   │       │   ├── blogmanagement/         # 📝 Blog System
│   │       │   ├── servicemanagement/      # 🏥 Service Management
│   │       │   └── notificationmanagement/ # 🔔 Notifications
│   │       └── common/                     # Shared Components
│   ├── src/main/resources/      # Configuration & Database
│   └── database/                # 🗄️ SQL Scripts
├── public/                      # Static Assets
└── docs/                        # 📚 Documentation
```

---

## 🌟 **Tính năng chính**

### 👤 **Quản lý người dùng**

- ✅ Đăng ký/Đăng nhập với Google OAuth
- ✅ Phân quyền: Admin, Manager, Doctor, Patient
- ✅ Quản lý hồ sơ cá nhân

### 🩺 **Quản lý điều trị**

- ✅ **14-step Treatment Workflow** từ đăng ký đến hoàn thành
- ✅ Phác đồ điều trị IUI & IVF với templates
- ✅ Kết quả khám lâm sàng chi tiết
- ✅ Milestone tracking với deadline management
- ✅ Conflict prevention cho scheduling

### 📊 **Clinical Results System**

- ✅ Quản lý kết quả xét nghiệm máu, siêu âm
- ✅ Theo dõi hormone levels (FSH, LH, E2, AMH...)
- ✅ Đính kèm file ảnh, PDF
- ✅ Statistics & reporting

### 🔔 **Hệ thống thông báo**

- ✅ Email automation với 4 loại notification
- ✅ Reminder system với grace periods
- ✅ Timeout management & auto-cancellation

### 📝 **Blog & Content Management**

- ✅ Quản lý bài viết về vô sinh
- ✅ Comment system với moderation
- ✅ Image upload & management

---

## 🚀 **Cài đặt & Chạy dự án**

### **1. Prerequisites**

```bash
- Java 21+
- Node.js 18+
- SQL Server
- Git
```

### **2. Clone Repository**

```bash
git clone [repository-url]
cd Fertility
```

### **3. Setup Database**

```bash
# Import database schema
sqlcmd -S [server] -d fertix -i ferticare-back/database/database_schema.sql
```

### **4. Backend Setup**

```bash
cd ferticare-back

# Configure database connection in application.properties
# Update: spring.datasource.url, username, password

# Run backend
./mvnw spring-boot:run
```

### **5. Frontend Setup**

```bash
# Install dependencies
npm install

# Run development server
npm run dev
```

### **6. Access Application**

- 🖥️ **Frontend**: http://localhost:5173
- 🔧 **Backend API**: http://localhost:8080
- 📊 **Swagger UI**: http://localhost:8080/swagger-ui.html
- 🗄️ **H2 Console**: http://localhost:8080/h2-console

---

## 📚 **Tài liệu tham khảo**

- 📖 [API Documentation](FertiCare-Backend-API-Documentation.md)
- 🔧 [Development Guide](DEV-GUIDE.md)
- 📧 [Email Troubleshooting](MAIL-TROUBLESHOOTING-GUIDE.md)
- 🔄 [Migration Summary](ferticare-back/MIGRATION_SUMMARY.md)

---

## 🏥 **Workflow Logic**

1. **Registration** → Appointment Booking
2. **Clinical Examination** → Results Recording
3. **Treatment Plan Creation** → Template-based Planning
4. **Phase Execution** → Sequential Milestone Tracking
5. **Notification System** → Automated Reminders
6. **Completion** → Final Results & Reports

---

## 👥 **Team Members**

- **Frontend Development**: React.js Team
- **Backend Development**: Spring Boot Team
- **Database Design**: SQL Server Team
- **DevOps & Deployment**: Infrastructure Team

---

## 📄 **License**

This project is developed for educational purposes as part of SWP391 course.

---

**🌟 Built with ❤️ for better fertility treatment management**
