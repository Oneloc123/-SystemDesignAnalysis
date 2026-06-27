package controller.recruitmentManagement;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import view.RecruitmentManagement.RecruitmentManagementView;

public class RecruitmentManagementController {
    private RecruitmentManagementView rmv;
    private AddressEnum address = AddressEnum.RecruitmentManagement;

    public RecruitmentManagementController(){
        rmv = new RecruitmentManagementView(this);
    }

    public boolean navigate() throws Exception {
        // Only HR or Admin can access recruitment management
        if (MainController.currentUser.getRole() != RoleEnum.HR
                && MainController.currentUser.getRole() != RoleEnum.ADMIN) {
            return false;
        }
        MainController.addresses.add(address);
        rmv.show();
        MainController.addresses.remove(AddressEnum.RecruitmentManagement);
        return true;
    }

    public void executeCommand(String question) throws Exception {
        switch(question) {
            case "1":
                functionCreatePost();
                break;
            case "2":
                functionReviewApplication();
                break;
            case "3":
                functionScheduleInterview();
                break;
            case "4":
                functionEvaluateInterview();
                break;
            default:
                rmv.showError("Lệnh không hợp lệ");
                break;
        }
    }

    public void functionCreatePost() throws Exception {
        JobPostingController jpc = new JobPostingController();
        boolean result = jpc.navigate();
        if(!result){
            rmv.showError("Không có quyền truy cập chức năng tạo bài đăng");
        }
    }

    public void functionReviewApplication() throws Exception{
        ApplicationReviewController arc = new ApplicationReviewController();
        boolean result = arc.navigate();
        if(!result){
            rmv.showError("Không có quyền truy cập chức năng xét duyệt hồ sơ");
        }
    }

    public void functionScheduleInterview() throws Exception {
        InterviewScheduleController isc = new InterviewScheduleController();
        boolean result = isc.navigate();
        if(!result){
            rmv.showError("Không có quyền truy cập chức năng lên lịch phỏng vấn");
        }
    }

    public void functionEvaluateInterview() throws Exception {
        InterviewEvaluationController iec = new InterviewEvaluationController();
        boolean result = iec.navigate();
        if(!result){
            rmv.showError("Không có quyền truy cập chức năng đánh giá phỏng vấn");
        }
    }
}
