package GetVersion;

import java.io.File;
import java.util.ArrayList;

public class CheckDiff {

    Utils utils = new Utils();
    HandDiff handDiff = new HandDiff();
    Common common = new Common();

    public ArrayList<String> getFileName(String filepath) {
        File file = new File(filepath);
        String[] filelist = file.list();
        ArrayList<String> fileLists = new ArrayList<String>();

        // TODO 选出文件夹
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File(filepath + "\\" + filelist[i]);
            if (!readfile.isFile()) {
                fileLists.add(filelist[i]);
            }
        }
        return fileLists;
    }

    public ArrayList<String> getFileVersions(ArrayList<String> fileLists, String versionPrefix) {

        ArrayList<String> versionLists = new ArrayList<String>();
        for (int i = 0; i < fileLists.size(); i++) {
            versionLists.add(fileLists.get(i).replaceAll(versionPrefix, ""));
        }
        return versionLists;
    }

    private String getCodeFile(String versionPrefix, String codepath, String version,
            String fileName) {
        String content = "";
        String path = common.getCodefilePath(codepath, versionPrefix, version, fileName);
        content = utils.readText(path);
        return content;
    }

    public ArrayList<String> getVersionFileExist(String codePath, String versionPrefix,
            String fileName, ArrayList<String> versionList) {
        ArrayList<String> versionsTrue = new ArrayList<String>();
        for (String version : versionList) {
            String filePath = common.getCodefilePath(codePath, versionPrefix, version, fileName);
            if (utils.fileExist(filePath))
                versionsTrue.add(version);
        }
        return versionsTrue;
    }

    /**
     * 
     * @param diffStr
     *            diff文件内容
     * @param codepath
     *            源码路径
     * @param versionPrefix
     *            版本号前缀
     * @param versionList
     *            版本列
     * @return 满足条件的版本列
     */
    public ArrayList<String> getVersionContainDiff(String cve, String codepath,
            String versionPrefix, String fileName, ArrayList<String> versionList, boolean isOld) {
        ArrayList<String> diffStrList = handDiff.handleDiff(cve, fileName, isOld);
        ArrayList<String> versionsTrue = new ArrayList<String>();
        if (diffStrList.size() < 1) {
            return versionsTrue;
        }
        int flag = 1;
        for (String version : versionList) {
            flag = 1;
            String codeStr = getCodeFile(versionPrefix, codepath, version, fileName);
            for (String diffStr : diffStrList)
                if (!codeStr.contains(diffStr)) {
                    flag = 0;
                    break;
                }
            if (flag > 0) {
                versionsTrue.add(version);
            }
        }
        return versionsTrue;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // String codepath = "C:\\Users\\wt\\Desktop\\ffmpeg";
        String codepath2 = "C:\\Users\\yytang\\Desktop\\all\\tar文件";
        // excel 中 获取的函数文件名
        String filePath = "libavcodec\\flashsv.c";
        // String diffPath =
        // "C:\\Users\\wt\\Desktop\\实验室work-tyy\\需完成的工作\\测试数据\\Ffmpeg\\Ffmpeg-1.1diff";
        String diffPath2 = "C:\\Users\\yytang\\Desktop\\all\\Ffmpeg-1.1diff";
        // String cve = diffPath2 + File.separator + "CVE-2013-7015.txt";
        // 多个函数测试
        String cve2 = diffPath2 + File.separator + "CVE-2013-7023.txt";
        String versionPrefix = "ffmpeg-";
        System.out.println("begin");
        CheckDiff checkDiff = new CheckDiff();

        ArrayList<String> fileList = checkDiff.getFileName(codepath2);
        ArrayList<String> versionList = checkDiff.getFileVersions(fileList, versionPrefix);
        // 满足区间条件的版本列

        ArrayList<String> versions = checkDiff.getVersionContainDiff(cve2, codepath2,
                versionPrefix, filePath, versionList, false);
        System.out.println(versions.size() + "end");
        System.out.println(versions);
        ArrayList<String> versions2 = checkDiff.getVersionContainDiff(cve2, codepath2,
                versionPrefix, filePath, versionList, true);
        System.out.println(versions2.size() + "end");
        System.out.println(versions2);
    }
}
