using System.Collections.Generic;

namespace Utils;

public class Sorter<T>
{
    private Sorter() { }

    // Bubble sort:
    public static T[] GetSorted(IEnumerable<T> enumerable, int count)
    {
        T[] result = new T[count];

        int pos = 0;
        foreach (T item in enumerable)
        {
            result[pos++] = item;

            if (pos <= 1)
                continue;

            for (int i = 0; i < pos - 1; i++)
                for (int j = 0; j < pos - 1; j++)
                    if (Comparer<T>.Default.Compare(result[j], result[j + 1]) >= 0)
                        (result[j + 1], result[j]) = (result[j], result[j + 1]);
        }

        return result;
    }
}
