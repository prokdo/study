namespace EventArgs;

    public class RemoveEventArgs<T>: System.EventArgs
    {
        public int? Index { get; set; }
        public T? Value { get; set; }

        public RemoveEventArgs(int index)
        {
            Index = index;
            Value = default;
        }

        public RemoveEventArgs(T value)
        {
            Index = null;
            Value = value;
        }
    }

