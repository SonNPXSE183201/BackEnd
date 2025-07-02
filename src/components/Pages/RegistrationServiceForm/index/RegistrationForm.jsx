import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import axiosClient from "../../../../api/axiosClient";
import "./RegistrationForm.css";

const RegistrationForm = () => {
  const [formData, setFormData] = useState({
    serviceId: "",
    doctorOption: "auto",
    doctorId: "",
    appointmentDate: "",
    appointmentTime: "",
    notes: "",
    agreePolicy: false,
  });

  const [services, setServices] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [availableDates, setAvailableDates] = useState([]);
  const [availableSlots, setAvailableSlots] = useState([]);
  const [showSuccess, setShowSuccess] = useState(false);
  const [registerInfo, setRegisterInfo] = useState(null);
  const [connectionError, setConnectionError] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [selectedDate, setSelectedDate] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [loadingStates, setLoadingStates] = useState({
    services: false,
    doctors: false,
    dates: false,
    slots: false,
  });

  // Kiểm tra kết nối API khi component mount
  useEffect(() => {
    const checkAPIConnection = async () => {
      try {
        setLoadingStates((prev) => ({ ...prev, services: true }));
        await axiosClient.get("/api/services");
        setConnectionError(false);
      } catch (error) {
        console.error("❌ [RegistrationForm] API Connection failed:", error);
        setConnectionError(true);
      } finally {
        setLoadingStates((prev) => ({ ...prev, services: false }));
      }
    };
    checkAPIConnection();
  }, []);

  // Lấy danh sách dịch vụ
  useEffect(() => {
    const fetchServices = async () => {
      try {
        setLoadingStates((prev) => ({ ...prev, services: true }));
        const res = await axiosClient.get("/api/services");
        setServices(res.data);
        setConnectionError(false);
      } catch (error) {
        console.error("❌ [RegistrationForm] Failed to load services:", error);
        setConnectionError(true);
      } finally {
        setLoadingStates((prev) => ({ ...prev, services: false }));
      }
    };
    fetchServices();
  }, []);

  // Khi chọn dịch vụ, lấy danh sách bác sĩ
  useEffect(() => {
    if (!formData.serviceId) return;

    const fetchDoctors = async () => {
      try {
        setLoadingStates((prev) => ({ ...prev, doctors: true }));
        const res = await axiosClient.get(
          `/api/service-request/available-doctors/${formData.serviceId}`
        );
        setDoctors(res.data);
      } catch (error) {
        console.error("❌ Failed to load doctors:", error);
      } finally {
        setLoadingStates((prev) => ({ ...prev, doctors: false }));
      }
    };

    fetchDoctors();

    // Reset các trường liên quan
    setFormData((prev) => ({
      ...prev,
      doctorId: "",
      appointmentDate: "",
      appointmentTime: "",
    }));
    setAvailableDates([]);
    setAvailableSlots([]);
    setSelectedDate(null);
  }, [formData.serviceId]);

  // Nếu chọn auto thì load lịch bác sĩ đầu tiên
  useEffect(() => {
    const fetchAutoDoctorSchedule = async () => {
      if (
        formData.doctorOption === "auto" &&
        doctors.length > 0 &&
        !formData.appointmentDate
      ) {
        const autoDoctorId = doctors[0].id;
        setFormData((prev) => ({ ...prev, doctorId: autoDoctorId }));

        try {
          setLoadingStates((prev) => ({ ...prev, dates: true }));
          const res = await axiosClient.get(
            `/api/service-request/available-dates/${autoDoctorId}`
          );
          const dateList = res.data.map((dateStr) => ({
            date: dateStr,
            slots: [],
          }));
          setAvailableDates(dateList);
          setAvailableSlots([]);
        } catch (err) {
          console.error("❌ Lỗi lấy lịch bác sĩ auto:", err);
        } finally {
          setLoadingStates((prev) => ({ ...prev, dates: false }));
        }
      }
    };
    fetchAutoDoctorSchedule();
  }, [formData.doctorOption, doctors, formData.appointmentDate]);

  // Khi chọn bác sĩ thủ công
  const handleDoctorChange = async (e) => {
    const doctorId = e.target.value;
    setFormData((prev) => ({
      ...prev,
      doctorId,
      appointmentDate: "",
      appointmentTime: "",
    }));
    setSelectedDate(null);
    setAvailableSlots([]);

    if (!doctorId) return;

    try {
      setLoadingStates((prev) => ({ ...prev, dates: true }));
      const res = await axiosClient.get(
        `/api/service-request/available-dates/${doctorId}`
      );
      const dateList = res.data.map((dateStr) => ({
        date: dateStr,
        slots: [],
      }));
      setAvailableDates(dateList);
    } catch (err) {
      console.error("❌ Lỗi lấy danh sách ngày:", err);
      setAvailableDates([]);
    } finally {
      setLoadingStates((prev) => ({ ...prev, dates: false }));
    }
  };

  // Xử lý khi chọn ngày từ DatePicker
  const handleDatePickerChange = async (date) => {
    setSelectedDate(date);

    if (!date || !formData.doctorId) return;

    // Format date to YYYY-MM-DD
    const formattedDate = date.toISOString().split("T")[0];

    setFormData((prev) => ({
      ...prev,
      appointmentDate: formattedDate,
      appointmentTime: "",
    }));

    try {
      setLoadingStates((prev) => ({ ...prev, slots: true }));
      const res = await axiosClient.get(
        `/api/service-request/doctor-available-times/${formData.doctorId}?date=${formattedDate}`
      );
      const slots = res.data.map((item) => item.time);
      setAvailableSlots(slots);
    } catch (err) {
      console.error("❌ Lỗi lấy slots cho ngày:", err);
      setAvailableSlots([]);
    } finally {
      setLoadingStates((prev) => ({ ...prev, slots: false }));
    }
  };

  const handleSlotChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      appointmentTime: e.target.value,
    }));
  };

  const handleChange = (e) => {
    const { name, type, value, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleFieldBlur = (fieldName) => {
    // Clear any existing error for this field
    setFormErrors((prev) => ({
      ...prev,
      [fieldName]: "",
    }));
  };

  // Tạo danh sách các ngày có thể chọn
  const getAvailableDatesArray = () => {
    if (!Array.isArray(availableDates)) return [];
    return availableDates.map((d) => new Date(d.date));
  };

  // Kiểm tra ngày có available không
  const isDateAvailable = (date) => {
    if (!Array.isArray(availableDates)) return false;
    const dateStr = date.toISOString().split("T")[0];
    return availableDates.some((d) => d.date === dateStr);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validation
    if (!formData.agreePolicy) {
      alert("⚠️ Bạn cần đồng ý với chính sách và điều khoản sử dụng.");
      return;
    }
    if (!formData.serviceId) {
      alert("⚠️ Vui lòng chọn dịch vụ điều trị.");
      return;
    }
    if (formData.doctorOption === "manual" && !formData.doctorId) {
      alert("⚠️ Vui lòng chọn bác sĩ.");
      return;
    }
    if (!formData.appointmentDate || !formData.appointmentTime) {
      alert("⚠️ Vui lòng chọn ngày và giờ hẹn.");
      return;
    }

    const appointmentTime = `${formData.appointmentDate}T${formData.appointmentTime}:00`;

    const dataToSubmit = {
      serviceId: formData.serviceId,
      doctorId: formData.doctorOption === "manual" ? formData.doctorId : null,
      doctorSelection: formData.doctorOption,
      note: formData.notes,
      appointmentTime,
    };

    try {
      setIsLoading(true);
      const res = await axiosClient.post("/api/service-request", dataToSubmit);
      setRegisterInfo(res.data);
      setShowSuccess(true);

      // Reset form
      setFormData({
        serviceId: "",
        doctorOption: "auto",
        doctorId: "",
        appointmentDate: "",
        appointmentTime: "",
        notes: "",
        agreePolicy: false,
      });
      setSelectedDate(null);
      setAvailableDates([]);
      setAvailableSlots([]);
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || err.response?.data || err.message;
      alert("❌ Đăng ký thất bại: " + errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const formatDateTime = (dateTime) => {
    if (!dateTime) return "";
    const date = new Date(dateTime);
    return date.toLocaleString("vi-VN", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <>
      <div id="date-picker-portal"></div>
      <main className="registration-form-container">
        {connectionError && (
          <div className="error-banner">
            <strong>⚠️ Lỗi kết nối:</strong> Không thể kết nối tới server. Vui
            lòng kiểm tra:
            <ul style={{ margin: "8px 0", paddingLeft: "20px" }}>
              <li>Backend có đang chạy trên port 8080 không</li>
              <li>
                Chạy lệnh: <code>npm run backend</code>
              </li>
              <li>
                Hoặc chạy: <code>npm start</code> để khởi động cả frontend và
                backend
              </li>
            </ul>
          </div>
        )}

        <form onSubmit={handleSubmit} className="registration-form">
          <h1>🏥 ĐĂNG KÝ DỊCH VỤ ĐIỀU TRỊ</h1>

          {/* Dịch vụ */}
          <section className="section">
            <h2 className="section-title">1. Chọn Dịch Vụ Điều Trị</h2>
            {loadingStates.services ? (
              <div style={{ textAlign: "center", padding: "20px" }}>
                <div>⏳ Đang tải danh sách dịch vụ...</div>
              </div>
            ) : (
              <div className="radio-group">
                {services.map((service) => (
                  <label key={service.id}>
                    <input
                      type="radio"
                      name="serviceId"
                      value={service.id}
                      checked={formData.serviceId === service.id}
                      onChange={handleChange}
                      disabled={isLoading}
                    />
                    <span>{service.name}</span>
                  </label>
                ))}
              </div>
            )}
          </section>

          {/* Bác sĩ */}
          {formData.serviceId && (
            <section className="section">
              <h2 className="section-title">2. Chọn Bác Sĩ</h2>

              <div className="doctor-option-group">
                <label className="doctor-option-label">
                  <input
                    type="radio"
                    name="doctorOption"
                    value="auto"
                    checked={formData.doctorOption === "auto"}
                    onChange={handleChange}
                    disabled={isLoading}
                  />
                  <span className="option-text">
                    🤖 Hệ thống tự động chọn bác sĩ phù hợp
                  </span>
                </label>
                <label className="doctor-option-label">
                  <input
                    type="radio"
                    name="doctorOption"
                    value="manual"
                    checked={formData.doctorOption === "manual"}
                    onChange={handleChange}
                    disabled={isLoading}
                  />
                  <span className="option-text">
                    👩‍⚕️ Tôi muốn tự chọn bác sĩ
                  </span>
                </label>
              </div>

              {formData.doctorOption === "manual" && (
                <div className="doctor-selection-container">
                  <div className="input-group">
                    <label className="input-label">Chọn bác sĩ:</label>
                    {loadingStates.doctors ? (
                      <div style={{ textAlign: "center", padding: "20px" }}>
                        <div>⏳ Đang tải danh sách bác sĩ...</div>
                      </div>
                    ) : (
                      <select
                        name="doctorId"
                        value={formData.doctorId}
                        onChange={handleDoctorChange}
                        className="input-field"
                        required
                        disabled={isLoading}
                      >
                        <option value="">-- Vui lòng chọn bác sĩ --</option>
                        {doctors.map((doc) => (
                          <option key={doc.id} value={doc.id}>
                            👨‍⚕️ {doc.name} -{" "}
                            {doc.specialization || "Bác sĩ chuyên khoa"}
                          </option>
                        ))}
                      </select>
                    )}
                  </div>
                </div>
              )}

              {((formData.doctorOption === "manual" && formData.doctorId) ||
                formData.doctorOption === "auto") && (
                <div className="appointment-scheduling">
                  <div className="scheduling-header">
                    <h3 className="scheduling-title">📅 Đặt Lịch Hẹn</h3>
                    <p className="scheduling-subtitle">
                      Chọn ngày và giờ phù hợp với lịch trình của bạn
                    </p>
                  </div>

                  {loadingStates.dates ? (
                    <div style={{ textAlign: "center", padding: "30px" }}>
                      <div>⏳ Đang tải lịch khám...</div>
                    </div>
                  ) : availableDates.length > 0 ? (
                    <div className="datetime-container">
                      <div className="date-picker-container">
                        <label className="input-label">📅 Chọn ngày hẹn:</label>
                        <DatePicker
                          id="appointmentDate"
                          selected={selectedDate}
                          onChange={handleDatePickerChange}
                          includeDates={getAvailableDatesArray()}
                          dateFormat="dd/MM/yyyy"
                          placeholderText="🗓️ Nhấn để chọn ngày hẹn..."
                          className="input-field date-picker-input"
                          calendarClassName="custom-calendar"
                          dayClassName={(date) =>
                            isDateAvailable(date)
                              ? "available-date"
                              : "unavailable-date"
                          }
                          minDate={new Date()}
                          showPopperArrow={false}
                          required
                          autoComplete="off"
                          withPortal={true}
                          shouldCloseOnSelect={true}
                          isClearable={false}
                          showYearDropdown={false}
                          showMonthDropdown={false}
                          disabled={isLoading}
                          onBlur={() => handleFieldBlur("appointmentDate")}
                          aria-describedby={
                            formErrors.appointmentDate
                              ? "appointmentDate-error"
                              : undefined
                          }
                          portalId="date-picker-portal"
                        />
                      </div>

                      {selectedDate && (
                        <div className="time-picker-container">
                          <label className="input-label">
                            🕐 Chọn giờ hẹn:
                          </label>
                          {loadingStates.slots ? (
                            <div
                              style={{ textAlign: "center", padding: "20px" }}
                            >
                              <div>⏳ Đang tải khung giờ...</div>
                            </div>
                          ) : availableSlots.length > 0 ? (
                            <div className="time-slots-grid">
                              {availableSlots.map((slot, i) => (
                                <label
                                  key={i}
                                  className={`time-slot ${
                                    formData.appointmentTime === slot
                                      ? "selected"
                                      : ""
                                  }`}
                                >
                                  <input
                                    type="radio"
                                    name="appointmentTime"
                                    value={slot}
                                    checked={formData.appointmentTime === slot}
                                    onChange={handleSlotChange}
                                    required
                                    disabled={isLoading}
                                  />
                                  <span className="time-slot-text">
                                    🕐 {slot}
                                  </span>
                                </label>
                              ))}
                            </div>
                          ) : (
                            <div className="no-dates-available">
                              <p>
                                😔 Không có khung giờ trống cho ngày này. Vui
                                lòng chọn ngày khác.
                              </p>
                            </div>
                          )}
                        </div>
                      )}
                    </div>
                  ) : (
                    <div className="no-dates-available">
                      <p>
                        😔 Hiện tại bác sĩ chưa có lịch trống. Vui lòng thử lại
                        sau hoặc chọn bác sĩ khác.
                      </p>
                    </div>
                  )}
                </div>
              )}
            </section>
          )}

          {/* Ghi chú + Chính sách */}
          <section className="section">
            <h2 className="section-title">3. Thông Tin Bổ Sung</h2>
            <div className="notes-container">
              <label className="input-label">📝 Ghi chú (tùy chọn):</label>
              <textarea
                name="notes"
                value={formData.notes}
                onChange={handleChange}
                placeholder="Ví dụ: Triệu chứng cụ thể, tiền sử bệnh, yêu cầu đặc biệt, thời gian thuận tiện..."
                className="textarea-field"
                rows="4"
                disabled={isLoading}
              />
            </div>

            <label className="policy-confirmation">
              <input
                type="checkbox"
                name="agreePolicy"
                checked={formData.agreePolicy}
                onChange={handleChange}
                required
                disabled={isLoading}
              />
              <span className="policy-text">
                🔒 Tôi xác nhận đã đọc và đồng ý với{" "}
                <strong>chính sách bảo mật thông tin</strong> và{" "}
                <strong>điều khoản sử dụng dịch vụ</strong> của phòng khám
              </span>
            </label>
          </section>

          <button
            type="submit"
            className="submit-button"
            disabled={!formData.agreePolicy || isLoading}
          >
            {isLoading ? "⏳ Đang xử lý..." : "🚀 Hoàn Tất Đăng Ký"}
          </button>
        </form>

        {/* Success Modal */}
        {showSuccess && registerInfo && (
          <div
            className="success-modal-overlay"
            onClick={() => setShowSuccess(false)}
          >
            <div className="success-modal" onClick={(e) => e.stopPropagation()}>
              <div className="success-header">
                <h2>🎉 Đăng Ký Thành Công!</h2>
                <p>Thông tin lịch hẹn của bạn đã được xác nhận</p>
              </div>
              <div className="success-content">
                <div className="info-item">
                  <span className="info-label">📋 Mã đơn hẹn:</span>
                  <span className="info-value">#{registerInfo.id}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">📅 Thời gian hẹn:</span>
                  <span className="info-value">
                    {formatDateTime(registerInfo.appointmentTime)}
                  </span>
                </div>
                <div className="info-item">
                  <span className="info-label">🏥 Dịch vụ:</span>
                  <span className="info-value">
                    {registerInfo.service?.name}
                  </span>
                </div>
                <div className="info-item">
                  <span className="info-label">👨‍⚕️ Bác sĩ:</span>
                  <span className="info-value">
                    {registerInfo.doctor?.fullName || "Sẽ được thông báo sau"}
                  </span>
                </div>
                {registerInfo.note && (
                  <div className="info-item">
                    <span className="info-label">📝 Ghi chú:</span>
                    <span className="info-value">{registerInfo.note}</span>
                  </div>
                )}
                <div
                  style={{
                    marginTop: "20px",
                    padding: "16px",
                    backgroundColor: "#f0f9ff",
                    borderRadius: "8px",
                    textAlign: "center",
                  }}
                >
                  <p style={{ margin: 0, color: "#0369a1", fontWeight: "500" }}>
                    📱 Phòng khám sẽ liên hệ với bạn để xác nhận lịch hẹn trong
                    vòng 24 giờ
                  </p>
                </div>
              </div>
              <button
                className="success-close-btn"
                onClick={() => setShowSuccess(false)}
              >
                ✅ Đóng
              </button>
            </div>
          </div>
        )}
      </main>
    </>
  );
};

export default RegistrationForm;
