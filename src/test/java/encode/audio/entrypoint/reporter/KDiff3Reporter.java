//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package encode.audio.entrypoint.reporter;

import org.approvaltests.reporters.GenericDiffReporter;

public class KDiff3Reporter extends GenericDiffReporter {
    public static String[] POSSIBLE_LOCATIONS = new String[]{"C:\\Program Files\\KDiff3\\kdiff3.exe", "C:\\Program Files (x86)\\KDiff3\\kdiff3.exe"};
    public static final KDiff3Reporter INSTANCE = new KDiff3Reporter();

    public KDiff3Reporter() {
        super(POSSIBLE_LOCATIONS, "\"%s\" \"%s\"");
    }
}