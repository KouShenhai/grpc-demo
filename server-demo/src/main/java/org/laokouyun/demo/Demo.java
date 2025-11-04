package org.laokouyun.demo;

import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;

public class Demo {
    public static void main(String[] args) {
        Person person = new Person("Tom");

        // 1️⃣ 获取方法
        Method method = ReflectionUtils.findMethod(Person.class, "sayHello", String.class);


        // 2️⃣ 调用方法
        assert method != null;
        ReflectionUtils.makeAccessible(method);
        Object result = ReflectionUtils.invokeMethod(method, person, "World");

        System.out.println(result); // 输出：Hello World, I'm Tom
    }
}

class Person {
    private final String name;

    Person(String name) {
        this.name = name;
    }

    public String sayHello(String target) {
        return "Hello " + target + ", I'm " + name;
    }
}
