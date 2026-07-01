package view.profileManagement;

import controller.profileManagement.CreateNewProfileController;
import view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateProfileView extends View {
    private CreateNewProfileController cnpv;

    public CreateProfileView(CreateNewProfileController cnpv) {
        netIn = new BufferedReader(new InputStreamReader(System.in));
        this.cnpv = cnpv;
    }

    @Override
    public void show() throws Exception {
        loop:
        while(true) {
            Map<String, Object> rawData = new HashMap<>();

            rawData.put("fullName", handleParam("Họ và tên:"));
            rawData.put("dateOfBirth", handleValidatedInput("ngày tháng năm sinh", "DATE"));
            rawData.put("gender", handleParam("giới tính:"));
            rawData.put("phone", handleParam("số điện thoại:"));
            rawData.put("citizenIdentificationCard", handleParam("cccd:"));
            rawData.put("address", handleParam("địa chỉ:"));
            rawData.put("role", handleParam("vai trò:"));

            Map<String, String> errors = cnpv.createProfile(rawData);

            if (errors.isEmpty()) {
                System.out.println("Tạo hồ sơ thành công");
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

            System.out.println("Nhấn 0 để quay lại hoặc Enter để tiếp tục thêm mới");
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
