/**
 * Created by andreas on 20.06.2014.
 */
public class FlarmNetData {

    public static final String SEP = "|";
    private String flarmId;
    private String name;
    private String airport;
    private String type;
    private String registration;
    private String callSign;
    private String frequency;
    private String region;

    public String getAirport() {
        return airport.trim();
    }

    public String getFlarmId() {
        return flarmId;
    }

    public String getRegistration() {
        return registration;
    }

    public String getType() {
        return type;
    }

    public String getCallSign() {
        return callSign;

    }

    public FlarmNetData(String rawLine) {
        // "4B5171Andreas Lüthi        LSTB                 Discus 2             HB-3274UB 122.375"
        flarmId = rawLine.substring(0, 6);
        name = rawLine.substring(6, 27);
        airport = rawLine.substring(27, 48);

        type = rawLine.substring(48, 69);
        registration = rawLine.substring(69, 76);
        callSign = rawLine.substring(76, 79);
        frequency = rawLine.substring(79);
        region = "UNKNOWN";
    }

    public String toString() {
        return flarmId + SEP + name + SEP + airport + SEP + type + SEP + registration + SEP + callSign + SEP + frequency;
    }
}
