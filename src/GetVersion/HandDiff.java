package GetVersion;

import java.io.File;
import java.util.ArrayList;

public class HandDiff {
    Utils utils = new Utils();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HandDiff handDiff = new HandDiff();
        ;
        String diffPath2 = "C:\\Users\\yytang\\Desktop\\all\\Ffmpeg-1.1diff";
        // String diffPath1 =
        // "C:\\Users\\wt\\Desktop\\实验室work-tyy\\Ffmpeg-1.1diff";
        // 多个函数
        // String cve2 = "CVE-2013-7023.txt";
        // 多个文件
        String cve3 = "CVE-2014-2099.txt";

        ArrayList<String> diffList = handDiff.handleDiff(diffPath2 + File.separator + cve3);
        System.out.println(diffList.size());
        System.out.println("end");
        int i = 0;
        for (String string : diffList) {

            System.out.println(i + "个函数：\n" + string);
            i++;
        }
    }

    boolean matchCode(String diffPath, String codePath) {
        ArrayList<String> diffStrList = handleDiff(diffPath);
        String codeStr = utils.readText(codePath);
        // TODO check是否包含该漏洞
        for (String string : diffStrList) {
            if (!codeStr.contains(string))
                return false;
        }
        return true;
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
}
