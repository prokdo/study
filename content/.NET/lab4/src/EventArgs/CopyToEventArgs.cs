namespace EventArgs;

public class CopyToEventArgs<T> : System.EventArgs
{
    public T[] Array { get; set; }
    public int Index { get; set; }

    public CopyToEventArgs(T[] array, int index)
    {
        Array = array;
        Index = index;
    }
}

