using System;
using System.Collections;
using System.Collections.Generic;

namespace Utils.MyList;

public class MyList<T> : IList<T>
{
    public Node<T>? Begin { get; private set; }

    public int Count { get; private set; }

    public bool IsReadOnly { get; set; }

    public T this[int index] 
    { 
        get
        {
            if (index < 0 || index >= Count)
            {
                throw new ArgumentOutOfRangeException(nameof(index));
            }

            Node<T>? current = Begin;
            for (var i = 0; i < index; i++)
            {
                current = current!.Next;
            }

            return current!.Value;
        }
        set
        {
            if (index < 0 || index >= Count)
            {
                throw new ArgumentOutOfRangeException(nameof(index));
            }

            Node<T>? current = Begin;
            for (var i = 0; i < index; i++)
            {
                current = current!.Next;
            }

            current!.Value = value;
        }
    }

    protected class ListEnumerator : IEnumerator<T>
    {
        public Node<T>? _current;
        private readonly Node<T>? _begin;

        public ListEnumerator(Node<T>? begin)
        {
            _begin = begin;
            _current = null;
        }

        public T Current => _current!.Value;

        object? IEnumerator.Current => Current;

        public void Dispose()
        {
            _current = null;
        }

        public bool MoveNext()
        {
            if (_current == null)
            {
                _current = _begin;
                return true;
            }

            if (_current.Next != null)
            {
                _current = _current.Next;
                return true;
            }

            return false;
        }

        public void Reset()
        {
            _current = null;
        }
    }

    public IEnumerator<T> GetEnumerator()
    {
        return new ListEnumerator(Begin);
    }

    public MyList()
    {
        Begin = null;
        Count = 0;
        IsReadOnly = false;
    }

    public MyList(T[] array)
    {
        if (array.Length == 0)
        {
            Begin = null;
            Count = 0;
            IsReadOnly = false;
        }
        else
        {
            Begin = new Node<T>(array[0]);
            Node<T>? current = Begin;
            for (var i = 1; i < array.Length; i++)
            {
                current!.Next = new Node<T>(array[i]);
                current = current.Next;
            }

            Count = array.Length;
            IsReadOnly = false;
        }
    }

    public MyList(IEnumerable<T> collection)
    {
        if (collection == null)
        {
            throw new ArgumentNullException(nameof(collection));
        }

        Begin = null;
        Count = 0;
        IsReadOnly = false;

        foreach (var item in collection)
        {
            Add(item);
        }
    }

    public void Add(T item)
    {
        if (IsReadOnly)
        {
            throw new NotSupportedException();
        }

        if (Begin == null)
        {
            Begin = new Node<T>(item);
            this.Count++;
        }
        else
        {
            Node<T>? current = Begin;
            while (current!.Next != null)
            {
                current = current.Next;
            }

            current.Next = new Node<T>(item);
            this.Count++;
        }
    }

    public void Clear()
    {
        if (!IsReadOnly)
        {
            Begin = null;
            Count = 0;
        }
        else throw new NotSupportedException();
    }

    public bool Contains(T item)
    {
        Node<T>? current = Begin;
        while (current != null)
        {
            if (current.Value.Equals(item))
            {
                return true;
            }

            current = current.Next;
        }

        return false;
    }

    public void CopyTo(T[] array, int arrayIndex)
    {
        if (arrayIndex < 0 || arrayIndex >= array.Length)
        {
            throw new ArgumentOutOfRangeException(nameof(arrayIndex));
        }

        if (array.Length - arrayIndex < Count)
        {
            throw new ArgumentException("The number of elements in the source ICollection<T> is greater than the available space from arrayIndex to the end of the destination array.");
        }

        Node<T>? current = Begin;
        while (current != null)
        {
            array[arrayIndex++] = current.Value;
            current = current.Next;
        }
    }

    public int IndexOf(T item)
    {
        if (Begin == null)
        {
            return -1;
        }

        Node<T>? current = Begin;
        var index = 0;
        while (current != null)
        {
            if (current.Value.Equals(item))
            {
                return index;
            }

            current = current.Next;
            index++;
        }

        return -1;
    }

    public void Insert(int index, T item)
    {
        if (index < 0 || index > Count)
        {
            throw new ArgumentOutOfRangeException(nameof(index));
        }

        if (IsReadOnly)
        {
            throw new NotSupportedException();
        }

        if (index == 0)
        {
            var node = new Node<T>(item);
            node.Next = Begin;
            Begin = node;
        }
        else
        {
            Node<T>? current = Begin;
            for (var i = 0; i < index - 1; i++)
            {
                current = current!.Next;
            }

            var node = new Node<T>(item);
            node.Next = current!.Next;
            current.Next = node;
        }
    }

    public bool Remove(T item)
    {
        if (IsReadOnly)
        {
            throw new NotSupportedException();
        }

        if (Begin == null)
        {
            return false;
        }

        if (Begin.Value.Equals(item))
        {
            Begin = Begin.Next;
            return true;
        }

        Node<T>? current = Begin;
        while (current!.Next != null)
        {
            if (current.Next.Value.Equals(item))
            {
                current.Next = current.Next.Next;
                return true;
            }

            current = current.Next;
        }

        return false;
    }

    public void RemoveAt(int index)
    {
        if (index < 0 || index >= Count)
        {
            throw new ArgumentOutOfRangeException(nameof(index));
        }

        if (IsReadOnly)
        {
            throw new NotSupportedException();
        }

        if (index == 0)
        {
            Begin = Begin!.Next;
        }
        else
        {
            Node<T>? current = Begin;
            for (var i = 0; i < index - 1; i++)
            {
                current = current!.Next;
            }

            current!.Next = current.Next!.Next;
        }
    }

    IEnumerator IEnumerable.GetEnumerator()
    {
        return GetEnumerator();
    }

    public override string ToString()
    {
        if (Begin == null)
        {
            return "[]";
        }

        var result = "[";
        Node<T>? current = Begin;
        while (current != null)
        {
            result += current.Value + ", ";
            current = current.Next;
        }

        result = result.Remove(result.Length - 2);
        result += "]";
        return result;
    }

    // Bubble sort:
    public MyList<T> GetSorted(bool reversed=false)
    {
        T[] array = new T[this.Count];
        this.CopyTo(array, 0);

        if (!reversed)
        {
            for (var i = 0; i < array.Length - 1; i++)
            {
                for (var j = 0; j < array.Length - i - 1; j++)
                {
                    if (Comparer<T>.Default.Compare(array[j], array[j + 1]) > 0)
                    {
                        var temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                }
            }
        }
        else
        {
            for (var i = 0; i < array.Length - 1; i++)
            {
                for (var j = 0; j < array.Length - i - 1; j++)
                {
                    if (Comparer<T>.Default.Compare(array[j], array[j + 1]) < 0)
                    {
                        var temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                    }
                }
            }
        }

        return new MyList<T>(array);
    }
}
