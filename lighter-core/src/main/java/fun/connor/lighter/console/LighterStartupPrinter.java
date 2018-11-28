package fun.connor.lighter.console;

import java.io.PrintStream;

public class LighterStartupPrinter {

    private static final String BANNER =
            "" +
                   "  _      _       _     _            \n" +
                    " | |    (_)     | |   | |           \n" +
                    " | |     _  __ _| |__ | |_ ___ _ __ \n" +
                    " | |    | |/ _` | '_ \\| __/ _ \\ '__|\n" +
                    " | |____| | (_| | | | | ||  __/ |   \n" +
                    " |______|_|\\__, |_| |_|\\__\\___|_|   \n" +
                    "            __/ |                   \n" +
                    "           |___/  \n" +
                    " :: Lighter Framework ::    MVP Version";

    private PrintStream outStream;

    public LighterStartupPrinter(PrintStream outStream) {
        this.outStream = outStream;
    }

    public void printBanner() {
        outStream.println(BANNER);
    }
}
