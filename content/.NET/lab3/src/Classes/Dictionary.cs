using System;
using System.Text;

using Interfaces;
using Structs;

namespace Classes;

public class Dictionary<TKey, TValue> : IDictionary<TKey, TValue>
{
    public Entry<TKey, TValue>? Begin { get; protected set; }
    public int Count { get; protected set; }

    public Dictionary()
    {
        this.Begin = null;
        this.Count = 0;
    }

    public Dictionary(TKey key, TValue value)
    {
        this.Begin = new Entry<TKey, TValue>(key, value);
        this.Count = 1;
    }

    public Dictionary(TKey[] keys, TValue[] values)
    {
        if (keys.Length > 0 && values.Length > 0)
        {
            this.Begin = new Entry<TKey, TValue>(keys[0], values[0]);
            this.Count = 1;

            for (int i = 1; i < keys.Length && i < values.Length; i++)
            {
                this.Add(keys[i], values[i]);
            }
        }
        else
        {
            this.Begin = null;
            this.Count = 0;
        }
    }

    public static bool operator ==(Dictionary<TKey, TValue>? left, Dictionary<TKey, TValue>? right)
    {
        if (left is null && right is null)
            return true;

        if (left is null || right is null)
            return false;

        return (object)left == (object)right;
    }

    public static bool operator !=(Dictionary<TKey, TValue>? left, Dictionary<TKey, TValue>? right)
    {
        return !(left == right);
    }

    public class DictionaryEnumerator : IEnumerator
    {
        private Entry<TKey, TValue>? Begin { get; set; }
        public object? Current { get; private set; }

        public DictionaryEnumerator(Entry<TKey, TValue>? Begin)
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
            else if (((Entry<TKey, TValue>)this.Current).HasNext())
            {
                this.Current = ((Entry<TKey, TValue>)this.Current).Next;
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
        return new DictionaryEnumerator(this.Begin);
    }

    public void Add(KeyValuePair<TKey, TValue> item)
    {
        this.Add(item.Key, item.Value);
    }

    public void Add(TKey key, TValue value)
    {
        if (this.Begin == null)
        {
            this.Begin = new Entry<TKey, TValue>(key, value);
        }
        else
        {
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (!((Entry<TKey, TValue>)enumerator.Current).HasNext())
                {
                    ((Entry<TKey, TValue>)enumerator.Current).Next = new Entry<TKey, TValue>(key, value);
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

    public bool Contains(TValue item)
    {
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Entry<TKey, TValue>)enumerator.Current).Value.Equals(item))
                return true;
        }

        return false;
    }

    public bool Contains(KeyValuePair<TKey, TValue> item)
    {
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Entry<TKey, TValue>)enumerator.Current).Key.Equals(item.Key) && ((Entry<TKey, TValue>)enumerator.Current).Value.Equals(item.Value))
                return true;
        }

        return false;
    }

    public void CopyTo(TValue[] array, Int32 arrayIndex)
    {
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            array[arrayIndex++] = ((Entry<TKey, TValue>)enumerator.Current).Value;
        }
    }

    public void CopyTo(KeyValuePair<TKey, TValue>[] array, Int32 arrayIndex)
    {
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            array[arrayIndex++] = new KeyValuePair<TKey, TValue>(((Entry<TKey, TValue>)enumerator.Current).Key, ((Entry<TKey, TValue>)enumerator.Current).Value);
        }
    }

    public bool Remove(TValue item)
    {
        if (this.Begin == null)
        {
            return false;
        }
        else if (this.Begin.Value.Equals(item))
        {
            this.Begin = this.Begin.Next;
            this.Count--;
            return true;
        }
        else
        {
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Entry<TKey, TValue>)enumerator.Current).Next.Value.Equals(item))
                {
                    ((Entry<TKey, TValue>)enumerator.Current).Next = ((Entry<TKey, TValue>)enumerator.Current).Next.Next;
                    this.Count--;
                    return true;
                }
            }
            return false;
        }
    }

    public TValue this[TKey key]
    {
        get
        {
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Entry<TKey, TValue>)enumerator.Current).Key.Equals(key))
                    return ((Entry<TKey, TValue>)enumerator.Current).Value;
            }

            throw new IndexOutOfRangeException();
        }
        set
        {
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Entry<TKey, TValue>)enumerator.Current).Key.Equals(key))
                {
                    ((Entry<TKey, TValue>)enumerator.Current).Value = value;
                    return;
                }
            }

            throw new IndexOutOfRangeException();
        }
    }

    public TKey[] Keys
    {
        get
        {
            TKey[] keys = new TKey[this.Count];
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            for (int i = 0; enumerator.MoveNext(); i++)
            {
                keys[i] = ((Entry<TKey, TValue>)enumerator.Current).Key;
            }

            return keys;
        }
    }

    public TValue[] Values
    {
        get
        {
            TValue[] values = new TValue[this.Count];
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            for (int i = 0; enumerator.MoveNext(); i++)
            {
                values[i] = ((Entry<TKey, TValue>)enumerator.Current).Value;
            }

            return values;
        }
    }

    public bool ContainsKey(TKey key)
    {
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Entry<TKey, TValue>)enumerator.Current).Key.Equals(key))
                return true;
        }

        return false;
    }

    public bool Remove(TKey key)
    {
        if (this.Begin == null)
        {
            return false;
        }
        else if (this.Begin.Key.Equals(key))
        {
            this.Begin = this.Begin.Next;
            this.Count--;
            return true;
        }
        else
        {
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Entry<TKey, TValue>)enumerator.Current).Next.Key.Equals(key))
                {
                    ((Entry<TKey, TValue>)enumerator.Current).Next = ((Entry<TKey, TValue>)enumerator.Current).Next.Next;
                    this.Count--;
                    return true;
                }
            }
            return false;
        }
    }

    public bool Remove(KeyValuePair<TKey, TValue> item)
    {
        if (this.Begin == null)
        {
            return false;
        }
        else if (this.Begin.Key.Equals(item.Key) && this.Begin.Value.Equals(item.Value))
        {
            this.Begin = this.Begin.Next;
            this.Count--;
            return true;
        }
        else
        {
            DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
            while (enumerator.MoveNext())
            {
                if (((Entry<TKey, TValue>)enumerator.Current).Next.Key.Equals(item.Key) && ((Entry<TKey, TValue>)enumerator.Current).Next.Value.Equals(item.Value))
                {
                    ((Entry<TKey, TValue>)enumerator.Current).Next = ((Entry<TKey, TValue>)enumerator.Current).Next.Next;
                    this.Count--;
                    return true;
                }
            }
            return false;
        }
    }

    public bool TryGetValue(TKey key, out TValue value)
    {
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            if (((Entry<TKey, TValue>)enumerator.Current).Key.Equals(key))
            {
                value = ((Entry<TKey, TValue>)enumerator.Current).Value;
                return true;
            }
        }

        value = default(TValue);
        return false;
    }

    public override bool Equals(object? obj)
    {
        if (obj == null || !(obj is Dictionary<TKey, TValue>))
            return false;

        Dictionary<TKey, TValue> other = (Dictionary<TKey, TValue>)obj;
        if (this.Count != other.Count)
            return false;

        DictionaryEnumerator enumerator1 = (DictionaryEnumerator)this.GetEnumerator();
        DictionaryEnumerator enumerator2 = (DictionaryEnumerator)other.GetEnumerator();
        while (enumerator1.MoveNext() && enumerator2.MoveNext())
        {
            if (!((Entry<TKey, TValue>)enumerator1.Current).Key.Equals(((Entry<TKey, TValue>)enumerator2.Current).Key) || !((Entry<TKey, TValue>)enumerator1.Current).Value.Equals(((Entry<TKey, TValue>)enumerator2.Current).Value))
                return false;
        }
        return true;
    }

    public override int GetHashCode()
    {
        int hash = 19;
        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            hash = hash * 31 + (((Entry<TKey, TValue>)enumerator.Current).Key.GetHashCode() ^ ((Entry<TKey, TValue>)enumerator.Current).Value.GetHashCode());
        }

        return hash;
    }

    public override string ToString()
    {
        StringBuilder result = new StringBuilder();
        result.Append("{");

        DictionaryEnumerator enumerator = (DictionaryEnumerator)this.GetEnumerator();
        while (enumerator.MoveNext())
        {
            result.Append(((Entry<TKey, TValue>)enumerator.Current).ToString());
            if (((Entry<TKey, TValue>)enumerator.Current).HasNext())
            {
                result.Append(", ");
            }
            else
            {
                result.Append("}");
            }
        }
        return result.ToString();
    }
}