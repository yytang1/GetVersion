package GetVersion;

import java.io.File;
import java.util.ArrayList;

import GetVersion.VulnerabilityInfo.VulnerInfo;

public class ExecuteExcel {

    public void executeExcel(String diffPath, String codePath, String excelPath) throws Exception {

        VulnerabilityInfo vulnerabilityInfo = new VulnerabilityInfo();
        CheckDiff checkDiff = new CheckDiff();
        HandleVersion handleVersion = new HandleVersion();

        ArrayList<VulnerInfo> vulnerInfos = vulnerabilityInfo.readInfoFromExcel(excelPath);
        for (VulnerInfo vulnerInfo : vulnerInfos) {
            String versionPrefix = vulnerInfo.softeware + "-";
            System.out.println(vulnerInfo.cve);
            String diffFilePath = diffPath + File.separator + vulnerInfo.cve + ".txt";

            // 源码所有版本文件名列
            ArrayList<String> fileList = checkDiff.getFileName(codePath);
            // 源码所有版本列
            ArrayList<String> versionList = checkDiff.getFileVersions(fileList, versionPrefix);
            // 满足区间条件的版本列
            ArrayList<String> versions = handleVersion.getCodeVersion(versionList,
                    vulnerInfo.softwareVersion);

            vulnerInfo.containVersions = checkDiff.getVersionContainDiff(diffFilePath, codePath,
                    versionPrefix, vulnerInfo.fileName, versions, true);
            vulnerInfo.errorVersions = checkDiff.getVersionContainDiff(diffFilePath, codePath,
                    versionPrefix, vulnerInfo.fileName, versions, false);
        }
        printResult(vulnerInfos);
        vulnerabilityInfo.writeResultToExcel(vulnerInfos, excelPath);
    }

    void printResult(ArrayList<VulnerInfo> vulnerInfos) {
        for (VulnerInfo vulnerInfo : vulnerInfos) {
            System.out.println("cve:" + vulnerInfo.cve);
            System.out.println("versions:" + vulnerInfo.containVersions);
            System.out.println("ErrorVersions:" + vulnerInfo.errorVersions);
        }
        System.out.println(vulnerInfos.size());
    }

    public static void main(String[] args) throws Exception {
        // String path = "C:\\Users\\wt\\Desktop\\实验室work-tyy\\";
        String path = "C:\\Users\\yytang\\Desktop\\all\\";
        String diffPath1 = path + "Ffmpeg-1.1diff";
        // String codePath1 = "C:\\Users\\wt\\Desktop\\ffmpeg";
        String codePath1 = path + "tar文件";
        String excelPath1 = path + "test.xlsx";
        ExecuteExcel executeExcel = new ExecuteExcel();

        executeExcel.executeExcel(diffPath1, codePath1, excelPath1);
        System.out.println("end");
    }
}
