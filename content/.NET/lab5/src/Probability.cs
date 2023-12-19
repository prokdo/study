using System;
using System.Collections.Generic;

using Utils.MyList;

namespace lab5;

public class Probability
{
    private Probability() 
    { 
    }

    public static MyList<Tuple<double, double>> CalculateProbability(IEnumerable<double> range)
    {
        MyList<Tuple<double, double>> result = new();

        int count = 0;
        MyList<double> valueList = new();
        foreach (var x in range)
        {
            if (!valueList.Contains(x))
            {
                valueList.Add(x);
            }
            count++;
        }

        valueList = valueList.GetSorted();
        foreach (var value in valueList)
        {
            int valueCount = 0;
            foreach (var x in range)
            {
                if (x == value)
                {
                    valueCount++;
                }
            }
            result.Add(new Tuple<double, double>(value, (double)valueCount / count));
        }

        return result;
    }

    public static double CalculateExpectedValue(IEnumerable<double> range)
    {
        var probabilityList = Probability.CalculateProbability(range);
        
        double result = 0;
        foreach (var x in probabilityList)
        {
            result += (x.Item1 * x.Item2);
        }

        return result;
    }

    public static double CalculateDispersion(IEnumerable<double> range)
    {
        var probabilityList = Probability.CalculateProbability(range);
        double expectedValue = Probability.CalculateExpectedValue(range);

        double result = 0;
        foreach (var x in probabilityList)
        {
            result += (Math.Pow(x.Item1 - expectedValue, 2) * x.Item2);
        }

        return result;
    }
}
