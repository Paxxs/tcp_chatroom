package com.daiinfo.util;

public class TriangleUtil {
    private static int tri = 0;

    public void Triangle(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) {
            tri = 0;
        } else {
            if (((a + b) > c) && ((a + c) > b && (b + c) > a)) {
                if ((a == b) && (a == c)) {
                    tri = 3;// 等边三角形
                }
                if ((a == b && a != c) || (a == c && a != b) || (b == c && a != b)) {
                    tri = 2;// 等腰三角形
                }
                if (a != b && a != c && b != c) {
                    tri = 1;// 不等边三角形
                }
            } else {
                tri = 0;// 无法构成三角形
            }
        }
    }

    public int getReuslt() {
        return tri;
    }

    public void clear() {
        tri = 0;
    }

}
