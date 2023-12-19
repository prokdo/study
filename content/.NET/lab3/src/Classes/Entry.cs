namespace Classes;

public class Entry<TKey, TValue>
{
    public TKey Key { get; set; }
    public TValue Value { get; set; }
    public Entry<TKey, TValue>? Next { get; set; }

    public Entry(TKey key, TValue value)
    {
        Key = key;
        Value = value;
        Next = null;
    }

    public static bool operator ==(Entry<TKey, TValue>? left, Entry<TKey, TValue>? right)
    {
        if (left is null && right is null)
            return true;

        if (left is null || right is null)
            return false;

        return (object)left == (object)right;
    }

    public static bool operator !=(Entry<TKey, TValue>? left, Entry<TKey, TValue>? right)
    {
        return !(left == right);
    }

    public override bool Equals(object? other)
    {
        if (other == null)
            return false;

        if (other is not Entry<TKey, TValue>)
            return false;

        Entry<TKey, TValue> entry = (Entry<TKey, TValue>)other;

        if (this.Key == null || this.Value == null || entry.Key == null || entry.Value == null)
            return false;

        return this.Key.Equals(entry.Key) && this.Value.Equals(entry.Value);
    }

    public bool HasNext()
    {
        return Next != null;
    }

    public override int GetHashCode()
    {
        if (Key == null || Value == null)
            return 0;

        return Key.GetHashCode() ^ Value.GetHashCode();
    }

    public override string ToString()
    {
        if (Key == null || Value == null)
            return "null";

        return $"<{Key}, {Value}>";
    }
}