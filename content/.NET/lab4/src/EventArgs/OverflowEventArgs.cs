namespace EventArgs;

public class OverflowEventArgs<T> : AddEventArgs<T>
{
    private new int Index { get; set; }
    public OverflowEventArgs(T value) : base(value)
    {
    }
}
