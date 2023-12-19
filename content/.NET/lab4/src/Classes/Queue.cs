using System;
using System.Text;

using Interfaces;
using EventArgs;

namespace Classes;

public class Queue<T> : ICollection<T>
{
    public Node<T>? Begin { get; protected set; }
    public Node<T>? End { get; protected set; }
    public int Count { get; protected set; }

    public Queue()
    {
        this.Begin = null;
        this.End = null;
        this.Count = 0;
    }

    public Queue(T value)
    {
        this.Begin = new Node<T>(value);
        this.End = this.Begin;
        this.Count = 1;
    }

    public Queue(T[] array)
    {
        if (array.Length > 0)
        {
            this.Begin = new Node<T>(array[0]);
            this.End = this.Begin;
            this.Count = 1;

            for (int i = 1; i < array.Length; i++)
            {
                this.Add(array[i]);
            }
        }
        else
        {
            this.Begin = null;
            this.End = null;
            this.Count = 0;
        }
    }

    public static bool operator ==(Queue<T>? left, Queue<T>? right)
    {
        if (left is null && right is null)
            return true;

        if (left is null || right is null)
            return false;

        return (object)left == (object)right;
    }

    public static bool operator !=(Queue<T>? left, Queue<T>? right)
    {
        return !(left == right);
    }

    public class QueueEnumerator : IEnumerator
    {
        private Node<T>? Begin { get; set; }
        private Node<T>? End { get; set; }
        public object? Current { get; private set; }

        public QueueEnumerator(Node<T>? Begin, Node<T>? End)
        {
            if (Begin == null && End != null || Begin != null && End == null)
                throw new ArgumentException("Begin and End must be either both null or both not null");

            this.Begin = Begin;
            this.End = End;
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
            this.End = null;
            this.Current = null;
        }
    }

    public event ICollection<T>.AddEventHandler? AddEvent;
    public event ICollection<T>.RemoveEventHandler? RemoveEvent;
    public event ICollection<T>.CopyToEventHandler? CopyToEvent;
    public event ICollection<T>.ClearEventHandler? ClearEvent;

    public IEnumerator GetEnumerator()
    {
        return new QueueEnumerator(this.Begin, this.End);
    }

    public void Add(T value)
    {
        if (this.Begin == null)
        {
            this.Begin = new Node<T>(value);
            this.End = this.Begin;
        }
        else
        {
            this.End.Next = new Node<T>(value);
            this.End = this.End.Next;
        }

        this.Count++;

        AddEvent?.Invoke(this, new AddEventArgs<T>(value));
    }

    public void Clear()
    {
        ClearEvent?.Invoke(this);

        this.Begin = null;
        this.End = null;
        this.Count = 0;
    }

    public bool Contains(T value)
    {
        QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Node<T>)enumerator.Current).Value.Equals(value))
            {
                return true;
            }
        }
        return false;
    }

    public void CopyTo(T[] array, int arrayIndex)
    {
        CopyToEvent?.Invoke(this, new CopyToEventArgs<T>(array, arrayIndex));

        QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
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

            RemoveEvent?.Invoke(this, new RemoveEventArgs<T>(value));

            return true;
        }
        else if (this.End.Value.Equals(value))
        {
            QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Node<T>)enumerator.Current).Next.Equals(this.End))
                {
                    ((Node<T>)enumerator.Current).Next = null;
                    this.End = (Node<T>)enumerator.Current;
                    this.Count--;

                    RemoveEvent?.Invoke(this, new RemoveEventArgs<T>(value));

                    return true;
                }
            }
            return false;
        }
        else
        {
            QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Node<T>)enumerator.Current).Next.Value.Equals(value))
                {
                    ((Node<T>)enumerator.Current).Next = ((Node<T>)enumerator.Current).Next.Next;
                    this.Count--;

                    RemoveEvent?.Invoke(this, new RemoveEventArgs<T>(value));

                    return true;
                }
            }
            return false;
        }
    }

    public T? PopBegin()
    {
        if (this.Begin == null)
        {
            return default(T);
        }
        else
        {
            T? value = this.Begin.Value;
            this.Begin = this.Begin.Next;
            this.Count--;

            RemoveEvent?.Invoke(this, new RemoveEventArgs<T>(value));

            return value;
        }
    }

    public T? PopEnd()
    {
        if (this.Begin == null)
        {
            return default(T);
        }
        else if (this.Begin == this.End)
        {
            T? value = this.Begin.Value;
            this.Begin = null;
            this.End = null;
            this.Count--;

            RemoveEvent?.Invoke(this, new RemoveEventArgs<T>(value));

            return value;
        }
        else
        {
            QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Node<T>)enumerator.Current).Next == this.End)
                {
                    T? value = this.End.Value;
                    ((Node<T>)enumerator.Current).Next = null;
                    this.End = (Node<T>)enumerator.Current;
                    this.Count--;

                    RemoveEvent?.Invoke(this, new RemoveEventArgs<T>(value));

                    return value;
                }
            }
            return default(T);
        }
    }

    public override int GetHashCode()
    {
        int hash = 19;
        QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            hash = hash * 31 + ((Node<T>)enumerator.Current).Value.GetHashCode();
        }

        return hash;
    }

    public override bool Equals(object? obj)
    {
        if (obj == null || !(obj is Queue<T>))
            return false;

        Queue<T> queue = (Queue<T>)obj;
        if (this.Count != queue.Count)
            return false;

        QueueEnumerator enumerator1 = (QueueEnumerator)this.GetEnumerator();
        QueueEnumerator enumerator2 = (QueueEnumerator)queue.GetEnumerator();
        while (enumerator1.MoveNext() && enumerator2.MoveNext())
        {
            if (!((Node<T>)enumerator1.Current).Value.Equals(((Node<T>)enumerator2.Current).Value))
            {
                return false;
            }
        }
        return true;
    }

    public override string ToString()
    {
        StringBuilder result = new StringBuilder();
        result.Append("[");

        QueueEnumerator enumerator = (QueueEnumerator)this.GetEnumerator();
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