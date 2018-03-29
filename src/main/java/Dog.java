package main.java;

import java.util.concurrent.atomic.AtomicInteger;

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

    public Dog() {
        this.id = COUNTER.getAndIncrement();
    }

    public int getId() {
        return id;
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
