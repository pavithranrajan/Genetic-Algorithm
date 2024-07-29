import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;


/*Method to convert the input txt file to csv file for easy reading*/
public class FileConverter {
    public static Path convertTxtToCsv(String pathName) throws IOException {
        String[] filenameWithPath = pathName.split("\\.");
        String[] filename = filenameWithPath[0].split("/");
        final Path path = Paths.get("src");
        final Path txt = path.resolve(filename[1] + ".txt");
        final Path csv = path.resolve(filename[1] + ".csv");
        final Charset utf8 = Charset.forName("UTF-8");
        try (
                final Scanner scanner = new Scanner(Files.newBufferedReader(txt, utf8));
                final PrintWriter pw = new PrintWriter(Files.newBufferedWriter(csv, utf8, StandardOpenOption.CREATE_NEW))) {
            while (scanner.hasNextLine()) {
                pw.println(scanner.nextLine());
            }
        }
        return path.resolve(filename+".csv");
    }
}
