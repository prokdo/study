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
            System.Console.WriteLine($"Элемент {args.Value} был добавлен в список {collection.ToString()}");

            return;
        }

        System.Console.WriteLine($"Элемент {args.Value} был добавлен в список {collection.ToString()} на позицию {args.Index}");
    }

    public void NotifyRemove<T>(ICollection<T> collection, RemoveEventArgs<T> args)
    {
        if (args.Index == null)
        {
            System.Console.WriteLine($"Элемент {args.Value} был удален из списка {collection.ToString()}");

            return;
        }

        System.Console.WriteLine($"Элемент под номером {args.Index} был удален из списка {collection.ToString()}");
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
}

