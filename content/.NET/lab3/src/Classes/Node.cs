namespace Classes;

public class Node<T>
{
    public T Value { get; set; }
    public Node<T>? Next { get; set; }

    public Node(T value)
    {
        Value = value;
        Next = null;
    }

    public static bool operator ==(Node<T>? left, Node<T>? right)
    {
        if (left is null && right is null)
            return true;

        if (left is null || right is null)
            return false;

        return (object)left == (object)right;
    }

    public static bool operator !=(Node<T>? left, Node<T>? right)
    {
        return !(left == right);
    }

    public override bool Equals(object? other)
    {
        if (other == null)
            return false;

        if (other is not Node<T>)
            return false;

        Node<T> node = (Node<T>)other;

        if (this.Value == null || node.Value == null)
            return false;

        return this.Value.Equals(node.Value);
    }

    public bool HasNext()
    {
        return Next != null;
    }

    public override int GetHashCode()
    {
        if (Value == null)
            return 0;

        return Value.GetHashCode();
    }

    public override string ToString()
    {
        if (Value == null)
            return "null";

        return Value.ToString();
    }
}