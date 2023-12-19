namespace lab1;

public class Program
{
    static int GetArrayLength()
    {
        while (true)
        {
            Console.Write("Введите длину исходного массива: ");

            int length;
            try { length = Convert.ToInt32(Console.ReadLine()); }
            catch (FormatException)
            {
                Console.WriteLine("\nНеверный формат ввода!\n");
                continue;
            }

            if (length <= 0)
            {
                Console.WriteLine("\nНеверный формат ввода!\n");
                continue;
            }

            return length;
        }
    }

    static double[] GetArray(int length)
    {
        double[] array = new double[length];

        for (int i = 0; i < length; i++)
        {
            while (true)
            {
                Console.Write(String.Format("Введите {0}-ый элемент массива: ", i + 1));

                try { array[i] = Convert.ToDouble(Console.ReadLine()); }
                catch (FormatException)
                {
                    Console.WriteLine("\nНеверный формат ввода!\n");
                    continue;
                }

                break;
            }

        }

        return array;
    }

    static void PrintArray(double[] array)
    {
        Console.Write(String.Format("[{0:0.00} ", array[0]));
        for (int i = 1; i < array.Length - 1; i++)
        {
            Console.Write(String.Format("{0:0.00} ", array[i]));
        }
        Console.Write(String.Format("{0:0.00}]\n", array[array.Length - 1]));
    }

    static void Swap<T>(T[] array, int i, int j)
    {
        (array[j], array[i]) = (array[i], array[j]);
    }

    static void InsertionSort(double[] array)
    {
        double x;
        int j;

        for (int i = 0; i < array.Length; i++)
        {
            x = array[i];
            j = i;
            while (j > 0 && array[j - 1] > x)
            {
                Swap(array, j, j - 1);
                j -= 1;
            }
            array[j] = x;
        }
    }

    static Tuple<int, double> FindMax(double[] array)
    {
        int max_ind = -1;
        double max_value = double.MinValue;

        for (int i = 0; i < array.Length; i++)
        {
            if (array[i] > max_value)
            {
                max_ind = i;
                max_value = array[i];
            }
        }

        return Tuple.Create(max_ind, max_value);
    }

    static double FindStandardMaxDeviation(double[] array)
    {
        double StandardMaxDeviation;

        Tuple<int, double> max = FindMax(array);

        double sum = 0;
        for (int i = 0; i < array.Length; i++)
        {
            if (i == max.Item1)
                continue;

            sum += Math.Pow((array[i] - max.Item2), 2.0);
        }

        StandardMaxDeviation = Math.Pow(sum / array.Length - 1, 0.5);

        return StandardMaxDeviation;
    }

    static void NormalizeArray(double[] array)
    {
        Tuple<int, double> max = FindMax(array);
        double StandardMaxDeviation = FindStandardMaxDeviation(array);

        for (int i = 0; i < array.Length; i++)
        {
            array[i] -= StandardMaxDeviation;
        }
    }

    static void Main(string[] args)
    {
        Console.WriteLine("Вариант №7. Прокопенко Д.О., ВПР32\n");

        int length = GetArrayLength();

        double[] array = GetArray(length);

        Console.Write("\nВведенный массив: ");
        PrintArray(array);

        InsertionSort(array);
        Console.Write("\nСортированный массив (Insertion Sort): ");
        PrintArray(array);


        Tuple<int, double> max = FindMax(array);
        Console.Write(String.Format("\nМаксимальный элемент: {0:0.00} \n\n", max.Item2));

        double StandardMaxDeviation = FindStandardMaxDeviation(array);
        Console.Write(String.Format("Максимальное стандартное отклонение: {0:0.00}\n\n", StandardMaxDeviation));

        NormalizeArray(array);
        Console.Write("Нормализованный массив: ");
        PrintArray(array);

        Console.ReadLine();
    }
}