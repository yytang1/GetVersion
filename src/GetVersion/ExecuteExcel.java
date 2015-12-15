package GetVersion;

import java.io.File;
import java.util.ArrayList;

import GetVersion.VulnerabilityInfo.VulnerInfo;

public class ExecuteExcel {

    public void executeExcel(String diffPath, String codePath, String excelPath,
            String versionPrefix) throws Exception {

        VulnerabilityInfo vulnerabilityInfo = new VulnerabilityInfo();
        CheckDiff checkDiff = new CheckDiff();
        HandleVersion handleVersion = new HandleVersion();

        ArrayList<VulnerInfo> vulnerInfos = vulnerabilityInfo.readInfoFromExcel(excelPath);
        for (VulnerInfo vulnerInfo : vulnerInfos) {
            String diffFilePath = diffPath + File.separator + vulnerInfo.cve + ".txt";
            String diffStr = checkDiff.handleDiff(diffFilePath);

            // 源码所有版本文件名列
            ArrayList<String> fileList = checkDiff.getFileName(codePath);
            // 源码所有版本列
            ArrayList<String> versionList = checkDiff.getFileVersions(fileList, versionPrefix);
            // 满足区间条件的版本列
            ArrayList<String> versions = handleVersion.getCodeVersion(versionList,
                    vulnerInfo.softwareVersion);

            ArrayList<String> versionsR = checkDiff.getVersionContainDiff(diffStr, codePath,
                    versionPrefix, vulnerInfo.fileName, versions);
            vulnerInfo.containVersions = (ArrayList<String>) versionsR.clone();

            System.out.println(versionsR);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String path = "C:\\Users\\wt\\Desktop\\实验室work-tyy\\";
        String diffPath1 = path + "Ffmpeg-1.1diff";
        String codePath1 = "";
        String excelPath1 = path + "all\\test.xlsx";
        ExecuteExcel executeExcel = new ExecuteExcel();
        String versionPrefix = "ffmpeg-";
        // executeExcel.executeExcel(diffPath1, codePath1, excelPath1);
        System.out.println("end");
    }
}
