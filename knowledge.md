# Project knowledge

This file gives Codebuff context about your project: goals, commands, conventions, and gotchas.

## Project overview
Console-based HR Management System (Quản lý nhân sự) in Java. Runs in the terminal with text-based menus (not Swing). Users navigate by entering numbers/commands via `System.in`.

- **Language:** Java (JDK 21)
- **Database:** MySQL `qlns` on `localhost:3306`, user `root`, no password
- **DB setup scripts:** `sql/create_salary_tables.sql`
- **Entry point:** `src/view/Main.java` → `LoginController`
- **Test harness:** `src/test/HomeTest.java` (manual test with role selection); `src/test/newMain.java` (alternative entry via LoginController)
- **DB Connector:** MySQL Connector/J 9.7 (library configured in `.iml`)

## Quickstart
- **Build:** IntelliJ IDEA internal build (no Maven/Gradle). Compile via IDE or `javac` manually.
- **Run:** `src/view/Main.java` as the main class
- **Test:** Run `src/test/HomeTest.java` (manual, interactive test) or `src/test/newMain.java` (direct login)
- **DB:** Make sure MySQL is running with the `qlns` schema and MySQL Connector/J (9.7) is on the classpath

## Architecture
- **MVC pattern:** `src/view/` → `src/controller/` → `src/dao/` → MySQL
- **Screen navigation:** Stack-based `ScreenManager` pushes/pops controllers; each controller has `showOn()`/`showOff()`
- **User session:** `ScreenManager.currentUser` holds logged-in user; roles checked per feature
- **Roles:** ACCOUNTANT, HR, ADMIN, EMPLOYER, CANDIDATE, EMPLOYEE, MANAGER
- **Address breadcrumb:** `MainController.addresses` (ArrayList of AddressEnum) shows navigation path in the prompt
- **Login flow:** `LoginController.goToHome()` → `HomeController.show()` → user selects feature via numeric menu

## Key files & directories
- `sql/` — Database setup scripts (salary tables)
- `src/controller/base/Controller.java` — Abstract base for all controllers
- `src/controller/ScreenManager.java` — Navigation hub with `navigateTo()` and `back()`
- `src/dao/DatabaseConnection.java` — Singleton DB connection (JDBC)
- `src/enumModel/RoleEnum.java` — User roles enum
- `src/enumModel/AddressEnum.java` — Navigation breadcrumb locations
- `src/model/User.java` — User entity (auth + profile info)
- `src/model/hr/Employee.java` — Employee entity
- `src/model/calcSalary/` — Salary calculation models (Payroll, Parameter, TaxBracket, etc.)
- `CHANGELOG-FIX.md` — Detailed changelog of all bug fixes made to the project
- `fix-plan.md` — Original fix plan with priority tiers (P0-P3)

## Conventions
- **Language:** Vietnamese for user-facing text; English for code identifiers
- **Base classes:** `Controller` (abstract, in `controller/base/`) and `View` (abstract, in `view/`)
- **Views** use `BufferedReader netIn` for input and `System.out` for output
- **DAOs** use singleton `DatabaseConnection.getInstance().getConnection()`
- **All dates** are `java.sql.Date` or `java.sql.Timestamp`
- **Containers:** `ArrayList` and `Stack` are used throughout
- **Role check:** Use `!= RoleEnum.XXX` directly (not `.toString()` comparison)

## Things to avoid
- **Don't add GUI/Swing** — this is a console app
- **Don't change the database credentials** unless asked — they're hardcoded in `DatabaseConnection.java`
- **Don't add build tools** (Maven/Gradle) — project uses IntelliJ IDEA's build system
- **Don't put DAO logic in Model classes** — Models should be pure entities (follows BCE pattern)
- **Don't use `System.out.println` in Controllers** — route output through the View layer
