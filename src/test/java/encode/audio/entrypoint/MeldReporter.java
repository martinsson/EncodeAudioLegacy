package encode.audio.entrypoint;

import java.text.MessageFormat;

import org.approvaltests.reporters.GenericDiffReporter;

public class MeldReporter extends GenericDiffReporter {

	 private static final String        DIFF_PROGRAM = "/usr/local/bin/meld";
	  static final String                MESSAGE      = MessageFormat.format("Unable to find meld at {0}",
	                                                      DIFF_PROGRAM);
	  public static final MeldReporter INSTANCE     = new MeldReporter();
	  public MeldReporter()
	  {
	    super(DIFF_PROGRAM, "%s %s", MESSAGE, GenericDiffReporter.TEXT_FILE_EXTENSIONS);
	  }
	

}
