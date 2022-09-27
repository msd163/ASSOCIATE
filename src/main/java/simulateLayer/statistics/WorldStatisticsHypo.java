package simulateLayer.statistics;

import utils.Config;

import java.util.Arrays;

public class WorldStatisticsHypo {

    public WorldStatisticsHypo(WorldStatisticsHypo xPrevStatHypo, WorldStatisticsHypo prevStatHypo) {

        this.xPrevStatHypo = xPrevStatHypo;
        this.prevStatHypo = prevStatHypo;

        allHypoFluctPosToNeg
                = allHypoFluctNegToPos
                = allHypoSuspectDiagnosis
                = allHypoIgnoredPos
                = allHypoIgnoredPosTruePositive
                = allHypoIgnoredNeg
                = allHypoIgnoredNegTruePositive
                = 0;

        allHypoResistanceByNumberAgainstNeg = new int[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        Arrays.fill(allHypoResistanceByNumberAgainstNeg, 0);

        allHypoResistanceByNumberAgainstPos = new int[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        Arrays.fill(allHypoResistanceByNumberAgainstPos, 0);

        avgHypoResistanceByNumberAgainstNeg = new int[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        Arrays.fill(avgHypoResistanceByNumberAgainstNeg, 0);

        ittHypoResistanceByNumberAgainstNeg = new int[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        Arrays.fill(ittHypoResistanceByNumberAgainstNeg, 0);

        ittHypoResistanceByNumberAgainstPos = new int[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        Arrays.fill(ittHypoResistanceByNumberAgainstPos, 0);

        avgHypoResistanceByNumberAgainstPos = new int[Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT];
        Arrays.fill(avgHypoResistanceByNumberAgainstPos, 0);

    }

    public void init(WorldStatisticsHypo hypo) {
        allHypoFluctPosToNeg = hypo.allHypoFluctPosToNeg;
        allHypoFluctNegToPos = hypo.allHypoFluctNegToPos;
        allHypoSuspectDiagnosis = hypo.allHypoSuspectDiagnosis;
        allHypoIgnoredPos = hypo.allHypoIgnoredPos;
        allHypoIgnoredPosTruePositive = hypo.allHypoIgnoredPosTruePositive;
        allHypoIgnoredNeg = hypo.allHypoIgnoredNeg;
        allHypoIgnoredNegTruePositive = hypo.allHypoIgnoredNegTruePositive;

        if (Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT >= 0) {
            System.arraycopy(hypo.allHypoResistanceByNumberAgainstNeg, 0, allHypoResistanceByNumberAgainstNeg, 0, Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT);
        }
        if (Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT >= 0) {
            System.arraycopy(hypo.allHypoResistanceByNumberAgainstPos, 0, allHypoResistanceByNumberAgainstPos, 0, Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT);
        }

        Arrays.fill(ittHypoResistanceByNumberAgainstNeg, 0);

        Arrays.fill(ittHypoResistanceByNumberAgainstPos, 0);

    }

    private final WorldStatisticsHypo xPrevStatHypo;      // the x previous statHypo for averaging. i.e. current-x
    private final WorldStatisticsHypo prevStatHypo;       // the previous statHypo. i.e. current-1

    private int allHypoFluctPosToNeg;                  // the number of changing SCORES from POSITIVE to NEGATIVE in [L''}
    private int allHypoFluctNegToPos;                  // the number of changing SCORES from NEGATIVE to POSITIVE in [L''}
    private int allHypoSuspectDiagnosis;               // the number of [L''] list that is labeled as Hypocrite list. the Trustee (responder) related to this list is suspected of being Hypocrite
    private int allHypoIgnoredPos;                     // the number of cases that the top value is POSITIVE but the final trust value is NEGATIVE
    private int allHypoIgnoredPosTruePositive;
    private int allHypoIgnoredNeg;                     // the number of cases that the top value is NEGATIVE but the final trust value is POSITIVE
    private int allHypoIgnoredNegTruePositive;

    // Resistance of POSITIVE scores against a NEGATIVE score
    private final int[] allHypoResistanceByNumberAgainstNeg;      // the all number of resistance occurred in specific RESISTANCE_NUMBER. The index of array indicates the resistance number, and values in any array[index] indicates the number of occurrences
    private final int[] ittHypoResistanceByNumberAgainstNeg;      // the current number of resistance occurred in specific RESISTANCE_NUMBER. The index of array indicates the resistance number, and values in any array[index] indicates the number of occurrences
    private final int[] avgHypoResistanceByNumberAgainstNeg;      // the average of resistance occurred in specific RESISTANCE_NUMBER. The index of array indicates the resistance number, and values in any array[index] indicates the number of occurrences

    // Resistance of NEGATIVE scores against a POSITIVE score
    private final int[] allHypoResistanceByNumberAgainstPos;      // the all number of resistance occurred in specific RESISTANCE_NUMBER. The index of array indicates the resistance number, and values in any array[index] indicates the number of occurrences
    private final int[] ittHypoResistanceByNumberAgainstPos;      // the current number of resistance occurred in specific RESISTANCE_NUMBER. The index of array indicates the resistance number, and values in any array[index] indicates the number of occurrences
    private final int[] avgHypoResistanceByNumberAgainstPos;      // the average of resistance occurred in specific RESISTANCE_NUMBER. The index of array indicates the resistance number, and values in any array[index] indicates the number of occurrences

    private int worldTime;                                  // time of world in this statistics

    //============================//============================
    private int calcAverage(int coeff, int currentVal, Integer xPrevVal) {

        if (xPrevVal == null) {
            return (int) ((float) currentVal / (worldTime == 0 ? 1 : worldTime));
        } else {
            return (int) ((coeff) * (float) (currentVal - xPrevVal) / Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE);
        }
    }

    private int calcAverage(int currentVal, Integer xPrevVal) {
        return calcAverage(1, currentVal, xPrevVal);
    }

    //============================//============================//============================

    public void setWorldTime(int worldTime) {
        this.worldTime = worldTime;
    }

    //============================//============================//============================


    public void add_allHypoResistanceByNumberAgainstNeg(int N) {
        if (N < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT) {
            allHypoResistanceByNumberAgainstNeg[N]++;
            ittHypoResistanceByNumberAgainstNeg[N]++;
        }
    }

    public void add_allHypoResistanceByNumberAgainstPos(int N) {
        if (N < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT) {
            allHypoResistanceByNumberAgainstPos[N]++;
            ittHypoResistanceByNumberAgainstPos[N]++;
        }
    }

    //============================

    public int[] getAvgHypoResistanceByNumberAgainstNeg() {

        if (xPrevStatHypo == null) {
            for (int i = 0; i < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; i++) {
                avgHypoResistanceByNumberAgainstNeg[i] = allHypoResistanceByNumberAgainstNeg[i] /
                        (worldTime == 0 ? 1 : worldTime);
            }
        } else {
            for (int i = 0; i < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; i++) {
                avgHypoResistanceByNumberAgainstNeg[i] =
                        (allHypoResistanceByNumberAgainstNeg[i] - xPrevStatHypo.allHypoResistanceByNumberAgainstNeg[i])
                                / Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE;
            }
        }
        return avgHypoResistanceByNumberAgainstNeg;
    }

    public int[] getAvgHypoResistanceByNumberAgainstPos() {

        if (xPrevStatHypo == null) {
            for (int i = 0; i < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; i++) {
                avgHypoResistanceByNumberAgainstPos[i] = allHypoResistanceByNumberAgainstPos[i] /
                        (worldTime == 0 ? 1 : worldTime);
            }
        } else {
            for (int i = 0; i < Config.STATISTICS_HYPOCRITE_RESISTANCE_COUNT; i++) {
                avgHypoResistanceByNumberAgainstPos[i] =
                        (allHypoResistanceByNumberAgainstPos[i] - xPrevStatHypo.allHypoResistanceByNumberAgainstPos[i])
                                / Config.STATISTICS_AVERAGE_TIME_WINDOW_FOR_RESISTANCE;
            }
        }
        return avgHypoResistanceByNumberAgainstPos;
    }

    public int[] getAvgHypoResistanceByNumberAgainstAll() {
        int[] hypoResistanceByNumberAgainstPos = getAvgHypoResistanceByNumberAgainstPos();
        int[] hypoResistanceByNumberAgainstNeg = getAvgHypoResistanceByNumberAgainstNeg();
        int len = hypoResistanceByNumberAgainstPos.length;
        int[] sum = new int[len];

        for (int i = 0; i < len; i++) {
            sum[i] = hypoResistanceByNumberAgainstPos[i] + hypoResistanceByNumberAgainstNeg[i];
        }
        return sum;
    }

    public int getIttHypoResistanceOfAllNumberAgainstNeg() {

        return Arrays.stream(ittHypoResistanceByNumberAgainstNeg).sum();
    }

    public int getAvgHypoResistanceOfAllNumberAgainstNeg() {

        return calcAverage(
                Arrays.stream(allHypoResistanceByNumberAgainstNeg).sum(),
                xPrevStatHypo == null ? null : Arrays.stream(xPrevStatHypo.allHypoResistanceByNumberAgainstNeg).sum()
        );
    }

    public int getIttHypoResistanceOfAllNumberAgainstPos() {

        return Arrays.stream(ittHypoResistanceByNumberAgainstPos).sum();
    }

    public int getAvgHypoResistanceOfAllNumberAgainstPos() {
        return calcAverage(
                Arrays.stream(allHypoResistanceByNumberAgainstPos).sum(),
                xPrevStatHypo == null ? null : Arrays.stream(xPrevStatHypo.allHypoResistanceByNumberAgainstPos).sum()
        );
    }

    public int getIttHypoResistanceOfAllNumberAgainstAll() {
        return getIttHypoResistanceOfAllNumberAgainstNeg() + getIttHypoResistanceOfAllNumberAgainstPos();
    }

    public int getAvgHypoResistanceOfAllNumberAgainstAll() {

        return calcAverage(
                Arrays.stream(allHypoResistanceByNumberAgainstNeg).sum() +
                        Arrays.stream(allHypoResistanceByNumberAgainstPos).sum()
                ,
                xPrevStatHypo == null ? null :
                        Arrays.stream(xPrevStatHypo.allHypoResistanceByNumberAgainstNeg).sum() +
                                Arrays.stream(xPrevStatHypo.allHypoResistanceByNumberAgainstPos).sum()
        );
    }

    public int getIttHypoResistancePerSuspected() {
        int sum = 0;
        for (int i : ittHypoResistanceByNumberAgainstNeg) {
            sum += i;
        }
        for (int i : ittHypoResistanceByNumberAgainstPos) {
            sum += i;
        }

        if (prevStatHypo == null) {
            if (allHypoSuspectDiagnosis == 0) {
                return 0;
            }

            return ((1000 * sum) / allHypoSuspectDiagnosis) / 1000;
        } else {
            if (allHypoSuspectDiagnosis - prevStatHypo.allHypoSuspectDiagnosis == 0) {
                return 0;
            }
            return ((1000 * sum) / (allHypoSuspectDiagnosis - prevStatHypo.allHypoSuspectDiagnosis)) / 1000;
        }
    }

    //============================//============================
    public void add_allHypoFluctNegToPos() {
        allHypoFluctNegToPos++;
    }

    public void add_allHypoFluctPosToNeg() {
        allHypoFluctPosToNeg++;
    }

    public void add_allHypoSuspectDiagnosis() {
        allHypoSuspectDiagnosis++;
    }

    public void add_allHypoIgnoredPos() {
        allHypoIgnoredPos++;
    }

    public void add_allHypoIgnoredPosTP() {
        allHypoIgnoredPosTruePositive++;
    }

    public void add_allHypoIgnoredNeg() {
        allHypoIgnoredNeg++;
    }

    public void add_allHypoIgnoredNegTP() {
        allHypoIgnoredNegTruePositive++;
    }

    //============================
    public int getIttHypoFluctPosToNeg() {
        if (prevStatHypo == null) {
            return allHypoFluctPosToNeg;
        }
        return allHypoFluctPosToNeg - prevStatHypo.allHypoFluctPosToNeg;
    }

    public int getAvgHypoFluctPosToNeg() {
        return calcAverage(allHypoFluctPosToNeg, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoFluctPosToNeg);
    }

    public int getIttHypoFluctNegToPos() {
        if (prevStatHypo == null) {
            return allHypoFluctNegToPos;
        }
        return allHypoFluctNegToPos - prevStatHypo.allHypoFluctNegToPos;
    }

    public int getAvgHypoFluctNegToPos() {
        return calcAverage(allHypoFluctNegToPos, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoFluctNegToPos);

    }

    public int getIttHypoFluct() {
        if (prevStatHypo == null) {
            return allHypoFluctNegToPos + allHypoFluctPosToNeg;
        }
        return allHypoFluctNegToPos + allHypoFluctPosToNeg - prevStatHypo.allHypoFluctNegToPos - prevStatHypo.allHypoFluctPosToNeg;
    }

    public int getAvgHypoFluct() {
        return getAvgHypoFluctNegToPos() + getAvgHypoFluctPosToNeg();

    }

    public int getIttHypoSuspectDiagnosis() {
        if (prevStatHypo == null) {
            return allHypoSuspectDiagnosis;
        }
        return allHypoSuspectDiagnosis - prevStatHypo.allHypoSuspectDiagnosis;
    }


    public int getAvgHypoSuspectDiagnosis() {
        return calcAverage(allHypoSuspectDiagnosis, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoSuspectDiagnosis);
    }

    public int getIttHypoIgnoredPos() {
        if (prevStatHypo == null) {
            return allHypoIgnoredPos;
        }
        return allHypoIgnoredPos - prevStatHypo.allHypoIgnoredPos;
    }

    public int getAvgHypoIgnoredPos() {
        return calcAverage(allHypoIgnoredPos, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoIgnoredPos);
    }

    public int getAvgIgnoredPosProportionToFluct1000() {
        int avgHypoFluct = getAvgHypoFluct();
        if (avgHypoFluct == 0) {
            return 0;
        }
        return 1000 * getAvgHypoIgnoredPos() / avgHypoFluct;
    }
    public int getAvgIgnoredNegProportionToFluct1000() {
        int avgHypoFluct = getAvgHypoFluct();
        if (avgHypoFluct == 0) {
            return 0;
        }
        return 1000 * getAvgHypoIgnoredNeg() / avgHypoFluct;
    }

    public int getIttHypoIgnoredNeg() {
        if (prevStatHypo == null) {
            return allHypoIgnoredNeg;
        }
        return allHypoIgnoredNeg - prevStatHypo.allHypoIgnoredNeg;
    }

    public int getAvgHypoIgnoredNeg() {
        return calcAverage(allHypoIgnoredNeg, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoIgnoredNeg);
    }

    public int getAllHypoIgnoredPosTruePositive() {
        return allHypoIgnoredPosTruePositive;
    }

    public int getAvgHypoIgnoredPosTruePositive() {
        return calcAverage(allHypoIgnoredPosTruePositive, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoIgnoredPosTruePositive);
    }

    public int getAllHypoIgnoredNegTruePositive() {
        return allHypoIgnoredNegTruePositive;
    }

    public int getAvgHypoIgnoredNegTruePositive() {
        return calcAverage(allHypoIgnoredNegTruePositive, xPrevStatHypo == null ? null : xPrevStatHypo.allHypoIgnoredNegTruePositive);
    }
}
