import org.apache.commons.io.FileUtils;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;

import java.io.FileWriter;
import java.io.IOException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreas on 20.06.2014.
 */
public class PrintFlarmNet {

    // todo -- use org.apache.commons.cli for command line input

    public static final String FLARMNET_ORG_FILES = "http://www.flarmnet.org/files/data.fln";
    public static final String DOWNLOAD_DATA_FLN = "data/download-data.fln";

    private static String apFilter = null;
    private static String csvFileName = null;

    private static void printCsvFile(List<FlarmNetData> recs, String fileName) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        try {
            fileWriter = new FileWriter(fileName);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            csvFilePrinter.printRecord("ID", "CALL", "CN", "TYPE");
            for (FlarmNetData rec : recs) {
                csvFilePrinter.printRecord(rec.getFlarmId(), rec.getRegistration().trim(), rec.getCallSign().trim(), rec.getType().trim());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<FlarmNetData> recs = null;
        if (args.length > 0 && args[0] != null && args[0].length() > 0) {
            apFilter = args[0];
        }
        if (args.length > 1 && args[1] != null && args[1].length() > 0) {
            csvFileName = args[1];
            recs = new ArrayList<FlarmNetData>();
        }

        try {
            FileUtils.copyURLToFile(new URL(FLARMNET_ORG_FILES), new File(DOWNLOAD_DATA_FLN));
            FileInputStream fs = new FileInputStream(DOWNLOAD_DATA_FLN);
            DataInputStream in = new DataInputStream(fs);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            byte[] bytes = new byte[172 / 2];
            int entries = 0;
            int entriesTotal = 0;
            while ((strLine = br.readLine()) != null) {
                if (strLine.length() == 172) {
                    for (int i = 0; i < bytes.length; i++) {
                        int idx = i * 2;
                        bytes[i] = Long.valueOf(strLine.substring(idx, idx + 2), 16).byteValue();
                    }
                    String dec = new String(bytes, Charset.forName("ISO-8859-1"));
                    FlarmNetData rec = new FlarmNetData(dec);
                    if (apFilter != null) {
                        if (rec.getAirport().equals(apFilter)) {
                            System.out.println(rec);
                            if (recs != null) {
                                recs.add(rec);
                            }
                            entries++;
                        }
                    } else {
                        System.out.println(rec);
                        entries++;
                    }
                    entriesTotal++;
                }
            }
            in.close();
            System.out.println("entries=" + entries + "/" + entriesTotal);
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        if (recs != null) {
            printCsvFile(recs, csvFileName);
        }
    }

}
