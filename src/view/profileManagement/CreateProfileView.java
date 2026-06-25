package view.profileManagement;

import controller.profileManagement.CreateNewProfileController;
import controller.profileManagement.ProfileController;
import controller.recruimentManagement.CreatePostRecruimentController;
import enumModel.RoleEnum;
import model.User;
import view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static controller.MainController.printList;

public class CreateProfileView extends View {
    private CreateNewProfileController cnpv;
    User user;

    public CreateProfileView(CreateNewProfileController cnpv) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.cnpv = cnpv;
    }


    @Override
    public void show() throws Exception {
        loop:
        while(true) {

            handleForm();

            //
            printAddress();
            handleInput();
            //
            //exit
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
            // thuc thi func

        }
    }

    private void handleForm() throws IOException {
        user = new User();
        user.setId(Long.parseLong(handleParam("ID:")));
        user.setRole(RoleEnum.valueOf(handleParam("Vai trò:")));
        user.setFullName(handleParam("Họ và tên:"));
        user.setDateOfBirth(handleDateParam("ngày tháng năm sinh"));
        user.setGender(handleParam("giới tính:"));
        user.setPhone(handleParam("số điện thoại:"));
        user.setCitizenIdentificationCard(handleParam("cccd:"));
        user.setAddress(handleParam("địa chỉ:"));

        cnpv.createProfile(user);
    }

    //hàm chuyển String từ handleParam thành date
    private Date handleDateParam(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        sdf.setLenient(false);//validate du lieu nhap

        while (true) {
            try {
                System.out.println("nhập ngày tháng năm sinh(dd/mm/yyyy):");
                String input = netIn.readLine();
                return sdf.parse(input);
            } catch (Exception e) {
                showError("Ngày tháng không hợp lệ!");
                System.out.print("nhập lại: " + name + " : ");
            }


    }
}
}
