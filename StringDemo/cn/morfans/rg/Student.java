package cn.morfans.rg;

public class Student {
    public String[] studentList;
    /**
     * 当前数组真实大小
     */
    public int currentCount;

    public Student(int length) {
        studentList = new String[length];
        currentCount = 0;
    }

    public boolean addStudent(int id, String name) {
        // TODO：判断数组是否够用
        studentList[currentCount] = String.format("%d;%s", id, name);
        currentCount++;
        return true;
    }

    
    public boolean changeName(int id, String name) {
        int index = searchStudent(id);
        if (index == -1)
            return false;
        studentList[index] = String.format("%d;%s", id, name);
        return true;
    }

    public boolean deleteStudent(int id) {
        // 经过google, java并没有提供删除数组中指定元素的功能
        int index = searchStudent(id);
        if (index == -1)
            return false;
        for (int i = index; i < currentCount; i++) {
            if (i == (currentCount - 1)) { // 如果是最后一个元素了
                studentList[i] = "";
                continue;
            }
            studentList[i] = studentList[i + 1]; // 不是最后一个元素则用后边的覆盖
        }
        currentCount--;
        return true;
    }

    /**
     * 搜索指定id,返回所在 index
     *
     * @param id 学号
     * @return 数组所在的 index
     */
    public int searchStudent(int id) {
        for (int i = 0; i < currentCount; i++)
            if (id == getId(i))
                return i;
        return -1;
    }

    /**
     * 获取字符串数组指定index中的id值
     *
     * @param index 数组index从0开始
     * @return id的值
     */
    public int getId(int index) {
        int position = studentList[index].indexOf(';');
        String id = studentList[index].substring(0, position);
        return Integer.parseInt(id);
    }

    public void printStudent() {
        for (int i = 0; i < currentCount; i++) {
            System.out.println(studentList[i]);
        }
    }
}
