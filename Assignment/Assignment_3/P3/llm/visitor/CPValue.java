package visitor;

/**
 * Constant Propagation Lattice Value.
 *
 * Three-level lattice (per variable):
 *
 * NAC (Not A Constant — value is unknown / varies)
 * / | \
 * CONST(v) (known constant: an integer or boolean)
 * \ | /
 * UNDEF (bottom — variable not yet assigned on any path)
 *
 * Meet rule:
 * UNDEF meet x = x
 * x meet UNDEF = x
 * CONST(v) meet CONST(v) = CONST(v)
 * CONST(v) meet CONST(w) = NAC (v != w)
 * NAC meet x = NAC
 * x meet NAC = NAC
 */
public class CPValue {

    // ---------------------------------------------------------------
    // Kind enum
    // ---------------------------------------------------------------
    public enum Kind {
        UNDEF, CONST, NAC
    }

    // ---------------------------------------------------------------
    // Singleton constants
    // ---------------------------------------------------------------
    public static final CPValue UNDEF = new CPValue(Kind.UNDEF, null);
    public static final CPValue NAC = new CPValue(Kind.NAC, null);
    public static final CPValue TRUE_VAL = new CPValue(Kind.CONST, Boolean.TRUE);
    public static final CPValue FALSE_VAL = new CPValue(Kind.CONST, Boolean.FALSE);

    // ---------------------------------------------------------------
    // Fields
    // ---------------------------------------------------------------
    public final Kind kind;
    /** Non-null iff kind == CONST. Either an Integer or a Boolean. */
    public final Object value;

    // ---------------------------------------------------------------
    // Constructors / factories
    // ---------------------------------------------------------------
    private CPValue(Kind kind, Object value) {
        this.kind = kind;
        this.value = value;
    }

    /** Factory for integer constants. */
    public static CPValue ofInt(int v) {
        return new CPValue(Kind.CONST, v);
    }

    /** Factory for boolean constants. */
    public static CPValue ofBool(boolean v) {
        return v ? TRUE_VAL : FALSE_VAL;
    }

    // ---------------------------------------------------------------
    // Lattice meet operation
    // ---------------------------------------------------------------
    public static CPValue meet(CPValue a, CPValue b) {
        // UNDEF is bottom — it contributes nothing
        if (a.kind == Kind.UNDEF)
            return b;
        if (b.kind == Kind.UNDEF)
            return a;
        // NAC dominates everything
        if (a.kind == Kind.NAC || b.kind == Kind.NAC)
            return NAC;
        // Both CONST
        assert a.kind == Kind.CONST && b.kind == Kind.CONST;
        if (a.value.equals(b.value))
            return a;
        return NAC;
    }

    // ---------------------------------------------------------------
    // Convenience predicates
    // ---------------------------------------------------------------
    public boolean isConst() {
        return kind == Kind.CONST;
    }

    public boolean isNAC() {
        return kind == Kind.NAC;
    }

    public boolean isUndef() {
        return kind == Kind.UNDEF;
    }

    public boolean isTrue() {
        return isConst() && Boolean.TRUE.equals(value);
    }

    public boolean isFalse() {
        return isConst() && Boolean.FALSE.equals(value);
    }

    public boolean isBool() {
        return isConst() && value instanceof Boolean;
    }

    public boolean isInt() {
        return isConst() && value instanceof Integer;
    }

    public int intValue() {
        return (Integer) value;
    }

    public boolean boolValue() {
        return (Boolean) value;
    }

    // ---------------------------------------------------------------
    // Emit as TACoJava2 token string
    // ---------------------------------------------------------------
    /**
     * Returns the literal string to emit in the output program,
     * e.g. "42", "-3", "true", "false".
     * Only call this when isConst() == true.
     */
    public String toTokenString() {
        if (value instanceof Boolean)
            return (Boolean) value ? "true" : "false";
        int v = (Integer) value;
        // Negative integers need the minus sign
        return Integer.toString(v);
    }

    // ---------------------------------------------------------------
    // Object overrides
    // ---------------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CPValue))
            return false;
        CPValue other = (CPValue) o;
        if (kind != other.kind)
            return false;
        if (value == null)
            return other.value == null;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return kind.hashCode() * 31 + (value == null ? 0 : value.hashCode());
    }

    @Override
    public String toString() {
        switch (kind) {
            case UNDEF:
                return "UNDEF";
            case NAC:
                return "NAC";
            default:
                return "CONST(" + value + ")";
        }
    }
}