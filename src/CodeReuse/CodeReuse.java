package CodeReuse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GetVersion.Common;
import GetVersion.HandDiff;
import GetVersion.Utils;
import GetVersion.VulnerabilityInfo.VulnerInfo;

public class CodeReuse {
    Utils utils = new Utils();
    HandDiff handDiff = new HandDiff();
    Common common = new Common();

    public String getFunction(String filePath, String functionName) {
        // 读取文件源码
        String code = utils.readText(filePath);
        if (functionName.equals(Common.functionNameIsNull)) {
            return code;
        }

        String function = "";
        String re = "^[\\w\\s]+[\\s\\*]+" + functionName
        // + "\\s*\\([\\w\\[\\w\\]*\\s\\*\\,]*\\)\\s*\\{";
                + "\\s*\\([\\s\\S]*?\\)\\s*\\{";
        Pattern pattern = Pattern.compile(re, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            // 获取函数开始行号
            int start = matcher.start();
            int i = 0;
            int end = 0;
            int brackets = 0;
            for (i = matcher.start(); i < code.length(); i++) {
                if (code.charAt(i) == '{') {
                    brackets++;
                }
                if (code.charAt(i) == '}') {
                    brackets--;
                    if (brackets == 0)
                        break;
                }
            }
            end = i;
            function = code.substring(start, end + 1);
        }
        return function;
    }

    public ArrayList<String> getHalfMatchFile(String diffFilePath, String codePath,
            String versionPrefix, VulnerInfo vulnerInfo, ArrayList<String> versions,
            String resultPath) {
        ArrayList<String> reuseCodeList = new ArrayList<String>();
        ArrayList<String> reuseVersionList = new ArrayList<String>();
        String[] functionList = null;
        if (vulnerInfo.functionName == null) {
            functionList = new String[] { "functionName is null" };
        } else {
            functionList = vulnerInfo.functionName.split("[,，]");
        }
        System.out.println(Arrays.toString(functionList));
        // 获取diff文件
        ArrayList<String> diffList = handDiff.handleDiff(diffFilePath, vulnerInfo.fileName,
                vulnerInfo.functionName, true);

        for (String functionName : functionList) {
            System.out.println(functionName);
            int num = 0;
            for (String version : versions) {
                // 如果超过 10个 ，退出该函数
                if (num >= common.txtMaxNum)
                    break;
                String functionPath = common.getCodefilePath(codePath, versionPrefix, version,
                        vulnerInfo.fileName);
                String functionCode = getFunction(functionPath, functionName);
                if (functionCode.length() > 0) {
                    System.out.println(functionPath);
                }
                String path = common.getMarkFunctionPath(resultPath, vulnerInfo.softeware,
                        vulnerInfo.cve,
                        functionName.equals(Common.functionNameIsNull) ? vulnerInfo.fileName
                                : functionName);
                // 找到不完全匹配的函数
                int flag = 0;
                for (String diffStr : diffList) {
                    String markFunction = matchDiffLine(functionCode, diffStr);
                    // 存在不完全匹配的函数，将不完全匹配的函数，存到txt中
                    if (markFunction.contains(common.markStr)
                            && !reuseCodeList.contains(markFunction)) {
                        num++;
                        new File(path).mkdirs();
                        String filePath = path + version + ".txt";
                        if (utils.fileExist(filePath))
                            flag++;
                        filePath = flag > 0 ? (path + version + "(" + flag + ").txt") : path
                                + version + ".txt";
                        utils.writeText(markFunction, filePath, false);
                        reuseCodeList.add(markFunction);
                        reuseVersionList.add(version);
                        System.out.println(filePath);
                    }
                }
            }
        }
        return reuseVersionList;
    }

    public String matchDiffLine(String funciton, String diff) {
        String markFunction = "";
        String[] functionList = funciton.trim().split("\n");
        diff = common.handleLineBreak(diff);
        String[] diffList = diff.trim().split("\n");
        for (String string : diffList) {
            String temp = string.trim();
            if (temp.length() > 0) {
                for (int i = 0; i < functionList.length; i++) {
                    if (functionList[i].trim().equals(temp)) {
                        functionList[i] = common.markStr + functionList[i];
                    }
                }
            }
        }
        for (int i = 0; i < functionList.length; i++) {
            markFunction += (markFunction.length() > 0 ? "\r\n" + functionList[i] : functionList[i]);
        }
        return markFunction;
    }

    public static void main(String[] args) {
        CodeReuse codeReuse = new CodeReuse();
        HandDiff handDiff = new HandDiff();
        System.out.println("begin");
        String path = "C:\\Users\\yytang\\Desktop\\all\\tar文件\\ffmpeg-1.1.1\\";
        String fileName = "libavcodec\\gifdec.c";
        String functionName = "gif_decode_frame";
        String diffPath = "C:\\Users\\yytang\\Desktop\\all\\Ffmpeg-1.1diff\\CVE-2013-3673.txt";
        String function = codeReuse.getFunction(path + fileName, functionName);
        System.out.println(function);
        // 获取diff文件
        ArrayList<String> diffList = handDiff.handleDiff(diffPath, fileName, functionName, true);

        String returnStr = codeReuse.matchDiffLine(function, diffList.get(0));
        System.out.println(returnStr);
        String writePath = "C:\\Users\\yytang\\Desktop\\CVE-2013-3673.txt";
        codeReuse.utils.writeText(returnStr, writePath, false);
        System.out.println("end");
    }
}
