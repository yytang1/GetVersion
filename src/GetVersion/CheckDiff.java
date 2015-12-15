package GetVersion;

import java.io.File;
import java.util.ArrayList;

public class CheckDiff {

    static Utils utils = new Utils();

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
        String path = codepath + File.separator + versionPrefix + version + File.separator
                + fileName;
        content = utils.readText(path);
        return content;
    }

    public String handleDiff(String filePath) {
        String strFilter = "";
        String strOriginal = utils.readText(filePath);
        if (strOriginal.contains("\r\n")) {
            strOriginal = strOriginal.replace("\r\n", "\n");
        }
        String[] strs = strOriginal.split("\n");
        for (int i = 0; i < strs.length; i++) {
            String temp = strs[i];
            // 一个diff匹配 @@ 划分
            if (temp.contains("@@")) {
                if (strs[i + 1].contains("}")) {
                    i++;
                }
                continue;
            }
            if (temp.subSequence(0, 1).equals("+") && !temp.contains("+++")) {
                continue;
            } else {
                temp = temp.substring(1);
            }
            strFilter += (strFilter.length() > 0 ? "\n" + temp : temp);
        }
        return strFilter;
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
    public ArrayList<String> getVersionContainDiff(String diffStr, String codepath,
            String versionPrefix, String fileName, ArrayList<String> versionList) {
        ArrayList<String> versionsTrue = new ArrayList<String>();
        for (String version : versionList) {
            String codeStr = getCodeFile(versionPrefix, codepath, version, fileName);
            if (codeStr.contains(diffStr))
                versionsTrue.add(version);
        }
        return versionsTrue;
    }

    boolean matchCode(String diffPath, String codePath) {
        String diffStr = handleDiff(diffPath);
        String codeStr = utils.readText(codePath);
        // TODO check是否包含该漏洞
        if (!codeStr.contains(diffStr))
            return false;
        return true;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // String codepath = "C:\\Users\\wt\\Desktop\\ffmpeg";
        String codepath2 = "C:\\Users\\yytang\\Desktop\\tar文件";
        // excel 中 获取的函数文件名
        String filePath = "libavcodec\\flashsv.c";
        // String diffPath =
        // "C:\\Users\\wt\\Desktop\\实验室work-tyy\\需完成的工作\\测试数据\\Ffmpeg\\Ffmpeg-1.1diff";
        String diffPath2 = "C:\\Users\\yytang\\Desktop\\all\\Ffmpeg-1.1diff";
        String cve = diffPath2 + File.separator + "CVE-2013-7015.txt";
        String versionPrefix = "ffmpeg-";
        System.out.println("begin");
        CheckDiff checkDiff = new CheckDiff();
        String diffStr = checkDiff.handleDiff(cve);

        ArrayList<String> fileList = checkDiff.getFileName(codepath2);
        ArrayList<String> versionList = checkDiff.getFileVersions(fileList, versionPrefix);
        // 满足区间条件的版本列

        ArrayList<String> versions = checkDiff.getVersionContainDiff(diffStr, codepath2,
                versionPrefix, filePath, versionList);
        for (String version : versions) {
            System.out.println(version);
        }
        System.out.println(versions.size() + "end");
    }
}
