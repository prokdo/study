using System;

namespace Interfaces;

public interface IList<T> : ICollection<T>
{
    public T this[Int32 index] { get; set; }
    public Int32 IndexOf(T item);
    public void Insert(Int32 index, T item);
    public void RemoveAt(Int32 index);
}