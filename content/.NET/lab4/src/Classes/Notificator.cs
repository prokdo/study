using EventArgs;
using Interfaces;

namespace Classes;
public class Notificator
{
    public Notificator() { }

    public void NotifyAdd<T>(ICollection<T> collection, AddEventArgs<T> args)
    {
        if (args.Index == null)
        {
            System.Console.WriteLine($"Элемент {args.Value} был добавлен в коллекцию {collection.ToString()}");

            return;
        }

        System.Console.WriteLine($"Элемент {args.Value} был добавлен в коллекцию {collection.ToString()} на позицию {args.Index}");
    }

    public void NotifyRemove<T>(ICollection<T> collection, RemoveEventArgs<T> args)
    {
        if (args.Index == null)
        {
            System.Console.WriteLine($"Элемент {args.Value} был удален из коллекции {collection.ToString()}");

            return;
        }

        System.Console.WriteLine($"Элемент под номером {args.Index} был удален из коллекции {collection.ToString()}");
    }

    public void NotifyCopyTo<T>(ICollection<T> collection, CopyToEventArgs<T> args)
    {
        if (args.Index == 0)
        {
            System.Console.WriteLine($"Коллекция {collection.ToString()} скопирована в массив");

            return;
        }

        System.Console.WriteLine($"Коллекция {collection.ToString()} скопирована в массив {args.Array} с начальным индексом {args.Index}");
    }

    public void NotifyClear<T>(ICollection<T> collection)
    {
        System.Console.WriteLine($"Коллекция {collection} обнулена");
    }

    public void NotifyQueueOverflow<T>(Queue<T> queue, OverflowEventArgs<T> args)
    {
        System.Console.WriteLine($"Очередь {queue} размером {queue.Count} переполнена, невозможно добавить элемент {args.Value}");
    }
}

