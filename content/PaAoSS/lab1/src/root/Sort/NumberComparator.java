package root.Sort;

import java.math.BigDecimal;

final class NumberComparator {
    private NumberComparator() {}

    static final int compare(Number a, Number b){
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }

}