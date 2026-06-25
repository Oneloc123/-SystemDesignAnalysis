package test;

import controller.recruitmentManagement.SubmitCVController;
import view.RecruitmentManagement.SubmitCVView;

public class SubmmitedCV {
    public static void main(String[] args) throws Exception {
        SubmitCVView sv = new SubmitCVView(new SubmitCVController());
        sv.show();
    }
}
