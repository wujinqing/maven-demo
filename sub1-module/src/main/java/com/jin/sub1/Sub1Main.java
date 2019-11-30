package com.jin.sub1;

import com.alibaba.fastjson.JSON;

/**
 * @author wu.jinqing
 * @date 2019年11月21日
 */
public class Sub1Main {
    public static void main(String[] args) {
        Student student = new Student();

        student.setName("zhang san");
        student.setAge(10);

        System.out.println(JSON.toJSONString(student));
    }
}
