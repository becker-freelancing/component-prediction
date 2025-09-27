package com.becker.freelance.junit.commons.beans;

import java.util.Objects;

class BeanWithEquals extends Bean {

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bean that = (Bean) o;
        return Objects.equals(getName(), that.getName())
                && Objects.equals(getIntValue(), that.getIntValue())
                && Objects.equals(getLongValue(), that.getLongValue())
                && Objects.equals(getFloatValue(), that.getFloatValue())
                && Objects.equals(getDoubleValue(), that.getDoubleValue())
                && Objects.equals(getBigIntegerValue(), that.getBigIntegerValue())
                && Objects.equals(getBigDecimalValue(), that.getBigDecimalValue())
                && Objects.equals(getLocalDate(), that.getLocalDate())
                && Objects.equals(getLocalTime(), that.getLocalTime())
                && Objects.equals(getLocalDateTime(), that.getLocalDateTime())
                && getValuesEnum() == that.getValuesEnum()
                && getEnumWithOneValue() == that.getEnumWithOneValue();
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                getName(),
                getIntValue(),
                getLongValue(),
                getFloatValue(),
                getDoubleValue(),
                getBigIntegerValue(),
                getBigDecimalValue(),
                getLocalDate(),
                getLocalTime(),
                getLocalDateTime(),
                getValuesEnum(),
                getEnumWithOneValue());
    }
}
