package com.surendra;
public class HelloJenkins {
    public String message() { return "Hello from the JAR module — built by Jenkins!"; }
    public static void main(String[] args) { System.out.println(new HelloJenkins().message()); }
}
