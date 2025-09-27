package com.becker.freelance.junit.commons.beans;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

class Bean {

    private String name;
    private Integer intValue;
    private Long longValue;
    private Float floatValue;
    private Double doubleValue;
    private BigInteger bigIntegerValue;
    private BigDecimal bigDecimalValue;
    private LocalDate localDate;
    private LocalTime localTime;
    private LocalDateTime localDateTime;
    private ValuesEnum valuesEnum;
    private EnumWithOneValue enumWithOneValue;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Integer getIntValue() {

        return intValue;
    }

    public void setIntValue(Integer intValue) {

        this.intValue = intValue;
    }

    public Long getLongValue() {

        return longValue;
    }

    public void setLongValue(Long longValue) {

        this.longValue = longValue;
    }

    public Float getFloatValue() {

        return floatValue;
    }

    public void setFloatValue(Float floatValue) {

        this.floatValue = floatValue;
    }

    public Double getDoubleValue() {

        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {

        this.doubleValue = doubleValue;
    }

    public BigInteger getBigIntegerValue() {

        return bigIntegerValue;
    }

    public void setBigIntegerValue(BigInteger bigIntegerValue) {

        this.bigIntegerValue = bigIntegerValue;
    }

    public BigDecimal getBigDecimalValue() {

        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {

        this.bigDecimalValue = bigDecimalValue;
    }

    public LocalDate getLocalDate() {

        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {

        this.localDate = localDate;
    }

    public LocalTime getLocalTime() {

        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {

        this.localTime = localTime;
    }

    public LocalDateTime getLocalDateTime() {

        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {

        this.localDateTime = localDateTime;
    }

    public ValuesEnum getValuesEnum() {

        return valuesEnum;
    }

    public void setValuesEnum(ValuesEnum valuesEnum) {

        this.valuesEnum = valuesEnum;
    }

    public EnumWithOneValue getEnumWithOneValue() {

        return enumWithOneValue;
    }

    public void setEnumWithOneValue(EnumWithOneValue enumWithOneValue) {

        this.enumWithOneValue = enumWithOneValue;
    }
}
