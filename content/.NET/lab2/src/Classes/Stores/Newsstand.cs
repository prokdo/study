using Classes.Papers;

namespace Classes.Stores;

public class Newsstand
{
    public Dictionary<Object, int> Store { get; protected set; }

    public Newsstand()
    {
        Store = new Dictionary<Object, int>();
    }

    public void Add(Object Object, int Cost)
    {
        if (Object is Magazine || Object is Newspaper)
        {
            if (Store.ContainsKey(Object))
            {
                if (Store[Object] < Cost)
                {
                    Store[Object] = Cost;
                }
            }
            else
            {
                Store.Add(Object, Cost);
            }
        }
        else
        {
            throw new Exception("Invalid object type");
        }
    }

    public Object Sell(Object Object, int Cost)
    {
        if (Object is Magazine || Object is Newspaper)
        {
            if (Store.ContainsKey(Object))
            {
                if (Store[Object] == Cost)
                {
                    Store.Remove(Object);
                    return Object;
                }
                else
                {
                    throw new Exception("Invalid cost");
                }
            }
            else
            {
                throw new Exception("Object is not in stock");
            }
        }
        else
        {
            throw new Exception("Invalid object type");
        }
    }

    override public string ToString()
    {
        string Result = "";
        foreach (KeyValuePair<Object, int> Item in Store)
        {
            Result += Item.Key.ToString() + " - " + Item.Value.ToString() + "\n";
        }
        return Result;
    }
}