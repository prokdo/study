namespace EventArgs;

public class AddEventArgs<T> : System.EventArgs
{
    public int? Index { get; set; }
    public T Value { get; set; }

    public AddEventArgs(T value)
    {
        Index = null;
        Value = value;
    }

    public AddEventArgs(int index, T value)
    {
        Index = index;
        Value = value;
    }
}
