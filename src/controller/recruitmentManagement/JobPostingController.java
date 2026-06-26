package controller.recruitmentManagement;

import controller.MainController;
import dao.recruitment.JobPostingDAO;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.JobPosting;
import view.RecruitmentManagement.JobPostingView;

import java.sql.Date;
import java.util.List;

public class JobPostingController {
    private JobPostingView view;
    private JobPostingDAO jobPostingDAO;
    private AddressEnum address = AddressEnum.JobPostingManagement;

    public JobPostingController() {
        this.view = new JobPostingView(this);
        this.jobPostingDAO = new JobPostingDAO();
    }

    public boolean navigate() throws Exception {
        if (MainController.currentUser.getRole() != RoleEnum.HR
                && MainController.currentUser.getRole() != RoleEnum.ADMIN) {
            return false;
        }
        MainController.addresses.add(address);
        view.show();
        MainController.addresses.remove(address);
        return true;
    }

    public void executeCommand(String command) throws Exception {
        switch (command) {
            case "1":
                view.enterJobPostingDetails();
                break;
            case "2":
                List<JobPosting> allPostings = jobPostingDAO.findAll();
                view.selectPostingForEdit(allPostings);
                break;
            case "3":
                List<JobPosting> postings = jobPostingDAO.findAll();
                view.selectPostingForDelete(postings);
                break;
            case "4":
                List<JobPosting> list = jobPostingDAO.findAll();
                view.displayJobPostingList(list);
                break;
            default:
                view.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void createJobPosting(String title, String description, String requirement,
                                  int quantity, double salary, Date deadline) throws Exception {
        JobPosting jp = new JobPosting();
        jp.setTitle(title);
        jp.setDescription(description);
        jp.setRequirement(requirement);
        jp.setQuantity(quantity);
        jp.setSalary(salary);
        jp.setDeadline(deadline);
        jp.setCreatedBy(MainController.currentUser);

        if (!jp.isValid()) {
            view.showError("Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
            return;
        }

        if (jobPostingDAO.save(jp)) {
            view.showMessage("Tạo bài đăng tuyển dụng thành công! (ID: " + jp.getJobId() + ")");
            view.displayJobPostingDetail(jp);
        } else {
            view.showError("Lưu bài đăng thất bại.");
        }
    }

    public void updateJobPosting(int jobId, String title, String description,
                                  String requirement, int quantity, double salary) throws Exception {
        JobPosting jp = jobPostingDAO.findById(jobId);
        if (jp == null) {
            view.showError("Không tìm thấy bài đăng.");
            return;
        }
        jp.setTitle(title);
        jp.setDescription(description);
        jp.setRequirement(requirement);
        jp.setQuantity(quantity);
        jp.setSalary(salary);

        if (jobPostingDAO.update(jp)) {
            view.showMessage("Cập nhật bài đăng thành công.");
            view.displayJobPostingDetail(jp);
        } else {
            view.showError("Cập nhật thất bại.");
        }
    }

    public void deleteJobPosting(int jobId) throws Exception {
        if (jobPostingDAO.delete(jobId)) {
            view.showMessage("Xóa bài đăng thành công.");
        } else {
            view.showError("Xóa bài đăng thất bại.");
        }
    }
}
