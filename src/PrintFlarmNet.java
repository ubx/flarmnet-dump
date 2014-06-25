import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by andreas on 20.06.2014.
 */
public class PrintFlarmNet {

    public static final String FLARMNET_ORG_FILES = "http://www.flarmnet.org/files/data.fln";
    public static final String DOWNLOAD_DATA_FLN = "data/download-data.fln";


    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            FileUtils.copyURLToFile(new URL(FLARMNET_ORG_FILES), new File(DOWNLOAD_DATA_FLN));
            FileInputStream fs = new FileInputStream(DOWNLOAD_DATA_FLN);
            DataInputStream in = new DataInputStream(fs);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            byte[] bytes = new byte[172 / 2];
            int entries = 0;
            while ((strLine = br.readLine()) != null) {
                if (strLine.length() == 172) {
                    for (int i = 0; i < bytes.length; i++) {
                        int idx = i * 2;
                        bytes[i] = Long.valueOf(strLine.substring(idx, idx + 2), 16).byteValue();
                    }
                    String dec = new String(bytes, Charset.forName("ISO-8859-1"));
                    FlarmNetData rec = new FlarmNetData(dec);
                    if (args.length > 0 && args[0] != null && args[0].length() > 0) {
                        if (rec.getAirport().equals(args[0])) {
                            System.out.println(rec);
                            entries++;
                        }
                    } else {
                        System.out.println(rec);
                        entries++;
                    }
                }
            }
            in.close();
            System.out.println("entries=" + entries);
        } catch (Exception e) {// Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

}
