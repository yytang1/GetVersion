import java.util.ArrayList;

public class HandleVersion {

    static String all = "before 2.1.6, 2.2.x through 2.3.x, ";
    static String before = "before 2.1.6";
    static String through = "2.2.x through 2.3.x";
    static String abeforeB = "and 2.4.x before 2.4.4";

    static String Codepath = "C:\\Users\\wt\\Desktop\\ffmpeg";

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        getCodeVersion(before);
        getCodeVersion(through);
        getCodeVersion(abeforeB);
    }

    public static ArrayList<String> getCodeVersion(String excelVersion) {
        ArrayList<String> codeVersions = new ArrayList<String>();
        ArrayList<String> fileList = CheckDiff.getFileName(Codepath);
        // 无意义字符处理

        String[] versionStrs = excelVersion.split(" ");
        for (int i = 0; i < versionStrs.length; i++) {
            System.out.println(versionStrs[i]);
        }

        // before 2.1.6情况

        return codeVersions;
    }

    static// 过滤无意义词，比如and
    String filterStr(String versionStr) {
        String fiterVersion = "";
        String andStr = "and ";
        if (versionStr.contains(andStr)) {
            fiterVersion = versionStr.replaceAll(andStr, "");
        }
        return fiterVersion;
    }

    /**
     * 
     * @param version1
     *            格式例如3.0.5，只有两个如1.2 在末尾补零，变为1.2.5
     * @param version2
     * @return {@code version1} 大于 {@code version2} 返回 {@code true}
     */
    public boolean compareVersion(String version1, String version2) {
        return true;
    }

}
