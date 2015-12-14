package GetVersion;

import java.util.ArrayList;

import GetVersion.VulnerabilityInfo.VulnerInfo;

public class ExecuteExcel {
    VulnerabilityInfo vulnerabilityInfo = new VulnerabilityInfo();

    public void executeExcel(String diffPath, String codePath, String excelPath) {

        ArrayList<VulnerInfo> vulnerInfos = vulnerabilityInfo.readInfoFromExcel(excelPath);

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String diffPath = "";
        String codePath = "";
        String excelPath = "";

    }

}
