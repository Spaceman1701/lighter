package fun.connor.lighter.console;

import java.io.PrintStream;

public class LighterStartupPrinter {

    private static final String BANNER =
            "\n" +
                    "██╗     ██╗ ██████╗ ██╗  ██╗████████╗███████╗██████╗ \n" +
                    "██║     ██║██╔════╝ ██║  ██║╚══██╔══╝██╔════╝██╔══██╗\n" +
                    "██║     ██║██║  ███╗███████║   ██║   █████╗  ██████╔╝\n" +
                    "██║     ██║██║   ██║██╔══██║   ██║   ██╔══╝  ██╔══██╗\n" +
                    "███████╗██║╚██████╔╝██║  ██║   ██║   ███████╗██║  ██║\n" +
                    "╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝\n" +
                    "                                                     \n" +
                    " ::Lighter Framework                   (MVP version) \n";

    private PrintStream outStream;

    public LighterStartupPrinter(PrintStream outStream) {
        this.outStream = outStream;
    }

    public void printBanner() {
        outStream.println(BANNER);
    }
}
