using System;

using EventArgs;

namespace Interfaces;

public interface ICollection<T> : IEnumerable
{
    public delegate void AddEventHandler(ICollection<T> collection, AddEventArgs<T> args);
    public event AddEventHandler? AddEvent;

    public delegate void RemoveEventHandler(ICollection<T> collection, RemoveEventArgs<T> args);
    public event RemoveEventHandler? RemoveEvent;

    public delegate void CopyToEventHandler(ICollection<T> collection, CopyToEventArgs<T> args);
    public event CopyToEventHandler? CopyToEvent;

    public delegate void ClearEventHandler(ICollection<T> collection);
    public event ClearEventHandler? ClearEvent;
    
    public int Count { get; }
    public void Add(T item);
    public void Clear();
    public bool Contains(T item);
    public void CopyTo(T[] array, Int32 arrayIndex);
    public bool Remove(T item);
}