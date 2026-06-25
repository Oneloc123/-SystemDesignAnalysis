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

## PHASE 1 — P1: QUAN TRỌNG (IMPORTANT) ✅ ĐÃ HOÀN THÀNH

### P1-1: Chuẩn hóa naming convention ✅

| # | File | Lỗi | Trạng thái | Thay đổi |
|---|---|---|---|---|
| 1 | `HomeController.java` | `excuteComent()` → `executeCommand()` | ✅ Đã sửa | Rename method, đồng bộ `HomeView.java` gọi `hc.executeCommand()` |
| 2 | `ProfileController.java` | `excuteComent()` → `executeCommand()` | ✅ Đã sửa | Rename method, đồng bộ `ProfileManagementView.java` gọi `pc.executeCommand()` |
| 3 | `RecruitmentManagementController.java` | `excuteCommand()` → `executeCommand()` | ✅ Đã sửa | Rename method, đồng bộ `RecruitmentManagementView.java` gọi `rmc.executeCommand()` |
| 4 | `recruimentManagement/` folder | → `recruitmentManagement/` | ✅ Đã sửa | Tạo folder mới, copy 5 files, update package + import ở 8 files, xóa folder cũ |
| 5 | `CreatePostRecruimentView.java` + Controller | `Recruiment` → `Recruitment` | ✅ Đã sửa | Rename class + file + constructor + tất cả references |

---

### P1-2: Controller gọi UI trực tiếp ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `CreateNewProfileController.java` | ✅ Đã sửa | `System.out.println("Tạo hồ sơ thành công")` → `cpv.showMessage(...)`; `System.out.println("Lỗi: " + e.getMessage())` → `cpv.showError(...)` |

### P1-3: Thêm method showMessage/showError cho View ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `CreateProfileView.java` | ✅ Không cần sửa | `View` base class đã có `showMessage(String)` + `showError(String)` — `CreateProfileView` extends `View` nên kế thừa sẵn |

---

### P1-4: `User.java` — hợp nhất id + userId ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `User.java` | ✅ Đã sửa | Xóa field `private long id` + getter/setter `getId()`/`setId()`. Sửa constructor dùng `userId` thay `id` |
| `ProfileDao.java` | ✅ Đã sửa | `user.getId()` → `user.getUserId()`, `ps.setLong()` → `ps.setInt()` |
| `CreateProfileView.java` | ✅ Đã sửa | `user.setId(Long.parseLong(...))` → `user.setUserId(Integer.parseInt(...))` |
| `ProfileView.java` | ✅ Đã sửa | Đã dùng `u.getUserId()` từ trước |

---

### P1-5: `User.changePassword()` gọi DAO ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `User.java` | ✅ Đã sửa | Xóa hoàn toàn method `changePassword()` + `validatePasswordStrength()`. Xóa imports `UserDAO`, `Pattern` |
| `ChangePasswordController.java` | ✅ Đã sửa | Nhận logic changePassword từ User: validate oldPw, strength check, gọi `userDAO.updatePassword()` + `currentUser.setPassword()` |

---

### P1-6: `User.getAllEmployee()` static ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `User.java` | ✅ Đã sửa từ trước | Method đã được xóa |
| `ViewEmployeeProfileController.java` | ✅ Đã sửa từ trước | Đã dùng `new EmployeeDAO().findAll()` |

---

## PHASE 2 — P2: NÊN SỬA (SHOULD) ✅ ĐÃ HOÀN THÀNH

| # | File | Lỗi | Trạng thái | Thay đổi |
|---|---|---|---|---|
| 1 | `AttendanceDAO.java` | `createDefaultPeriod()` hardcode | ✅ Đã sửa | Xóa method `createDefaultPeriod()` + `createDetail()`. `getByDepartment()` trả `findByMonth()` trực tiếp |
| 2 | `src/model/calcSalary/*.java` | `double` → `BigDecimal` | ❌ Giữ nguyên | Theo kế hoạch cũ (knowledge.md): "Không đổi double → BigDecimal" |
| 3 | `UserDAO.java` | Code duplicate mapUser | ✅ Đã sửa | Extract `private User mapUser(ResultSet)`. 4 methods (`findById`, `findAll`, `findByUsername`, `findByEmail`) dùng chung |
| 4 | `ScheduleDAO.java` | `STR_TO_DATE` kém hiệu quả | ✅ Đã sửa | `MONTH(STR_TO_DATE(...))` → `SUBSTRING(date, 4, 2)`; `YEAR(STR_TO_DATE(...))` → `SUBSTRING(date, 7, 4)` |
| 5 | `ProfileDao.java` | SQL sai tên cột | ✅ Đã sửa | `id`→`user_id`, `fullName`→`full_name`, `dateOfBirth`→`date_of_birth`, `citizenIdentificationCard`→`citizen_id` (cả SELECT, INSERT, ResultSet) |

---

## PHASE 3 — P3: CÓ THỂ SỬA (NICE TO HAVE)

### P3-2: ScreenManager — thêm navigation cho tất cả màn hình ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `ScreenManager.java` | ✅ Đã sửa | Thêm case `RecruitmentManagement` + `ProfileManagement`. Sửa `ContractManagement` trả về `cmc.navigate()` trực tiếp, không còn `System.out.println` trùng lặp |
| `HomeController.java` | ✅ Đã sửa | Xóa dead code (`handleProfile`, `functionRecruitmentManagement`, `functionContractManagement`). Case 6, 8 gọi `ScreenManager.navigateTo()` trực tiếp + xử lý lỗi. Xóa imports không dùng |

---

## RENAME: TaxBraket → TaxBracket ✅

| File | Trạng thái | Thay đổi |
|---|---|---|
| `TaxBraket.java` → `TaxBracket.java` | ✅ Đã sửa | Rename file + class + constructor |
| `TaxBraketTest.java` → `TaxBracketTest.java` | ✅ Đã sửa | Rename file + class + tất cả references |
| `Parameter.java` | ✅ Đã sửa | Field `taxBraket`→`taxBracket`, getter/setter, copy constructor |
| `ParameterSettingsView.java` | ✅ Đã sửa | Import, type declarations, method calls |
| `CalcSalaryView.java` | ✅ Đã sửa | `getTaxBraket()` → `getTaxBracket()` |
| `CalcSalaryController.java` | ✅ Đã sửa | Type + method calls |
| `ParameterDAO.java` | ✅ Đã sửa | Import, type, method calls |
| `PayrollTest.java` | ✅ Đã sửa | Type + method calls |
| `ParameterTest.java` | ✅ Đã sửa | Type + method calls + fix 2 syntax errors (mất `System.out.println`) |

---

## GHI CHÚ THÊM

- **double → BigDecimal cho tiền tệ:** Theo kế hoạch cũ, chưa đổi
- **P0-4 (Security):** Chưa sửa — cần thảo luận thêm về hash password và config DB

---

*Changelog được tạo ngày 25/06/2026. Cập nhật lần cuối: 27/06/2026.*
