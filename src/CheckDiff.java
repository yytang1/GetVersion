import java.io.File;
import java.util.ArrayList;

public class CheckDiff {
    static String Codepath = "C:\\Users\\wt\\Desktop\\ffmpeg";
    // excel 中 获取的函数文件名
    static String filePath = "libavcodec\\flashsv.c";
    static String diffPath = "C:\\Users\\wt\\Desktop\\实验室work-tyy\\需完成的工作\\测试数据\\Ffmpeg\\Ffmpeg-1.1diff";
    static String cve = diffPath + File.separator + "CVE-2013-7015.txt";

    static Utils utils = new Utils();

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        System.out.println("begin");
        ArrayList<String> versions = getVersionContainDiff();
        for (String version : versions) {
            System.out.println(version);
        }
        System.out.println("end");
    }

    public static ArrayList<String> getFileName(String filepath) {
        File file = new File(filepath);
        String[] filelist = file.list();
        ArrayList<String> filelists = new ArrayList<String>();

        // TODO 选出文件夹
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File(filepath + "\\" + filelist[i]);
            if (!readfile.isFile()) {
                filelists.add(filelist[i]);
            }
        }
        return filelists;
    }

    public static String getCodeFile(String fileName) {
        String content = "";
        String path = Codepath + File.separator + fileName + File.separator + filePath;
        System.out.println(path);
        content = utils.readText(path);
        return content;
    }

    public static ArrayList<String> getVersionContainDiff() {
        ArrayList<String> versionsTrue = new ArrayList<String>();
        // 获取diff代码
        String diff = HandleDiff.handleDiff(diffPath);
        // 获取所有版本文件夹名
        ArrayList<String> filelists = getFileName(Codepath);
        for (String file : filelists) {
            String codeStr = getCodeFile(file);
            // check是否包含该漏洞
            if (codeStr.contains(diff)) {
                versionsTrue.add(file);
            }
        }
        return versionsTrue;
    }
}
