package com.foo;

import java.util.UUID;

/**
 * Created by sdutta on 9/23/16.
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(UUID.nameUUIDFromBytes("123456".getBytes()));
        System.out.println(UUID.nameUUIDFromBytes("123456".getBytes()));
    }
}
