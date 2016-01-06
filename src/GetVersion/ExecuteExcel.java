package GetVersion;

import java.util.ArrayList;
import java.util.Collections;

import CodeReuse.CodeReuse;
import GetVersion.VulnerabilityInfo.VulnerInfo;

public class ExecuteExcel {
    Common common = new Common();
    Utils utils = new Utils();

    public void executeExcel(String diffPath, String codePath, String excelPath) throws Exception {

        VulnerabilityInfo vulnerabilityInfo = new VulnerabilityInfo();
        CheckDiff checkDiff = new CheckDiff();
        HandleVersion handleVersion = new HandleVersion();
        CodeReuse codeReuse = new CodeReuse();
        DealSoftware dealSoftware = new DealSoftware();

        ArrayList<VulnerInfo> vulnerInfos = vulnerabilityInfo.readInfoFromExcel(excelPath);
        // 复用实例代码存放路径
        String resultPath = excelPath.substring(0, excelPath.lastIndexOf("\\"));

        for (VulnerInfo vulnerInfo : vulnerInfos) {
            String versionPrefix = vulnerInfo.softeware + "-";
            System.out.println(vulnerInfo.cve);
            String diffFilePath = common.getDifftxtPath(diffPath, vulnerInfo.cve);
            if (!utils.fileExist(diffFilePath)) {
                vulnerInfo.report += "\t该diff文件不存在";
                continue;
            }
            // 源码所有版本文件名列
            ArrayList<String> fileList = dealSoftware.getFileName(codePath, vulnerInfo.softeware);
            // 源码所有版本列
            ArrayList<String> versionList = checkDiff.getFileVersions(fileList, versionPrefix);
            // 满足区间条件的版本列
            ArrayList<String> versions = handleVersion.getCodeVersion(versionList,
                    vulnerInfo.softwareVersion);

            // 获取文件存在的版本列
            vulnerInfo.existVersions = checkDiff.getVersionFileExist(codePath, versionPrefix,
                    vulnerInfo.fileName, versions);

            vulnerInfo.containVersions = checkDiff.getVersionContainDiff(diffFilePath, codePath,
                    versionPrefix, vulnerInfo.fileName, vulnerInfo.existVersions, true);
            vulnerInfo.errorVersions = checkDiff.getVersionContainDiff(diffFilePath, codePath,
                    versionPrefix, vulnerInfo.fileName, vulnerInfo.existVersions, false);

            // 针对同一漏洞的代码复用实例的获取
            versions.removeAll(vulnerInfo.containVersions);
            versions.removeAll(vulnerInfo.errorVersions);
            Collections.reverse(versions);
            codeReuse.getHalfMatchFile(diffFilePath, codePath, versionPrefix, vulnerInfo, versions,
                    resultPath);

        }
        printResult(vulnerInfos);
        System.out.println("同一漏洞的代码复用实例存放路径：" + resultPath + "\n同一漏洞的代码复用实例标记：" + common.markStr);
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
