using Structs;

namespace Interfaces;

public interface IDictionary<TKey, TValue> : ICollection<KeyValuePair<TKey, TValue>>
{
    public TValue this[TKey key] { get; set; }
    public TKey[] Keys { get; }
    public TValue[] Values { get; }
    public void Add(TKey key, TValue value);
    public bool ContainsKey(TKey key);
    public bool Remove(TKey key);
    public bool TryGetValue(TKey key, out TValue value);
}