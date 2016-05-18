package com.gy.myframework.IOC.Core.entity;

import java.lang.annotation.Annotation;

/**
 * Created by gy on 2016/1/22.
 */
public class ClassMemberPackage {

    private String name;
    private String modify;
    private Class<?> refType;
    private BasicValueType basicType;
    private Annotation[] annotations;
    private BasicValuePackage basicValue;
    private Object value;

    public Annotation[] getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public BasicValuePackage getBasicValue() {
        return basicValue;
    }

    public void setBasicValue(BasicValuePackage basicValue) {
        this.basicValue = basicValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public Class<?> getRefType() {
        return refType;
    }

    public void setRefType(Class<?> refType) {
        this.refType = refType;
    }

    public BasicValueType getBasicType() {
        return basicType;
    }

    public void setBasicType(BasicValueType basicType) {
        this.basicType = basicType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
