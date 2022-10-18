package cn.morfans.rg;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStudent {


    private Student initStudent() {
        Student t = new Student(3);
        t.addStudent(1, "pax");
        t.addStudent(2, "paxx");
        t.addStudent(3, "paxxs");
        return t;
    }

    @Test
    public void testgetId() {
        Student t = initStudent();
        int expected0 = 1;
        int expected1 = 2;
        int expected2 = 3;
        assertEquals(expected0, t.getId(0));
        assertEquals(expected1, t.getId(1));
        assertEquals(expected2, t.getId(2));
    }

    @Test
    public void testSearchStudent() {
        Student t = initStudent();
        int expect0 = 0; // if search 1
        int expect1 = 1; // if search 2
        int expect2 = 2; // if search 3
        assertEquals(expect0, t.searchStudent(1));
        assertEquals(expect1, t.searchStudent(2));
        assertEquals(expect2, t.searchStudent(3));
    }

    @Test
    public void testDeleteStudent() {
        // 删除末尾
        Student t = initStudent();
        t.deleteStudent(3);
        int expectNum = 2;
        String expectFirstString = "1;pax";
        String expectString = "2;paxx";
        String expectLastString = "";
        assertEquals(expectNum, t.currentCount);
        assertEquals(expectFirstString, t.studentList[0]);
        assertEquals(expectString, t.studentList[1]);
        assertEquals(expectLastString, t.studentList[2]);

        // 删除中间
        t = initStudent();
        t.deleteStudent(2);
        expectFirstString = "1;pax";
        expectString = "3;paxxs";
        expectLastString = "";
        assertEquals(expectNum, t.currentCount);
        assertEquals(expectFirstString, t.studentList[0]);
        assertEquals(expectString, t.studentList[1]);
        assertEquals(expectLastString, t.studentList[2]);

        // 不能删除
        expectNum = 3;
        t = initStudent();
        t.deleteStudent(4);
        assertEquals(expectNum, t.currentCount);

    }

}
