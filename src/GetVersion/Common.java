package GetVersion;

import java.io.File;

public class Common {
    public String markStr = "//***\r\n";
    public int txtMaxNum=10;
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
    public String getMarkFunctionPath(String path, String cve, String functionName) {
        return path + File.separator + cve + File.separator + functionName + File.separator;
    }

}
