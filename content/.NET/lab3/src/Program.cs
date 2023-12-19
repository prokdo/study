using Classes;

namespace lab3;

public class Program
{
    public static void Main(string[] args)
    {
        List<int> list = new();
        list.Add(1);
        list.Add(2);
        list.Add(3);

        var listEnumerator = list.GetEnumerator();
        while (listEnumerator.MoveNext())
        {
            System.Console.Write(listEnumerator.Current + " ");
        }

        System.Console.Write($"Список: {list.ToString()} с количеством элементов {list.Count}");

        System.Console.WriteLine();

        Queue<int> queue = new();
        queue.Add(3);
        queue.Add(2);
        queue.Add(1);

        var queueEnumerator = queue.GetEnumerator();
        while (queueEnumerator.MoveNext())
        {
            System.Console.Write(queueEnumerator.Current + " ");
        }

        System.Console.Write($"Очередь: {queue.ToString()} с количеством элементов {queue.Count}");

        System.Console.WriteLine();

        Dictionary<string, int> dictionary = new();
        dictionary.Add("one", 1);
        dictionary.Add("two", 2);
        dictionary.Add("three", 3);

        var dictionaryEnumerator = dictionary.GetEnumerator();
        while (dictionaryEnumerator.MoveNext())
        {
            System.Console.Write(dictionaryEnumerator.Current + " ");
        }

        System.Console.Write($"Словарь: {dictionary.ToString()} с количеством элементов {dictionary.Count}");

        System.Console.WriteLine();
    }
}