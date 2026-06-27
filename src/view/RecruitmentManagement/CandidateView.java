package view.RecruitmentManagement;

import controller.recruitmentManagement.CandidateController;
import view.View;

public class CandidateView extends View {
    private CandidateController controller;

    public CandidateView(CandidateController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        showMessage("=== NỘP HỒ SƠ ỨNG TUYỂN ===");
        showMessage("Vui lòng nhập thông tin của bạn để nộp hồ sơ ứng tuyển.\n");

        String fullName = handleParam("Họ và tên");
        String gender = handleParam("Giới tính (Nam/Nữ/Khác)");
        String birthdayStr = handleParam("Ngày sinh (vd: 1998-05-15)");
        java.sql.Date birthday = null;
        try {
            birthday = java.sql.Date.valueOf(birthdayStr);
        } catch (IllegalArgumentException e) {
            showError("Ngày sinh không hợp lệ, bỏ qua.");
        }
        String phone = handleParam("Số điện thoại");
        String email = handleParam("Email");
        String address = handleParam("Địa chỉ");
        String education = handleParam("Trình độ học vấn");
        String experience = handleParam("Kinh nghiệm làm việc");
        String cvFile = handleParam("Đường dẫn file CV");

        controller.submitCV(fullName, gender, birthday, phone, email, address, education, experience, cvFile);
    }

    public void showSuccessMessage(int candidateId) {
        showMessage("==========================================");
        showMessage("  NỘP HỒ SƠ THÀNH CÔNG!");
        showMessage("  Mã số hồ sơ của bạn là: " + candidateId);
        showMessage("  Chúng tôi sẽ liên hệ với bạn qua email hoặc số điện thoại.");
        showMessage("  Cảm ơn bạn đã quan tâm đến vị trí tuyển dụng!");
        showMessage("==========================================");
    }

    @Override
    public void showError(String error) {
        super.showError(error);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }
}
