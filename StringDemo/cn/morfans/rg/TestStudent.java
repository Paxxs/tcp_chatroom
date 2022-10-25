package cn.morfans.rg;

import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

public class TestStudent {

    private Student initStudent() {
        Student t = new Student(3);
        t.addStudent(1, "pax");
        t.addStudent(2, "paxx");
        t.addStudent(3, "paxxs");
        return t;
    }

    @Test
    public void testChangeName(){
        Student t = initStudent();
        String expected = "2;java";
        t.changeName(2,"java");
        assertEquals(expected,t.studentList[1]);
    }

    @Test
    public void testSearchStudentById(){
        Student t = initStudent();
        Boolean expected = true;

        assertEquals(expected,t.searchStudentById(2)); // 2;paxx
    }

    @Test
    public void testgetId() throws Exception {
        Student t = initStudent();
        int expected0 = 1;
        int expected1 = 2;
        int expected2 = 3;

        // reflect
        // 获取class object
        Class<Student> stuClass = Student.class;
        // get method
        Method declareMethod = stuClass.getDeclaredMethod("getId", int.class);
        declareMethod.setAccessible(true);
        Object result0 = declareMethod.invoke(t, 0);
        Object result1 = declareMethod.invoke(t, 1);
        Object result2 = declareMethod.invoke(t, 2);
        declareMethod.setAccessible(false);

        assertEquals(expected0, result0);
        assertEquals(expected1, result1);
        assertEquals(expected2, result2);
    }

    @Test
    public void testSearchStudent() throws Exception {
        //获取目标类的实例
        Student t = initStudent();
        int expect0 = 0; // if search 1
        int expect1 = 1; // if search 2
        int expect2 = 2; // if search 3

        //获取目标类的class对象
        Class<Student> clazz = Student.class;
        // 获取方法
        Method declareMethod = clazz.getDeclaredMethod("searchStudent", int.class);
        declareMethod.setAccessible(true);
        Object result = declareMethod.invoke(t, 1);
        Object result2 = declareMethod.invoke(t, 2);
        Object result3 = declareMethod.invoke(t, 3);
        declareMethod.setAccessible(false);

        assertEquals(expect0, result);
        assertEquals(expect1, result2);
        assertEquals(expect2, result3);
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
