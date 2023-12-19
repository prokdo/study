package util;

public class Pair<T1, T2> {
    public final T1 first;
    public final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        else if (object == null)
            return false;

        if (!(object instanceof Pair)) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) object;

        return this.first.equals(pair.first) && this.second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return this.first.hashCode() ^ this.second.hashCode();
    }

    @Override
    public String toString() {
        return "<" + this.first + ", " + this.second + ">";
    }
}
