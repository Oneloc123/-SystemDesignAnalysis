package view.profileManagement;

import controller.profileManagement.EditProfileController;
import enumModel.RoleEnum;
import model.User;
import view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileView extends View {

    private EditProfileController epc;
    User user;

    public EditProfileView(EditProfileController epc) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.epc = epc;
    }

    @Override
    public void show() throws Exception {

        loop:
        while(true) {
            handleForm();
            printAddress();
            handleInput();
            if(question.equals("0")) {System.out.println("Thoat thanh cong"); break loop;}
        }
    }

    private void handleForm() throws Exception {

        user = new User();


        user.setId(Integer.parseInt(handleParam("ID nhân viên cần sửa:")));


        user.setRole(RoleEnum.valueOf(handleParam("Vai trò mới:")));
        user.setFullName(handleParam("Họ và tên mới:"));
        user.setDateOfBirth(handleDateParam("ngày tháng năm sinh mới"));
        user.setGender(handleParam("Giới tính mới:"));
        user.setPhone(handleParam("Số điện thoại mới:"));
        user.setCitizenIdentificationCard(handleParam("CCCD mới:"));
        user.setAddress(handleParam("Địa chỉ mới:"));

        epc.editProfile(user);
    }


    private Date handleDateParam(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        while (true) {
            try {
                System.out.print("Nhập " + name + " (dd/MM/yyyy): ");
                String input = netIn.readLine();
                return sdf.parse(input);
            } catch (Exception e) {
                showError("Ngày tháng không hợp lệ! Vui lòng nhập lại.");
            }
        }
    }

    private Object handleValidatedInput(String inputs, String dtType) {
        while (true) {
            try {

                String ip = handleParam(inputs);


                switch (dtType) {
                    case "INT":
                        return Integer.parseInt(ip);

                    case "ROLE":
                        return RoleEnum.valueOf(ip.toUpperCase().trim());

                    case "DATE":
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        sdf.setLenient(false);
                        Date dob = sdf.parse(ip);


                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.add(java.util.Calendar.YEAR, -18);
                        Date eighteenYearsAgo = cal.getTime();

                        if (dob.after(eighteenYearsAgo)) {
                            showError("Nhân viên phải từ đủ 18 tuổi trở lên!");
                            continue;
                        }

                        return dob;

                    default:
                        return ip;
                }
            } catch (Exception e) {

                showError("Dữ liệu không hợp lệ! Vui lòng nhập lại.");
            }
        }
}

    }
