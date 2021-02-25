package main.java.utils;
import main.java._type.TtParamType;
import static utils.Globals.*;




public class DefParameter {
    public TtParamType paramType;
    public int value ; // this variable store parameter value.
    public int upperBound ; // in range parameters upper bound will be stored here, of course result of range will be stored in value
    public int lowerBound ; // in range parameters lower bound will be stored here, of course result of range will be stored in value
    public int percent_ref_value; // in percent parameters this is reference value
    public int percent_desired; // desired ref value
    public DefParameter()
    {
        value = 0;
    }
    public DefParameter(String paramFromTxtFile)
    {

        char decision = paramFromTxtFile.charAt(0);
        switch (decision)
        {
            case '?': // this means this parameter has random variable
                paramFromTxtFile.substring(1,paramFromTxtFile.length());
                value = RANDOM.nextInt(Integer.parseInt(paramFromTxtFile));
                paramType = TtParamType.Rand;
                break;
            case '#': // this means this parameter is range parameter.
                paramFromTxtFile.substring(1,paramFromTxtFile.length());
                String[] desired_ranges = paramFromTxtFile.split(".^2");
                range(Integer.parseInt(desired_ranges[0]),Integer.parseInt(desired_ranges[1]));
                paramType = TtParamType.Range;
                break;
            case '%':// percent fraction of other parameter.
                String substring = paramFromTxtFile.substring(1, paramFromTxtFile.length());
                String[] desired_var = substring.split("@");
                percentage(Integer.parseInt(desired_var[0]),Integer.parseInt(desired_var[1]));
                paramType = TtParamType.Percent;
                break;
            default:  // this means this parameter has const value
                value = Integer.parseInt(paramFromTxtFile);
                paramType = TtParamType.Const;
                break;
        }
    }
    public void range(int low, int high)
    {
        upperBound = high;
        lowerBound = low;
    }
    public void percentage(int fraction, int ref_value)
    {
        percent_desired = fraction;
        percent_ref_value= ref_value;
    }
    public int nextValue() {
        switch (paramType) {
            case Rand:
                value = RANDOM.nextInt();
                break;
            case Const:
                value = value;
                break;
            case Range:
                value = lowerBound + RANDOM.nextInt(upperBound-lowerBound);
                break;
            case Percent:
                value = (percent_ref_value * percent_desired) / 100;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + paramType);
        }
        return  value;
    }
}
