package com.becker.freelance.junit.commons.equals;

import java.util.Objects;

public class EqualsTestTestObject {

    private String str;
    private int i;
    private double d;

    public EqualsTestTestObject(String str, int i, double d) {

        this.str = str;
        this.i = i;
        this.d = d;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        EqualsTestTestObject that = (EqualsTestTestObject) object;
        return i == that.i && Double.compare(d, that.d) == 0 && Objects.equals(str, that.str);
    }

    @Override
    public int hashCode() {

        return Objects.hash(str, i, d);
    }
}
