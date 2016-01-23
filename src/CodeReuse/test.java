package CodeReuse;

public class test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String mm = "stering ";
        String pp = "　yyy　";
        String uu = "ss";
        String teststr = "libavcodec/error_resilience.c ";
        int c = teststr.charAt(teststr.length() - 1);
        System.out.println(teststr.charAt(teststr.length() - 1));
        System.out.println(c);
        mm = mm.replaceAll("[ |　]", " ").trim();
        pp = pp.replaceAll("[ |　]", " ").trim();
        System.out.println(teststr.replaceAll("[ |　]", " ").replaceAll("\\u00A0", "").trim() + mm
                + pp + uu);

    }
}
