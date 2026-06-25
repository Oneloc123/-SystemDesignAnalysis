# CHANGELOG — Quá trình sửa chữa lỗi đồ án
## Quản Lý Nhân Sự Console

**File này ghi lại tất cả các thay đổi khi tiến hành sửa lỗi.**
Mỗi lần sửa xong một lỗi, cập nhật thông tin vào bảng dưới đây.

---

## Hướng dẫn sử dụng

- Khi bắt đầu sửa một lỗi, đánh dấu `⏳ Đang sửa`
- Khi sửa xong, cập nhật `✅ Đã sửa` + mô tả thay đổi chi tiết
- Nếu không sửa vì lý do nào đó, ghi rõ lý do

---

## PHASE 0 — P0: SỬA GẤP (CRITICAL) ✅ ĐÃ HOÀN THÀNH

### P0-1: Tách static DAO khỏi Model ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `src/model/hr/Employee.java` | ✅ Đã sửa | Xóa `private static EmployeeDAO employeeDAO` + 7 method save/update/delete/find/findAll/findByDepartment/search |
| `src/model/Contract.java` | ✅ Đã sửa | Xóa `private static ContractDAO contractDAO` + 7 method save/update/delete/findById/findAll/findByEmployee/search |
| `src/model/Department.java` | ✅ Đã sửa | Xóa `private static DepartmentDAO dao` + 5 method save/update/delete/findById/findAll + xóa getEmployees() dead code |
| `src/model/ScheduleEntry.java` | ✅ Đã sửa | Xóa `private static ScheduleDAO dao` + 5 method save/update/delete/findById/findAll/findByEmployeeAndMonth |
| `src/model/User.java` (xóa `getAllEmployee()`) | ✅ Đã sửa | Xóa import ProfileDao + method `getAllEmployee()` |

**Recruitment Models cũng được sửa:**

| File | Trạng thái | Thay đổi |
|---|---|---|
| `Employer.java` | ✅ Đã sửa | Xóa static EmployerDAO + JobPostingDAO, xóa save/update/delete/findById/findAll |
| `Candidate.java` | ✅ Đã sửa | Xóa static CandidateDAO + JobApplicationDAO, xóa save/update/delete/findById/findAll |
| `JobPosting.java` | ✅ Đã sửa | Xóa static JobPostingDAO, xóa save/saveDraf/update/delete/findById/findAll/findByEmployer |
| `JobApplication.java` | ✅ Đã sửa | Xóa static JobApplicationDAO, xóa save/saveDraft/update/delete/findById/findAll/findByJobPosting/findByCandidate |
| `ApplicationReview.java` | ✅ Đã sửa | Xóa static ApplicationReviewDAO, xóa save/update/delete/findById/findAll/findByApplication |
| `InterviewSchedule.java` | ✅ Đã sửa | Xóa static InterviewScheduleDAO, xóa save/saveDraft/update/delete/findById/findAll/findDraftByApplication/findByApplication |

**Các Controller đã sửa để gọi DAO thay vì Model:**

| Controller | Trạng thái | Thay đổi |
|---|---|---|
| `AttendanceController.java` | ✅ Đã sửa | `Department.findAll()` → `departmentDAO.findAll()`, `Department.findById()` → `departmentDAO.findById()` |
| `EmployeeListController.java` | ✅ Đã sửa | Thêm `DepartmentDAO` + `EmployeeDAO`, `Department.findAll()` → `departmentDAO.findAll()`, `Employee.search()` → `employeeDAO.search()` |
| `CreateContractController.java` | ✅ Đã sửa | Thêm `ContractDAO`, `contract.save()` → `contractDAO.save(contract)` |
| `ViewContractController.java` | ✅ Đã sửa | Thêm `ContractDAO` + `EmployeeDAO`, `Contract.findAll()` → `contractDAO.findAll()`, `c.delete()` → `contractDAO.delete()` |
| `MyProfileController.java` | ✅ Đã sửa | Thêm `EmployeeDAO` + `DepartmentDAO`, `Employee.findByUserId()` → `employeeDAO.findByUserId()` |
| `ScheduleController.java` | ✅ Đã sửa | Thêm `ScheduleDAO`, `ScheduleEntry.findByEmployeeAndMonth()` → `scheduleDAO.findByEmployeeAndMonth()` |
| `ViewEmployeeProfileController.java` | ✅ Đã sửa | Thêm `EmployeeDAO`, `User.getAllEmployee()` → `employeeDAO.findAll()` |
| `ReviewApplicationsController.java` | ✅ Đã sửa | Thêm `JobPostingDAO` + `JobApplicationDAO` + `ApplicationReviewDAO` |
| `ScheduleInterviewController.java` | ✅ Đã sửa | Thêm `JobPostingDAO` + `JobApplicationDAO` + `InterviewScheduleDAO` |
| `SubmitCVController.java` | ✅ Đã sửa | Thêm `JobPostingDAO` + `JobApplicationDAO` |
| `CreatePostRecruimentController.java` | ✅ Đã sửa | Thêm `JobPostingDAO`, `rp.save()` → `jobPostingDAO.save(rp)` |
| `ProfileView.java` | ✅ Đã sửa | `List<User>` → `List<Employee>`, `u.getId()` → `u.getUserId()` |
| `SubmitCVView.java` | ✅ Đã sửa | Thêm instance field `currentApplication` thay vì pass `new JobApplication()` dummy |

---

### P0-2: Sửa lỗi so sánh Role (`.toString()` → Enum) ✅

| # | File | Trạng thái | Thay đổi |
|---|---|---|---|
| 1 | `RecruitmentManagementController.java` | ✅ Đã sửa | `.equals(RoleEnum.EMPLOYER.toString())` → `!= RoleEnum.EMPLOYER` |
| 2 | `CreatePostRecruimentController.java` | ✅ Đã sửa | `.equals(RoleEnum.EMPLOYER.toString())` → `!= RoleEnum.EMPLOYER` |
| 3 | `ReviewApplicationsController.java` | ✅ Đã sửa | `.equals(RoleEnum.EMPLOYER.toString())` → `!= RoleEnum.EMPLOYER` |
| 4 | `ScheduleInterviewController.java` | ✅ Đã sửa | `.equals(RoleEnum.EMPLOYER.toString())` → `!= RoleEnum.EMPLOYER` |
| 5 | `SubmitCVController.java` | ✅ Đã sửa | `.equals(RoleEnum.CANDIDATE.toString())` → `!= RoleEnum.CANDIDATE` |

---

### P0-3: Sửa `LoginController.goToHome()` set null sai ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `LoginController.java` | ✅ Đã sửa | Xóa `finally { MainController.currentUser = null; }` — user không bị null sau login |

---

### P0-4: Hardcode DB + Password plaintext

| File | Trạng thái | Thay đổi |
|---|---|---|
| `DatabaseConnection.java` | ⬜ Chưa sửa | Cần thảo luận: thêm config.properties hoặc giữ nguyên cho đồ án |
| `LoginController.java` (so sánh plaintext) | ⬜ Chưa sửa | Cần hash password (SHA-256/Bcrypt) — phức tạp hơn |
| `UserDAO.java` (lưu plaintext) | ⬜ Chưa sửa | Cần hash khi lưu |

---

## PHASE 1 — P1: QUAN TRỌNG (IMPORTANT)

### P1-1: Chuẩn hóa naming convention

| # | File | Lỗi | Trạng thái | Thay đổi |
|---|---|---|---|---|
| 1 | `HomeController.java` | `excuteComent()` → `executeCommand()` | ⬜ Chưa sửa | |
| 2 | `recruimentManagement/` folder | → `recruitmentManagement/` | ⬜ Chưa sửa | |
| 3 | `RecruitmentManagementController.java` | `excuteCommand()` → `executeCommand()` | ⬜ Chưa sửa | |
| 4 | `CreatePostRecruimentView.java` | `Recruiment` → `Recruitment` | ⬜ Chưa sửa | |

---

### P1-2: Controller gọi UI trực tiếp

| File | Trạng thái | Thay đổi |
|---|---|---|
| `CreateNewProfileController.java` | ⬜ Chưa sửa | Chuyển `System.out.println` → `view.showMessage()` |

---

### P1-3: Thêm method showMessage/showError cho View

| File | Trạng thái | Thay đổi |
|---|---|---|
| `CreateProfileView.java` | ⬜ Chưa sửa | Thêm `showMessage()` + `showError()` |

---

### P1-4: `User.java` — hai trường id + userId

| File | Trạng thái | Thay đổi |
|---|---|---|
| `User.java` | ⬜ Chưa sửa | Xóa `id`, giữ `userId` |
| `ProfileView.java` | ⬜ Chưa sửa | `u.getId()` → `u.getUserId()` |
| `ProfileDao.java` | ⬜ Chưa sửa | Sửa SQL từ `id` → `user_id` |
| `CreateProfileView.java` | ⬜ Chưa sửa | `user.setId()` → `user.setUserId()` |

---

### P1-5: `User.changePassword()` gọi DAO

| File | Trạng thái | Thay đổi |
|---|---|---|
| `User.java` | ⬜ Chưa sửa | Chuyển logic lên Controller |
| `ChangePasswordController.java` | ⬜ Chưa sửa | Nhận logic từ User |

---

### P1-6: `User.getAllEmployee()` static

| File | Trạng thái | Thay đổi |
|---|---|---|
| `User.java` | ⬜ Chưa sửa | Xóa method |
| `ViewEmployeeProfileController.java` | ⬜ Chưa sửa | Dùng `new EmployeeDAO().findAll()` |

---

## PHASE 2 — P2: NÊN SỬA (SHOULD)

| # | File | Lỗi | Trạng thái | Thay đổi |
|---|---|---|---|---|
| 1 | `AttendanceDAO.java` | `createDefaultPeriod()` hardcode | ⬜ Chưa sửa | |
| 2 | `calcSalary/*.java` | `double` → `BigDecimal` | ⬜ Chưa sửa | |
| 3 | `UserDAO.java` | Code duplicate mapUser | ⬜ Chưa sửa | |
| 4 | `ScheduleDAO.java` | `STR_TO_DATE` kém hiệu quả | ⬜ Chưa sửa | |
| 5 | `ProfileDao.java` | SQL sai tên cột | ⬜ Chưa sửa | |

---

## GHI CHÚ THÊM

- **TaxBraket → TaxBracket:** Theo kế hoạch cũ, giữ nguyên để không ảnh hưởng
- **double → BigDecimal cho tiền tệ:** Theo kế hoạch cũ, chưa đổi
- **AttendanceDAO hardcode:** Đã biết, sẽ sửa sau khi có DB thật

---

*Changelog được tạo ngày 25/06/2026. Cập nhật sau mỗi lần sửa lỗi.*
