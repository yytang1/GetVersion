package GetVersion;

public class HandleDiff {
    static Utils utils = new Utils();
    static String path = "C:\\Users\\yytang\\Desktop\\CVE-2010-1236补丁.txt";

    public static void main(String args[]) {
        HandleDiff handleDifftest = new HandleDiff();
        String content = handleDifftest.handleDiff(path);
        System.out.println(content);
    }

    public String handleDiff(String filePath) {
        String strFilter = "";
        String strOriginal = utils.readText(filePath);
        String[] strs = strOriginal.split("\r\n");
        for (int i = 0; i < strs.length; i++) {
            String temp = strs[i];
            if (temp.subSequence(0, 1).equals("-")) {
                temp = temp.substring(1);
            } else if (temp.subSequence(0, 1).equals("+") && !temp.contains("+++")) {
                continue;
            }
            strFilter += (temp + "\r\n");
        }
        return strFilter;
    }
}
