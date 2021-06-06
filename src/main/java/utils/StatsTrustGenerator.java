package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StatsTrustGenerator {

    File file;
    FileWriter writer;

    public void init(String statPath) {
        file = new File(statPath);

        try {
            writer = new FileWriter(file);
        } catch (IOException ignored) {
        }
    }

    public void addComment() {
        try {
            writer.write("\n# Trust report.,,,,,,,,,,,\n# Date:," + ParsCalendar.getInstance().getShortDateTime() + ",,,,,,,,,,");
            writer.flush();

        } catch (IOException ignored) {
        }
    }

    public void addHeader() {
        try {
            writer.write(getCsvHeader());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStat(WorldStatistics stat) {
        try {
            writer.write("\n" + getCsvRec(stat));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCsvHeader() {
        return
                "WorldTime," +
                        "IttTrustToHonest," +
                        "IttTrustToDishonest"

                ;
    }

    private String getCsvRec(WorldStatistics stat) {
        return
                stat.getWorldTime() + "," +
                        stat.getIttTrustToHonest() + "," +
                        stat.getIttTrustToDishonest()

                ;
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}