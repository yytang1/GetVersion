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

    /**
     * diff文件多函数处理，提取代码列
     * 
     * @param filePath
     * @return
     */
    public ArrayList<String> handleDiff(String filePath) {
        ArrayList<String> strFilterList = new ArrayList<String>();

        String strOriginal = utils.readText(filePath);
        if (strOriginal.contains("\r\n")) {
            strOriginal = strOriginal.replace("\r\n", "\n");
        }
        String[] strs = strOriginal.split("\n");
        for (int i = 0; i < strs.length; i++) {
            // 一个diff匹配 @@ 划分
            if (strs[i].contains("@@")) {
                String strFilter = "";
                i++;
                if (strs[i].contains("}")) {
                    i++;
                }
                while (i < strs.length && !strs[i].contains("@@")) {

                    String temp = strs[i];
                    if (temp.subSequence(0, 1).equals("+") && !temp.contains("+++")) {
                        i++;
                        continue;
                    } else {
                        temp = temp.substring(1);
                    }

                    strFilter += (strFilter.length() > 0 ? "\n" + temp : temp);
                    i++;
                }
                i--;
                strFilterList.add(strFilter);
            }

        }
        return strFilterList;
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
            String versionPrefix, String fileName, ArrayList<String> versionList) {
        ArrayList<String> diffStrList = handleDiff(cve);
        ArrayList<String> versionsTrue = new ArrayList<String>();
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
        String codepath2 = "C:\\Users\\yytang\\Desktop\\tar文件";
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
                versionPrefix, filePath, versionList);
        System.out.println(versions.size() + "end");
    }
}
