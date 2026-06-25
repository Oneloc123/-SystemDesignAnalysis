package controller.recruimentManagement;

import controller.MainController;
import dao.recruitment.JobPostingDAO;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.Employer;
import model.Recruitment.JobPosting;
import view.RecruitmentManagement.CreatePostRecruimentView;

import java.sql.Date;

public class CreatePostRecruimentController {
    private JobPosting draf;
    private CreatePostRecruimentView cprv;
    private AddressEnum address = AddressEnum.CreatePostRecruitment;
    private JobPostingDAO jobPostingDAO;

    public CreatePostRecruimentController() {
        cprv = new CreatePostRecruimentView(this);
        this.jobPostingDAO = new JobPostingDAO();
    }

    public boolean navigateTo() throws Exception {
        if (MainController.currentUser.getRole() != RoleEnum.EMPLOYER) {
            return false;
        }
        MainController.addresses.add(address);
        cprv.show();
        MainController.addresses.remove(AddressEnum.RecruitmentManagement);
        return true;
    }

    public void processFormOptions(String s, JobPosting j) throws Exception {
        draf = j;
        switch (s) {
            case "1":
                cprv.promptSaveDraft();
                break;
            case "2":
                cprv.promptConfirmSubmit();
                break;
            case "3":
                cprv.displayEditOptions(draf, "bản chỉnh sửa");
                break;
            case "0":
                cprv.showMessage("thoát form");
                break;
            default:
                cprv.showError("Lệnh không hợp lệ");
                cprv.displayFormOptions();
                break;
        }
    }

    public void handleMainMenuCommand(String question) throws Exception {
        switch (question) {
            case "1":
                cprv.enterJobPostingDetails();
                break;
            case "2":
                Employer e = (Employer) MainController.currentUser;
                draf = e.getJobPostingDraf();
                if (draf == null) {
                    cprv.showError("không có bản nháp");
                    break;
                }
                draf.setEmployer(e);
                cprv.displayEditOptions(draf, "bản nháp");
                break;
            default:
                cprv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void handleConfirmSubmit(String s, JobPosting rp) throws Exception {
        if (s.toUpperCase().equals("Y")) {
            if (!rp.checkValid()) {
                cprv.showMessage("Dữ liệu không hợp lệ, vui lòng nhập lại");
                cprv.enterJobPostingDetails();
                return;
            }
            rp.setStatus("OPEN");
            rp.setCreatedDate(new Date(System.currentTimeMillis()));
            if (!jobPostingDAO.save(rp)) {
                cprv.showMessage("lưu database thất bại, vui lòng vào lại lần sau");
                return;
            }
            Employer employer = rp.getEmployer();
            if (employer != null) employer.addPostedJob(rp);
            cprv.showMessage("lưu thành công");
            return;
        }
        if (s.toUpperCase().equals("N")) {
            System.out.println("Huỷ gửi form");
            return;
        }
        cprv.showError("lệnh không hợp lệ");
        cprv.promptConfirmSubmit();
    }

    public void handleSaveDraft(String s, JobPosting rp) throws Exception {
        draf = rp;
        if (s.toUpperCase().equals("Y")) {
            if (!rp.checkValid()) {
                cprv.showMessage("Dữ liệu không hợp lệ, vui lòng nhập lại");
                cprv.enterJobPostingDetails();
                return;
            }
            rp.setStatus("DRAFT");
            if (!jobPostingDAO.saveDraft(rp)) {
                cprv.showMessage("lưu database thất bại, vui lòng vào lại lần sau");
                return;
            }
            cprv.showMessage("lưu nháp thành công");
            return;
        }
        if (s.toUpperCase().equals("N")) {
            System.out.println("Huỷ lưu nháp");
            return;
        }
        cprv.showError("lệnh không hợp lệ");
        cprv.promptSaveDraft(draf);
    }

    public void handleContinueDraft(String s) throws Exception {
        if (s.toUpperCase().equals("Y")) {
            cprv.displayEditOptions(draf, "tiếp tục bản nháp");
            return;
        }
        if (s.toUpperCase().equals("N")) {
            System.out.println("Thoát nháp");
            return;
        }
        cprv.showError("lệnh không hợp lệ");
        cprv.promptContinueDraft();
    }

    public void handleEditDraft(String s) throws Exception {
        switch (s) {
            case "1":
                draf.setTitle(cprv.handleParam("tiêu đề"));
                cprv.displayEditOptions(draf, "bản nháp");
                break;
            case "2":
                draf.setDescription(cprv.handleParam("mô tả"));
                cprv.displayEditOptions(draf, "bản nháp");
                break;
            case "3":
                draf.setRequiment(cprv.handleParam("yêu cầu"));
                cprv.displayEditOptions(draf, "bản nháp");
                break;
            case "4":
                draf.setSalary(cprv.handleDouleParam("Luong"));
                cprv.displayEditOptions(draf, "bản nháp");
                break;
            case "5":
                draf.setDayEnd((cprv.handleDate("ngày hết hạn (vd : 2026-06-18):")));
                cprv.displayEditOptions(draf, "bản nháp");
                break;
            case "6":
                cprv.showMessage("Thoát chỉnh sửa nháp");
                break;
            case "7":
                cprv.promptSaveDraft(draf);
                break;
            case "8":
                cprv.promptConfirmSubmit(draf);
                break;
            default:
                cprv.showError("Lệnh không hợp lệ");
                cprv.displayEditOptions(draf, "bản nháp");
                break;
        }
    }
}