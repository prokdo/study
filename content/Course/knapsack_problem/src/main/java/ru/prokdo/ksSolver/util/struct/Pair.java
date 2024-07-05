package ru.prokdo.ksSolver.util.struct;

public class Pair<T1, T2> {
    public final T1 first;
    public final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        else if (object == null) return false;

        if (!(object instanceof Pair)) return false;

        Pair<?, ?> other = (Pair<?, ?>) object;

        return this.first.equals(other.first) && this.second.equals(other.second);
    }

    @Override
    public int hashCode() {
        return first.hashCode() ^ second.hashCode();
    }

    @Override
    public String toString() {
        return "<" + first + ", " + second + ">";
    }
}
