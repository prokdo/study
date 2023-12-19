using System;
using System.Text;

using Interfaces;

namespace Classes;

public class List<T> : IList<T>
{
    public Node<T>? Begin { get; protected set; }
    public int Count { get; protected set; }

    public List()
    {
        this.Begin = null;
        this.Count = 0;
    }

    public List(T value)
    {
        this.Begin = new Node<T>(value);
        this.Count = 1;
    }

    public List(T[] array)
    {
        if (array.Length > 0)
        {
            this.Begin = new Node<T>(array[0]);
            this.Count = 1;

            for (int i = 1; i < array.Length; i++)
            {
                this.Add(array[i]);
            }
        }
        else
        {
            this.Begin = null;
            this.Count = 0;
        }
    }

    public static bool operator ==(List<T>? left, List<T>? right)
    {
        if (left is null && right is null)
            return true;

        if (left is null || right is null)
            return false;

        return (object)left == (object)right;
    }

    public static bool operator !=(List<T>? left, List<T>? right)
    {
        return !(left == right);
    }

    public class ListEnumerator : IEnumerator
    {
        private Node<T>? Begin { get; set; }
        public object? Current { get; private set; }

        public ListEnumerator(Node<T>? Begin)
        {
            this.Begin = Begin;
            this.Current = null;
        }

        public bool MoveNext()
        {
            if (this.Current == null)
            {
                this.Current = this.Begin;
                return true;
            }
            else if (((Node<T>)this.Current).HasNext())
            {
                this.Current = ((Node<T>)this.Current).Next;
                return true;
            }
            else
            {
                return false;
            }
        }

        public void Reset()
        {
            this.Current = null;
        }

        public void Dispose()
        {
            this.Begin = null;
            this.Current = null;
        }
    }

    public IEnumerator GetEnumerator()
    {
        return new ListEnumerator(this.Begin);
    }

    public void Add(T value)
    {
        if (this.Begin == null)
        {
            this.Begin = new Node<T>(value);
        }
        else
        {
            ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (!((Node<T>)enumerator.Current).HasNext())
                {
                    ((Node<T>)enumerator.Current).Next = new Node<T>(value);
                    break;
                }
            }
        }

        this.Count++;
    }

    public void Clear()
    {
        this.Begin = null;
        this.Count = 0;
    }

    public bool Contains(T value)
    {
        ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Node<T>)enumerator.Current).Value.Equals(value))
            {
                return true;
            }
        }
        return false;
    }

    public void CopyTo(T[] array, Int32 arrayIndex)
    {
        ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            array[arrayIndex++] = ((Node<T>)enumerator.Current).Value;
        }
    }

    public bool Remove(T value)
    {
        if (this.Begin == null)
        {
            return false;
        }
        else if (this.Begin.Value.Equals(value))
        {
            this.Begin = this.Begin.Next;
            this.Count--;
            return true;
        }
        else
        {
            ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Node<T>)enumerator.Current).Next.Value.Equals(value))
                {
                    ((Node<T>)enumerator.Current).Next = ((Node<T>)enumerator.Current).Next.Next;
                    this.Count--;
                    return true;
                }
            }
            return false;
        }
    }

    public T this[Int32 index]
    {
        get
        {
            if (index < 0 || index >= this.Count)
            {
                throw new IndexOutOfRangeException();
            }
            else if (index == 0)
            {
                return this.Begin.Value;
            }
            else
            {
                int position = -1;
                ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
                while (enumerator.MoveNext())
                {
                    if (++position == index)
                    {
                        return ((Node<T>)enumerator.Current).Value;
                    }
                }
                throw new IndexOutOfRangeException();
            }
        }
        set
        {
            if (index < 0 || index >= this.Count)
            {
                throw new IndexOutOfRangeException();
            }
            else if (index == 0)
            {
                this.Begin.Value = value;
            }
            else
            {
                int position = -1;
                ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
                while (enumerator.MoveNext())
                {
                    if (++position == index)
                    {
                        ((Node<T>)enumerator.Current).Value = value;
                        break;
                    }
                }
            }
        }
    }

    public Int32 IndexOf(T value)
    {
        int position = -1;
        ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Node<T>)enumerator.Current).Value.Equals(value))
            {
                return ++position;
            }
            else
            {
                position++;
            }
        }
        return -1;
    }

    public void Insert(Int32 index, T value)
    {
        if (index < 0 || index >= this.Count)
        {
            throw new IndexOutOfRangeException();
        }
        else if (index == 0)
        {
            Node<T> node = new Node<T>(value);
            node.Next = this.Begin;
            this.Begin = node;
        }
        else
        {
            int position = -1;
            ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (++position == index)
                {
                    ((Node<T>)enumerator.Current).Next = new Node<T>(value);
                    ((Node<T>)enumerator.Current).Next.Next = ((Node<T>)enumerator.Current).Next;
                    break;
                }
            }
        }
        this.Count++;
    }

    public void RemoveAt(Int32 index)
    {
        if (index < 0 || index >= this.Count)
        {
            throw new IndexOutOfRangeException();
        }
        else if (index == 0)
        {
            this.Begin = this.Begin.Next;
        }
        else
        {
            int position = -1;
            ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (++position == index)
                {
                    ((Node<T>)enumerator.Current).Next = ((Node<T>)enumerator.Current).Next.Next;
                    break;
                }
            }
        }
        this.Count--;
    }

    public override bool Equals(object? obj)
    {
        if (obj == null || !(obj is List<T>))
            return false;

        List<T> list = (List<T>)obj;
        if (this.Count != list.Count)
            return false;

        ListEnumerator enumerator1 = (ListEnumerator)this.GetEnumerator();
        ListEnumerator enumerator2 = (ListEnumerator)list.GetEnumerator();
        while (enumerator1.MoveNext() && enumerator2.MoveNext())
        {
            if (!(((Node<T>)enumerator1.Current).Value.Equals(((Node<T>)enumerator2.Current).Value)))
            {
                return false;
            }
        }
        return true;
    }

    public override int GetHashCode()
    {
        int hash = 19;
        ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            hash = hash * 31 + ((Node<T>)enumerator.Current).Value.GetHashCode();
        }

        return hash;
    }

    public override string ToString()
    {
        StringBuilder result = new StringBuilder();
        result.Append("[");

        ListEnumerator enumerator = (ListEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            result.Append(((Node<T>)enumerator.Current).ToString());
            if (((Node<T>)enumerator.Current).HasNext())
            {
                result.Append(", ");
            }
            else
            {
                result.Append("]");
            }
        }
        return result.ToString();
    }
}