using System;

using Utils;

namespace lab5;

public class Program
{
    public static void Main(string[] args)
    {
        int n;
        while (true)
        {
            Console.Write("> Enter n: ");
            if (int.TryParse(Console.ReadLine(), out n))
            {
                Console.WriteLine();
                break;
            }
            else
            {
                Console.WriteLine("Invalid input");
                Console.WriteLine();
            }
        }

        Console.WriteLine("IEnumerable:");
        int count = 1;
        foreach (var x in NumberGenerator.GenerateEnumerable(n))
        {
            if (count < n)
            {
                Console.Write($"{x}, ");
            }
            else
            {
                Console.Write(x);
            }

            count++;
        }

        Console.WriteLine();

        Console.WriteLine("Iterators:");
        var list = NumberGenerator.GenerateMyList(n);
        count = 1;
        foreach (var x in list)
        {
            if (count < n)
            {
                Console.Write($"{x}, ");
            }
            else
            {
                Console.Write(x);
            }

            count++;
        }

        Console.WriteLine();
        Console.WriteLine();

        list = NumberGenerator.GenerateMyList(n);
        Console.WriteLine("Range:");
        Console.WriteLine(list.ToString());

        Console.WriteLine("Probability:");
        var probabilityList = Probability.CalculateProbability(list);
        Console.WriteLine(probabilityList.ToString());

        Console.WriteLine($"Expected value: {Probability.CalculateExpectedValue(list)}");

        System.Console.WriteLine($"Dispersion: {Probability.CalculateDispersion(list)}");
    }
}