package utils;
import static utils.Globals.*;

enum EeParamType
{
   Const,
   Percent,
   Range,
   Rand
};
public class DefParameter {
    public EeParamType paramType;
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
                paramType = EeParamType.Rand;
                break;
            case '#': // this means this parameter is range parameter.
                paramFromTxtFile.substring(1,paramFromTxtFile.length());
                String[] desired_ranges = paramFromTxtFile.split(".^2");
                range(Integer.parseInt(desired_ranges[0]),Integer.parseInt(desired_ranges[1]));
                paramType = EeParamType.Range;
                break;
            case '%':// percent fraction of other parameter.
                paramFromTxtFile.substring(1,paramFromTxtFile.length());
                String[] desired_var = paramFromTxtFile.split("@");
                percentage(Integer.parseInt(desired_var[0]),Integer.parseInt(desired_var[1]));
                paramType = EeParamType.Percent;
                break;
            default:  // this means this parameter has const value
                value = Integer.parseInt(paramFromTxtFile);
                paramType = EeParamType.Const;
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
