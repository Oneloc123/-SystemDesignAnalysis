package view.profileManagement;

import controller.profileManagement.EditProfileController;
import model.profile.Profile;
import view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileView extends View {

    private EditProfileController epc;

    public EditProfileView(EditProfileController epc) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.epc = epc;
    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            String idStr = handleParam("ID nhân viên cần sửa:");
            long id = Long.parseLong(idStr);

            Profile currentProfile = epc.getProfileById(id);
            if (currentProfile == null) {
                showError("Không tìm thấy nhân viên có ID: " + id);
                System.out.println("Nhấn 0 để quay lại hoặc Enter để nhập lại");
                printAddress();
                handleInput();
                if(question.equals("0")) {
                    System.out.println("Thoat thanh cong");
                    break loop;
                }
                continue;
            }

            Map<String, Object> newData = new HashMap<>();
            newData.put("id", id);

            newData.put("fullName", handleParam("Họ và tên mới:"));
            newData.put("dateOfBirth", handleValidatedInput("ngày tháng năm sinh mới", "DATE"));
            newData.put("gender", handleParam("Giới tính mới:"));
            newData.put("phone", handleParam("Số điện thoại mới:"));
            newData.put("citizenIdentificationCard", handleParam("CCCD mới:"));
            newData.put("address", handleParam("Địa chỉ mới:"));
            newData.put("role", handleParam("Vai trò mới:"));

            Map<String, String> errors = epc.updateProfile(newData);

            if (errors.isEmpty()) {
                System.out.println("Cập nhật hồ sơ thành công");
            } else if (errors.containsKey("duplicate")) {
                showError(errors.get("duplicate"));
                System.out.println("Vui lòng nhập lại thông tin");
                continue;
            } else {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    showError(entry.getValue());
                }
                System.out.println("Vui lòng nhập lại thông tin");
                continue;
            }

            System.out.println("Nhấn 0 để quay lại hoặc Enter để tiếp tục sửa");
            printAddress();
            handleInput();
            if(question.equals("0")) {
                System.out.println("Thoat thanh cong");
                break loop;
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

                        cal = java.util.Calendar.getInstance();
                        cal.add(java.util.Calendar.YEAR, -70);
                        Date seventyYearsAgo = cal.getTime();

                        if (dob.before(seventyYearsAgo)) {
                            showError("Nhân viên không được quá 70 tuổi!");
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
