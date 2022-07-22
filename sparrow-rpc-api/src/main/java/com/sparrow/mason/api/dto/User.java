package com.sparrow.mason.api.dto;

/**
 * @author chengwei_shen
 * @date 2022/7/20 10:56
 **/
public class User {
    String name;
    Integer age;
    Byte gender;

    public User(String name, Integer age, Byte gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }
}
