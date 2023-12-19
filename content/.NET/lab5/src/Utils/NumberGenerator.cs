using System;
using System.Collections.Generic;

using Utils.MyList;

namespace Utils;

public class NumberGenerator
{
    private static readonly Random RANDOM = new();

    private NumberGenerator()
    {

    }

    public static double Generate(int minValue, int maxValue, int? precision)
    {
        if (minValue >= maxValue)
        {
            throw new ArgumentException("minValue cannot be equal to maxValue or greater than it");
        }
        else if (precision.HasValue && precision < 0)
        {
            throw new ArgumentException("Precision cannot be negative");
        }

        if (!precision.HasValue || precision == 0)
        {
            return RANDOM.Next(minValue, maxValue + 1);
        }
        
        int integer = RANDOM.Next(minValue, maxValue + 1);
        int fraction = RANDOM.Next(100);
        int power = (int) Math.Pow(10, precision.Value);

        if (integer * fraction >= 0)
        {
            return (double) integer + (double) fraction / (double) power;
        }

        return (-1) * ((double) Math.Abs(integer) + (double) fraction / (double) power);
    }

    public static IEnumerable<double> GenerateEnumerable(int n)
    {
        if (n <= 0)
        {
            throw new ArgumentException("Argument must be a natural number");
        }

        for (var i = 0; i < n; i++)
            yield return NumberGenerator.Generate(1, 5, 0);
    }

    public static IEnumerable<double> GenerateMyList(int n)
    {
        if (n <= 0)
        {
            throw new ArgumentException("Argument must be a natural number");
        }

        return new MyList<double>(NumberGenerator.GenerateEnumerable(n));
    }
}