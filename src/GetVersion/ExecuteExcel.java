package GetVersion;

import java.io.File;
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
        System.out.println(utils.deleteFileOrDir(resultPath + File.separator + Common.reuseCode));

        for (VulnerInfo vulnerInfo : vulnerInfos) {
            String versionPrefix = vulnerInfo.softeware + "-";
            System.out.println(vulnerInfo.cve);
            String diffFilePath = common.getDifftxtPath(diffPath, vulnerInfo.cve);
            if (!utils.fileExist(diffFilePath)) {
                vulnerInfo.report += "\t该diff文件不存在";
                continue;
            }
            String codePathTemp = codePath + File.separator + vulnerInfo.softeware;
            // 源码所有版本文件名列
            ArrayList<String> fileList = dealSoftware.getFileName(codePath, vulnerInfo.softeware);
            // 源码所有版本列
            ArrayList<String> versionList = checkDiff.getFileVersions(fileList, versionPrefix);
            // 满足区间条件的版本列
            ArrayList<String> versions = handleVersion.getCodeVersion(versionList,
                    vulnerInfo.softwareVersion);
            if (versions == null) {
                vulnerInfo.report += "Software version出错，请注意检查";
                continue;
            }

            // 获取文件存在的版本列
            vulnerInfo.existVersions = checkDiff.getVersionFileExist(codePathTemp, versionPrefix,
                    vulnerInfo.fileName, versions);
            System.out.println(vulnerInfo.existVersions);
            ArrayList<String> containVersions = checkDiff.getVersionContainDiff(diffFilePath,
                    codePathTemp, versionPrefix, vulnerInfo, true);
            if (containVersions == null) {
                vulnerInfo.report += "diff文件读取出错，请检查diff文件;";
                continue;
            }
            vulnerInfo.containVersions = containVersions;
            vulnerInfo.errorVersions = checkDiff.getVersionContainDiff(diffFilePath, codePathTemp,
                    versionPrefix, vulnerInfo, false);

            // 针对同一漏洞的代码复用实例的获取
            System.out.println(versions + "end");
            System.out.println("containVersions" + vulnerInfo.containVersions + "end");

            versions.removeAll(vulnerInfo.containVersions);
            versions.removeAll(vulnerInfo.errorVersions);
            Collections.reverse(versions);
            vulnerInfo.reuseVersions = codeReuse.getHalfMatchFile(diffFilePath, codePathTemp,
                    versionPrefix, vulnerInfo, versions, resultPath);

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
        String path = "C:\\Users\\wt\\Desktop\\tyy\\实验室work-tyy\\";

        String diffPath1 = "C:\\Users\\wt\\Desktop\\tyy\\实验室work-tyy\\Ffmpeg-最终核对数据-测试集\\Ffmpeg补丁文件-2016.1.1";
        String diffPath2 = "C:\\Users\\wt\\Desktop\\tyy\\实验室work-tyy\\Ffmpeg复用代码获取程序修改\\Ffmpeg补丁文件-新";
        String codePath1 = "C:\\Users\\wt\\Desktop\\tyy\\software";
        String excelPath1 = path + "getContainVersion\\testall.xlsx";
        String excelPath2 = path + "getContainVersion\\testTemp.xls";
        String excelPath3 = path + "getContainVersion\\2016.1.16-Ffmpeg漏洞信息-新.xls";
        ExecuteExcel executeExcel = new ExecuteExcel();
        executeExcel.executeExcel(diffPath2, codePath1, excelPath2);
        System.out.println("end");
    }
}
