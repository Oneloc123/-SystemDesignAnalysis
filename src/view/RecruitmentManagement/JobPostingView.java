package view.RecruitmentManagement;

import controller.recruitmentManagement.JobPostingController;
import model.Recruitment.JobPosting;
import view.View;

import java.util.List;

import static controller.MainController.printList;

public class JobPostingView extends View {
    private JobPostingController controller;

    public JobPostingView(JobPostingController controller) {
        this.controller = controller;
    }

    @Override
    public void show() throws Exception {
        loop:
        while (true) {
            printList(new String[]{"Tạo bài đăng tuyển dụng", "Chỉnh sửa bài đăng", "Xóa bài đăng", "Xem danh sách bài đăng"});
            printAddress();
            handleInput();
            if (question.equals("0")) {
                System.out.println("Thoát thành công");
                break loop;
            }
            controller.executeCommand(question);
        }
    }

    public void enterJobPostingDetails() throws Exception {
        showMessage("--- NHẬP THÔNG TIN BÀI ĐĂNG TUYỂN DỤNG ---");
        String title = handleParam("Tiêu đề");
        String description = handleParam("Mô tả công việc");
        String requirement = handleParam("Yêu cầu");
        int quantity = Integer.parseInt(handleParam("Số lượng tuyển"));
        double salary = handleDouleParam("Mức lương");
        java.sql.Date deadline = handleDate("Ngày hết hạn (vd: 2026-12-31)");
        controller.createJobPosting(title, description, requirement, quantity, salary, deadline);
    }

    public void displayJobPostingList(List<JobPosting> postings) throws Exception {
        if (postings.isEmpty()) {
            showError("Không có bài đăng nào.");
            return;
        }
        showMessage("=== DANH SÁCH BÀI ĐĂNG TUYỂN DỤNG ===");
        int index = 1;
        for (JobPosting jp : postings) {
            showMessage(index + ". " + jp.getTitle()
                    + " - Lương: " + jp.getSalary()
                    + " - Hạn: " + jp.getDeadline()
                    + " - Trạng thái: " + jp.getStatus());
            index++;
        }
    }

    public void displayJobPostingDetail(JobPosting jp) {
        showMessage("==============================");
        showMessage("ID            : " + jp.getJobId());
        showMessage("Tiêu đề       : " + jp.getTitle());
        showMessage("Mô tả         : " + jp.getDescription());
        showMessage("Yêu cầu       : " + jp.getRequirement());
        showMessage("Số lượng      : " + jp.getQuantity());
        showMessage("Mức lương     : " + jp.getSalary());
        showMessage("Ngày hết hạn  : " + jp.getDeadline());
        showMessage("Trạng thái    : " + jp.getStatus());
        showMessage("Ngày tạo      : " + jp.getCreatedDate());
        showMessage("==============================");
    }

    public void selectPostingForEdit(List<JobPosting> postings) throws Exception {
        if (postings.isEmpty()) {
            showError("Không có bài đăng nào.");
            return;
        }
        displayJobPostingList(postings);
        int choice = Integer.parseInt(handleParam("Chọn bài đăng để chỉnh sửa (nhập số thứ tự): "));
        JobPosting selected = postings.get(choice - 1);
        displayJobPostingDetail(selected);
        showMessage("--- NHẬP THÔNG TIN MỚI ---");
        String title = handleParam("Tiêu đề mới (" + selected.getTitle() + ")");
        if (title.trim().isEmpty()) title = selected.getTitle();
        String description = handleParam("Mô tả mới");
        if (description.trim().isEmpty()) description = selected.getDescription();
        String requirement = handleParam("Yêu cầu mới");
        if (requirement.trim().isEmpty()) requirement = selected.getRequirement();
        String qtyStr = handleParam("Số lượng mới (" + selected.getQuantity() + ")");
        int quantity = qtyStr.trim().isEmpty() ? selected.getQuantity() : Integer.parseInt(qtyStr);
        String salaryStr = handleParam("Mức lương mới (" + selected.getSalary() + ")");
        double salary = salaryStr.trim().isEmpty() ? selected.getSalary() : Double.parseDouble(salaryStr);
        controller.updateJobPosting(selected.getJobId(), title, description, requirement, quantity, salary);
    }

    public void selectPostingForDelete(List<JobPosting> postings) throws Exception {
        if (postings.isEmpty()) {
            showError("Không có bài đăng nào.");
            return;
        }
        displayJobPostingList(postings);
        int choice = Integer.parseInt(handleParam("Chọn bài đăng để xóa (nhập số thứ tự): "));
        JobPosting selected = postings.get(choice - 1);
        String confirm = handleParam("Xác nhận xóa \"" + selected.getTitle() + "\"? (Y/N): ");
        if (confirm.toUpperCase().equals("Y")) {
            controller.deleteJobPosting(selected.getJobId());
        }
    }

    @Override
    public void showError(String error) {
        super.showError(error);
    }

    public void showMessage(String message) {
        super.showMessage(message);
    }
}
