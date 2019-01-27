package main.java;

import java.util.concurrent.atomic.AtomicInteger;

//bean class for handling JSON objects
public class Dog {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private int id;
    private String breed;
    private int age;

    public Dog(String breed, int age) {
        this.breed = breed;
        this.age = age;
        this.id = COUNTER.getAndIncrement();
    }

    public Dog() {}

    public int getId() {
        return id;
    }

    public Dog setId(int id) {
        this.id = id;
        return this;
    }

    public String getBreed() {
        return breed;
    }

    public int getAge() {
        return age;
    }

    public void setBreed(final String breed) {
        this.breed = breed;
    }

    public void setAge(final int age) {
        this.age = age;
    }
}
