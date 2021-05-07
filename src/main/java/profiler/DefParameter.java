package profiler;

import _type.TtParamType;
import static utils.Globals.RANDOM;

public class DefParameter {
    private TtParamType paramType;
    private int value;                  // this variable store parameter value.
    private int randRange;              // this variable store maximum number of random vars.
    private int upperBound;             // in range parameters upper bound will be stored here, of course result of range will be stored in value
    private int lowerBound;             // in range parameters lower bound will be stored here, of course result of range will be stored in value
    private int percentRefValue;        // in percent parameters this is reference value
    private int percentDesired;         // desired ref value

    //============================//============================
    public DefParameter() {
        value = 0;
    }

    public DefParameter(String paramFromTxtFile) {
        setParameter(paramFromTxtFile);
    }

    //============================//============================

    public void setParameter(String paramFromTxtFile) {
        char decision = paramFromTxtFile.charAt(0);
        switch (decision) {
            case '?': // this means this parameter has random variable
                String sub1 = paramFromTxtFile.substring(1, paramFromTxtFile.length());
                randRange = RANDOM.nextInt(Integer.parseInt(sub1)) + 1;
                paramType = TtParamType.Rand;
                break;
            case '#': // this means this parameter is range parameter.
                String[] desired_ranges = paramFromTxtFile
                        .replace("..", ".")
                        .split("[.#]+");
                range(Integer.parseInt(desired_ranges[1])
                        , Integer.parseInt(desired_ranges[2]));
                paramType = TtParamType.Range;
                break;
            case '%':// percent fraction of other parameter.
                String[] desired_var = paramFromTxtFile
                        .substring(1, paramFromTxtFile.length())
                        .split("@");
                percentage(Integer.parseInt(desired_var[0]), Integer.parseInt(desired_var[1]));
                paramType = TtParamType.Percent;
                break;
            default:  // this means this parameter has const value
                value = Integer.parseInt(paramFromTxtFile);
                paramType = TtParamType.Const;
                break;
        }
    }

    public void range(int low, int high) {
//        System.out.println(low + " " + high);
        upperBound = high;
        lowerBound = low;
    }

    public void percentage(int fraction, int ref_value) {
        percentDesired = fraction;
        percentRefValue = ref_value;
    }

    public int nextValue() {
        switch (paramType) {
            case Rand:
                value = RANDOM.nextInt(randRange);
                break;
            case Const:
                value = value;
                break;
            case Range:
                value = lowerBound + RANDOM.nextInt(upperBound - lowerBound);
                break;
            case Percent:
                value = (percentRefValue * percentDesired) / 100;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + paramType);
        }
        return value;
    }

    public String toString(int tabIndex) {
        tabIndex++;
        StringBuilder tx = new StringBuilder("\n");
        StringBuilder ti = new StringBuilder("\n");
        for (int i = 0; i <= tabIndex; i++) {
            if (i > 1) {
                tx.append("\t");
            }
            ti.append("\t");
        }
        return tx + "DefParameter{" +
                ti + "  paramType=" + paramType +
                ti + ", value=" + value +
                ti + ", randRange=" + randRange +
                ti + ", upperBound=" + upperBound +
                ti + ", lowerBound=" + lowerBound +
                ti + ", percent_ref_value=" + percentRefValue +
                ti + ", percent_desired=" + percentDesired +
                tx + '}';
    }

    @Override
    public String toString() {
        return toString(0);
    }
}
