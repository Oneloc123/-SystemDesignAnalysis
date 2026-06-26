package model.admin;

public class Role {
    private int roleId;
    private String roleCode;
    private String roleName;

    public Role() {
    }

    public Role(int roleId, String roleCode, String roleName) {
        this.roleId = roleId;
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}