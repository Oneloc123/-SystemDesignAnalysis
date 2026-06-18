package controller.recruimentManagement;

import controller.MainController;
import enumModel.AddressEnum;
import enumModel.RoleEnum;
import view.RecruitmentManagement.CreatePostRecruimentView;

public class CreatePostRecruimentController {
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
}
