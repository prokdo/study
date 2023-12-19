using Classes;
using Structs;

namespace lab4;

public class Program
{
    public static void Main(string[] args)
    {
        Notificator listNotificator = new();
        Notificator queueNotificator = new();
        Notificator dictionaryNotificator = new();

        List<int> list = new();
        list.AddEvent += listNotificator.NotifyAdd<int>;
        list.RemoveEvent += listNotificator.NotifyRemove<int>;
        list.CopyToEvent += listNotificator.NotifyCopyTo<int>;
        list.ClearEvent += listNotificator.NotifyClear;

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
        queue.AddEvent += queueNotificator.NotifyAdd<int>;
        queue.RemoveEvent += queueNotificator.NotifyRemove<int>;
        queue.CopyToEvent += queueNotificator.NotifyCopyTo<int>;
        queue.ClearEvent += queueNotificator.NotifyClear;

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
        dictionary.AddEvent += dictionaryNotificator.NotifyAdd<KeyValuePair<string, int>>;
        dictionary.RemoveEvent += dictionaryNotificator.NotifyRemove<KeyValuePair<string, int>>;
        dictionary.CopyToEvent += dictionaryNotificator.NotifyCopyTo<KeyValuePair<string, int>>;
        dictionary.ClearEvent += dictionaryNotificator.NotifyClear;

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

        list.Clear();
        queue.Clear();
        dictionary.Clear();
    }
}