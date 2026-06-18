package controller.recruimentManagement;

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
        // xác thực quyền
        if(!MainController.currentUser.getRole().equals(RoleEnum.EMPLOYER)){
            return false;
        }
        // xác thực thành công
        MainController.addresses.add(address);
        rmv.show();
        // khi thoát
        MainController.addresses.remove(AddressEnum.RecruitmentManagement);
        return true;
    }

    public void excuteComent(String question) throws Exception {
        switch(question) {
            case "1":
                functionCreatePost();
                break;
            case "2":
                rmv.printAddress();
                System.out.println("Chuc nang 2 dang thuc hien");
                break;
            case "3":
                rmv.printAddress();
                System.out.println("Chuc nang 2 dang thuc hien");
                break;
            default:
                rmv.showError("Lệnh không hợp lệ");
                break;
        }
    }
    public void functionCreatePost() throws Exception {
        CreatePostRecruimentController cprc = new CreatePostRecruimentController();
        boolean result = cprc.navigateTo();
        if(!result){
            rmv.showError("không có quyền truy cập");
        }
    }
}
