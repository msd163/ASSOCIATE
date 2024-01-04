package core.utils;

import core.enums.TtParamType;
import static core.utils.Globals.RANDOM;

public class DefParameter {
    private TtParamType paramType;
    private int currentValue;                  // this variable store parameter value.
    private int randRange;              // this variable store maximum number of random vars.
    private int upperBound;             // in range parameters upper bound will be stored here, of course result of range will be stored in value
    private int lowerBound;             // in range parameters lower bound will be stored here, of course result of range will be stored in value
    private int percentRefValue;        // in percent parameters this is reference value
    private int percentDesired;         // desired ref value

    //============================//============================
    public DefParameter() {
        currentValue = 0;
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
                currentValue = Integer.parseInt(paramFromTxtFile);
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
                currentValue = RANDOM.nextInt(randRange + 1);
                break;
            case Const:
                currentValue = currentValue;
                break;
            case Range:
                currentValue = lowerBound + RANDOM.nextInt(upperBound - lowerBound + 1);
                break;
            case Percent:
                currentValue = (percentRefValue * percentDesired) / 100;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + paramType);
        }
        return currentValue;
    }


    public int getMaxValue() {
        switch (paramType) {
            case Rand:
                return randRange;
            case Const:
                return currentValue;
            case Range:
                return upperBound;
            case Percent:
                return percentRefValue;
            default:
                throw new IllegalStateException("Unexpected value: " + paramType);
        }
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
                ti + ", value=" + currentValue +
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

    public int getCurrentValue() {
        return currentValue;
    }
}
