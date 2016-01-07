package GetVersion;

import java.io.File;

public class Common {
    public String markStr = "//***\r\n";
    public int txtMaxNum = 10;
    // 正则匹配
    public static String re_begin = "^";
    public static String re_end = "$";
    public static String re_version = "(([0-9]+\\.)*[0-9]+\\.)*[0-9]+";
    public static String re_version_x = "(([0-9]+\\.)*[0-9]+\\.)*([0-9]+|x)";
    public static String re_throughA = re_begin + "through\\s" + re_version + re_end;

    public static String re_AthroughB = re_begin + re_version_x + "\\sthrough\\s" + re_version_x
            + re_end;
    public static String re_beforeA = re_begin + "before\\s" + re_version + re_end;
    public static String re_AbeforeB = re_begin + re_version_x + "\\sbefore\\s" + re_version
            + re_end;
    public static String re_earlier = re_begin + re_version + "\\searlier" + re_end;

    public static String functionNameIsNull = "functionName is null";
    public static String reuseCode = "reuseCode";

    public String getCodefilePath(String codePath, String versionPrefix, String version,
            String fileName) {
        return codePath + File.separator + versionPrefix + version + File.separator + fileName;
    }

    public String getDifftxtPath(String diffPath, String cve) {
        return diffPath + File.separator + cve + ".txt";
    }

    /**
     * 获取复用实例代码的存放路径
     * 
     * @param path
     *            存放复用实例代码路径
     * @param cve
     *            漏洞cve号
     * @param functionName
     *            漏洞函数名
     * @return 返回路径 如：
     */
    public String getMarkFunctionPath(String path, String software, String cve, String functionName) {
        return path + File.separator + reuseCode + File.separator + cve + File.separator + software
                + File.separator + functionName + File.separator;
    }
}
