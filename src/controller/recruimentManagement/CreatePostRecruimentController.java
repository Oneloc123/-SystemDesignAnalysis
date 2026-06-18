package controller.recruimentManagement;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import model.Recruitment.Employer;
import model.Recruitment.JobPosting;
import view.RecruitmentManagement.CreatePostRecruimentView;

public class CreatePostRecruimentController {
    private JobPosting draf;
    private CreatePostRecruimentView cprv;
    private AddressEnum address = AddressEnum.CreatePostRecruitment;
    public CreatePostRecruimentController(){
        cprv = new CreatePostRecruimentView(this);
    }
    public boolean navigateTo() throws Exception {
        // xác thực quyền
        if(!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER)){
            return false;
        }
        // xác thực thành công
        MainController.addresses.add(address);
        cprv.show();
        // khi thoát
        MainController.addresses.remove(AddressEnum.RecruitmentManagement);
        return true;
    }
    public void executeForm(String s,JobPosting j)throws Exception{
        draf = j;
        switch(s) {
            case "1":
                cprv.executeIsSaveDraf();
                break;
            case "2":
                cprv.executeIsConfirm();
                break;
            case "3":
                cprv.handleEdit(draf,"bản chỉnh sửa");
                break;
            case "0":
                cprv.showMessage("thoát form");
                break;
            default:
                cprv.showError("Lệnh không hợp lệ");
                cprv.executeForm();
                break;
        }
    }

    // xử lý menu
    public void excuteCommand(String question) throws Exception {
        switch(question) {
            case "1":
                cprv.handleForm();
                break;
            case "2":
                Employer e = (Employer) MainController.currentUser;
                draf = e.getJobPostingDraf();
                if(draf == null){
                    cprv.showError("không có bản nháp");
                    break;
                }
                cprv.handleEdit(draf,"bản nháp");
                break;
            default:
                cprv.showError("Lệnh không hợp lệ");
                break;
        }
    }


    // gửi form
    public void excecuteConfirm(String s,JobPosting rp) throws Exception {
        if(s.toUpperCase().equals("Y")){
            if(!rp.checkValid()){
                cprv.showMessage("Dữ liệu không hợp lệ, vui lòng nhập lại");
                cprv.handleForm();
                return;
            }
            if(!rp.save()){
                cprv.showMessage("lưu database thất bại, vui lòng vào lại lần sau");
                return;
            }
            cprv.showMessage("lưu thành công");
            return;
        }
        if(s.toUpperCase().equals("N")){
            System.out.println("Huỷ gửi form");
            return;
        }
        cprv.showError("lệnh không hợp lệ");
        cprv.executeIsConfirm();
    }

    // lưu nháp
    public void executeSaveDarf(String s, JobPosting rp) throws Exception {
        draf = rp;
        if(s.toUpperCase().equals("Y")){
            if(!rp.checkValid()){
                cprv.showMessage("Dữ liệu không hợp lệ, vui lòng nhập lại");
                cprv.handleForm();
                return;
            }
            if(!rp.saveDraf()){
                cprv.showMessage("lưu database thất bại, vui lòng vào lại lần sau");
                return;
            }
            cprv.showMessage("lưu nháp thành công");
            return;
        }
        if(s.toUpperCase().equals("N")){
            System.out.println("Huỷ lưu nháp");
            return;
        }
            cprv.showError("lệnh không hợp lệ");
            cprv.executeIsSaveDraf(draf);
    }

    // tiếp tục bản nháp
    public void executeContinueDraf(String s) throws Exception {
        if(s.toUpperCase().equals("Y")){
            cprv.handleEdit(draf,"tiếp tục bản nháp");
            return;
        }
        if(s.toUpperCase().equals("N")){
            System.out.println("Thoát nháp");
            return;
        }
        cprv.showError("lệnh không hợp lệ");
        cprv.isContinueDraf();
    }

    // chỉnh sửa nháp
    public void handleEditDraf(String s) throws Exception {
        switch(s) {
            case "1":
                draf.setTitle(cprv.handleParam("tiêu đề"));
                cprv.handleEdit(draf,"bản nháp");
                break;
            case "2":
                draf.setDescription(cprv.handleParam("mô tả"));
                cprv.handleEdit(draf,"bản nháp");
                break;
            case "3":
                draf.setRequiment(cprv.handleParam("yêu cầu"));
                cprv.handleEdit(draf,"bản nháp");
                break;
            case "4":
                draf.setSalary(cprv.handleDouleParam("Luong"));
                cprv.handleEdit(draf,"bản nháp");
                break;
            case "5":
                draf.setDayEnd((cprv.handleDate("ngày hết hạn (vd : 2026-06-18):")));
                cprv.handleEdit(draf,"bản nháp");
                break;
            case "6":
                cprv.showMessage("Thoát chỉnh sửa nháp");
                break;
            case "7":
                cprv.executeIsSaveDraf(draf);
                break;
            case "8":
                cprv.executeIsConfirm(draf);
                break;
            default:
                cprv.showError("Lệnh không hợp lệ");
                cprv.handleEdit(draf,"bản nháp");
                break;
        }
    }


}
