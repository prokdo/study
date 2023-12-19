using Classes.Papers;

namespace Classes.Stores;

public class Library
{
    public Dictionary<Object, int> Store { get; protected set; }

    public Library()
    {
        Store = new Dictionary<Object, int>();
    }

    public void Add(Object Object)
    {
        if (Object is Book || Object is Magazine || Object is Newspaper)
        {
            if (Store.ContainsKey(Object))
            {
                Store[Object]++;
            }
            else
            {
                Store.Add(Object, 1);
            }
        }
        else
        {
            throw new Exception("Invalid object type");
        }
    }

    public Object Give(Object Object)
    {
        if (Object is Book || Object is Magazine || Object is Newspaper)
        {
            if (Store.ContainsKey(Object))
            {
                if (Store[Object] > 0)
                {
                    Store[Object]--;
                    return Object;
                }
                else
                {
                    throw new Exception("Object is not in stock");
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
            Result += Item.Key + " - " + Item.Value + "\n";
        }
        return Result;
    }
}