package GetVersion;

import java.io.File;
import java.util.ArrayList;

public class HandDiff {
    Utils utils = new Utils();

    boolean matchCode(String diffPath, String codePath, String fileName) {
        ArrayList<String> diffStrList = handleDiff(diffPath, fileName, false);
        String codeStr = utils.readText(codePath);
        // TODO check是否包含该漏洞
        for (String string : diffStrList) {
            if (!codeStr.contains(string))
                return false;
        }
        return true;
    }

    /**
     * 
     * @param filePath
     *            diff文件路径
     * @param fileName
     *            文件名
     * @param isOld
     *            是否获取修改前代码，{@code false}则获取修改后代码
     * @return 文件名对应下的函数列
     */
    // diff文件多函数处理，提取代码列
    public ArrayList<String> handleDiff(String filePath, String fileName, boolean isOld) {
        ArrayList<String> strFilterList = new ArrayList<String>();
        // 文件名处理：
        fileName = fileName.replace("\\", "/");
        // 如果isOld，则去掉"+"行
        String plus = (isOld ? "+" : "-");
        String strOriginal = utils.readText(filePath);
        strOriginal = handleLineBreak(strOriginal);
        String[] strs = strOriginal.split("\n");
        for (int i = 0; i < strs.length; i++) {
            // 一个diff匹配 @@ 划分
            if (strs[i].contains("Index:") || strs[i].contains(fileName)) {
                // 去掉index之下几行无用代码
                while (!strs[++i].contains("@@")) {
                }
                while (i < strs.length && strs[i].contains("@@")) {
                    String strFilter = "";
                    i++;
                    if (strs[i].contains("}")) {
                        i++;
                    }
                    while (i < strs.length && !strs[i].contains("@@")) {
                        String temp = strs[i];
                        if (strs[i].contains("Index:") || strs[i].contains("@@"))
                            break;
                        if (temp.subSequence(0, 1).equals(plus)) {
                            i++;
                            continue;
                        } else
                            temp = temp.substring(1);

                        strFilter += (strFilter.length() > 0 ? "\n" + temp : temp);
                        i++;
                    }
                    strFilterList.add(strFilter);
                }

            }
        }
        return strFilterList;
    }

    /**
     * 将window下换行符“\r\n”替换为“\n”
     * 
     * @param string
     *            需要处理的字符串
     * @return
     */
    public String handleLineBreak(String string) {
        if (string.contains("\r\n")) {
            string = string.replace("\r\n", "\n");
        }
        return string;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HandDiff handDiff = new HandDiff();
        ;
        String diffPath2 = "C:\\Users\\yytang\\Desktop\\all\\Ffmpeg-1.1diff";
        // String diffPath1 =
        // "C:\\Users\\wt\\Desktop\\实验室work-tyy\\Ffmpeg-1.1diff";
        // 多个函数
        // String cve2 = "CVE-2013-7023.txt";
        // 多个函数
        // String cve3 = "CVE-2014-2099.txt";
        // 多个文件CVE-2011-1691;CVE-2011-1202
        // String cve4 = "CVE-2013-2912.txt";
        // String cve5 = "CVE-2013-2911.txt";
        // String cve = "CVE-2013-7015.txt";
        String cve6 = "CVE-2011-3936.txt";
        String fileName6 = "libavformat/dv.c";
        String fileName = "libavcodec/flashsv.c";
        // String fileName2 = "Source/core/xml/XSLStyleSheetLibxslt.cpp";
        // String fileName3 =
        // "content\\renderer\\pepper\\pepper_in_process_router.cc";
        System.out.println(fileName);
        ArrayList<String> diffList = handDiff.handleDiff(diffPath2 + File.separator + cve6,
                fileName6, true);
        System.out.println(diffList.size());
        int i = 0;
        for (String string : diffList) {

            System.out.println(i + "个函数：\n" + string);
            i++;
        }
        ArrayList<String> diffList2 = handDiff.handleDiff(diffPath2 + File.separator + cve6,
                fileName6, false);
        System.out.println(diffList2.size());
        i = 0;
        for (String string : diffList2) {
            System.out.println(i + "个函数：\n" + string);
            i++;
        }

    }
}
