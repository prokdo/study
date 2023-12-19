using System;

namespace Interfaces;

public interface ICollection<T> : IEnumerable
{
    public int Count { get; }
    public void Add(T item);
    public void Clear();
    public bool Contains(T item);
    public void CopyTo(T[] array, Int32 arrayIndex);
    public bool Remove(T item);
}