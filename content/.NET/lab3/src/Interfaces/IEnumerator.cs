namespace Interfaces;

public interface IEnumerator : IDisposable
{
    public object? Current { get; }
    public bool MoveNext();
    public void Reset();
}