package GetVersion;

import java.util.ArrayList;

public class HandleVersion {

    static CheckDiff checkDiff = new CheckDiff();

    /**
     * 解析software version格式，获取满足条件的软件版本列，支持“ before 2.1.6, 2.2.x through
     * 2.3.x，and 2.4.x before 2.4.15”
     * 
     * @param versionList
     *            所有版本列
     * @param excelVersionStr
     *            版本区间，多个版本区间，以逗号区分
     * @return 满足版本区间版本列
     * @throws Exception
     */
    //
    public ArrayList<String> getCodeVersion(ArrayList<String> versionList, String excelVersionStr)
            throws Exception {

        ArrayList<String> codeVersions = new ArrayList<String>();
        // 无意义字符处理 String test = "2.0.0";
        // 多个版本区间，逗号划分
        String[] excelVersions = excelVersionStr.split(",");
        for (int i = 0; i < excelVersions.length; i++) {
            String excelVersion = excelVersions[i];
            excelVersion = filterStr(excelVersion);

            String[] versionStr = excelVersion.split(" ");
            // before 2.1.6情况
            if (versionStr.length == 2) {
                for (String string : versionList) {
                    if (compareVersion(string, versionStr[1]) < 0)
                        codeVersions.add(string);
                }
            }
            // 2.4.x before 2.4.4 情况
            if (versionStr.length == 3 && versionStr[1].equals("before")) {
                for (String string : versionList) {
                    if (compareVersion(string, versionStr[0]) == 0
                            && compareVersion(string, versionStr[2]) < 0)
                        codeVersions.add(string);
                }
            }
            // 2.2.x through 2.3.x 情况 // *
            if (versionStr.length == 3 && versionStr[1].equals("through")) {
                for (String string : versionList) {
                    if (compareVersion(string, versionStr[0]) == 0
                            || compareVersion(string, versionStr[2]) == 0)
                        codeVersions.add(string);
                }
            }
        }
        return codeVersions;
    }

    /**
     * 过滤无意义词，比如and；或者其他语义处理（待扩充）
     * 
     * @param versionStr
     *            需要过滤的原始版本区间
     * @return
     */
    String filterStr(String versionStr) {
        String fiterVersion = versionStr;
        String andStr = "and ";
        if (versionStr.contains(andStr)) {
            fiterVersion = versionStr.replaceAll(andStr, "");
        }
        return fiterVersion.trim();
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * 
     * @param version1
     *            2.0.1或者2.1.x
     * @param version2
     * @return 返回0，表示相等，比如2.1.x
     */
    public int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");// 注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);// 取最小长度值
        int diff = 0;
        // 比较前两个数字
        while (idx < minLength) {
            if ((versionArray1[idx].equals("x") || versionArray2[idx].equals("x"))) {
                diff = 0;
                break;
            }
            if ((diff = versionArray1[idx].length() - versionArray2[idx].length()) != 0) {
                break;
            }
            if ((diff = versionArray1[idx].compareTo(versionArray2[idx])) != 0) {
                break;
            }
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static void main(String[] args) throws Exception {
        String all = "before 2.1.6";
        // String before = "before 2.1.6";
        // String through = "2.2.x through 2.3.x";
        // String abeforeB = "and 2.4.x before 2.4.15";
        String Codepath = "C:\\Users\\yytang\\Desktop\\all\\tar文件\\";
        String versionPrefix = "ffmpeg-";
        HandleVersion handleVersion = new HandleVersion();
        ArrayList<String> fileList = checkDiff.getFileName(Codepath);

        fileList = checkDiff.getFileVersions(fileList, versionPrefix);
        System.out.println(fileList.size() + "\nbegin");

        ArrayList<String> versions = handleVersion.getCodeVersion(fileList, all);
        for (String string : versions) {
            System.out.println(string);
        }
        System.out.println("end");
    }
}
