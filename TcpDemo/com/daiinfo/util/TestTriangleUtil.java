package com.daiinfo.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTriangleUtil {
    private static TriangleUtil triangleUtil = new TriangleUtil();

    @Test
    public void testTriangle() {

        // 三边长度无法构成三角形
        triangleUtil.Triangle(-5, 5, 5);
        assertEquals(0, triangleUtil.getReuslt());

        //equilateral 等边三角形
        triangleUtil.Triangle(5, 5, 5);
        assertEquals(3, triangleUtil.getReuslt());

        //isosceles 等腰三角形
        triangleUtil.Triangle(5, 5, 6);
        assertEquals(2, triangleUtil.getReuslt());

        //scalene 不等边三角形
        triangleUtil.Triangle(3, 4, 5);
        assertEquals(1, triangleUtil.getReuslt());

        // The triateral length cannot form a triangle 三边长度无法构成三角形
        triangleUtil.Triangle(12, 5, 5);
        assertEquals(0, triangleUtil.getReuslt());

        //The triateral length cannot form a triangle  三边长度无法构成三角形
        triangleUtil.Triangle(10, 5, 5);
        assertEquals(0, triangleUtil.getReuslt());

        //本意是设计等腰三角形，但三边长度无法构成三角形 等边三角形，所以失败failure
        triangleUtil.Triangle(11, 5, 5);
        assertEquals(3, triangleUtil.getReuslt());

    }

}
