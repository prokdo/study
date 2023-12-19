namespace Utils.MyList; 

public class Node<T>
{
    public T Value { get; set; }
    public Node<T>? Next { get; set; }

    public Node()
    {
        Value = default;
        Next = null;
    }

    public Node(T value)
    {
        Value = value;
        Next = null;
    }

    public override string ToString()
    {
        return Value.ToString();
    }

    public override bool Equals(object? obj)
    {
        if (obj is Node<T> node)
        {
            return Value.Equals(node.Value);
        }

        return false;
    }

    public override int GetHashCode()
    {
        return Value.GetHashCode();
    }

    public static bool operator ==(Node<T>? left, Node<T>? right)
    {
        if (left is null && right is null)
        {
            return true;
        }
        else if (left is null || right is null)
        {
            return false;
        }

        return left.Equals(right);
    }

    public static bool operator !=(Node<T>? left, Node<T>? right)
    {
        return !(left == right);
    }
}
