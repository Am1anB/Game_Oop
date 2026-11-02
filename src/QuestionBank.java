import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** คลังโจทย์ของแต่ละด่าน */
public final class QuestionBank {
    private static final String OUTPUT = "ผลลัพธ์ของโค้ดนี้คืออะไร";

    private QuestionBank() {
    }

    private static Question fill(String title, String code, String... answers) {
        return new FillBlankQuestion(title, code, answers);
    }

    private static Question choice(String title, String code, int correctIndex, String... choices) {
        return new MultipleChoiceQuestion(title, code, choices, correctIndex);
    }

    private static List<Question> shuffled(Question... questions) {
        List<Question> list = new ArrayList<>();
        Collections.addAll(list, questions);
        Collections.shuffle(list);
        return list;
    }

    /** ด่าน 1: Java พื้นฐาน */
    public static List<Question> basics() {
        return shuffled(
            fill("เติมชื่อ constructor ให้ถูกต้อง",
                "public class Main {\n"
                + "    public ___() {\n"
                + "        System.out.println(\"Constructor called\");\n"
                + "    }\n"
                + "}", "Main"),
            fill("เติมชนิดข้อมูลของจำนวนเต็มขนาด 32 บิต",
                "___ score = 100;", "int"),
            fill("เติมคำสั่งสำหรับสร้าง object",
                "Dog d = ___ Dog();", "new"),
            fill("เติมคำสั่งที่ทำให้หลุดออกจาก loop ทันที",
                "while (true) {\n"
                + "    ___;\n"
                + "}", "break"),
            fill("เติมชนิดข้อมูลของ method ที่ไม่คืนค่า",
                "public ___ hello() {\n"
                + "    System.out.println(\"Hello\");\n"
                + "}", "void"),
            fill("เติมคำที่ใช้อ้างถึง object ปัจจุบัน",
                "class Cat {\n"
                + "    String name;\n"
                + "    Cat(String name) {\n"
                + "        ___.name = name;\n"
                + "    }\n"
                + "}", "this"),
            choice(OUTPUT,
                "int x = 7 / 2;\n"
                + "System.out.println(x);",
                1, "3.5", "3", "4", "Compile Error"),
            choice(OUTPUT,
                "String s = \"Java\";\n"
                + "System.out.println(s.length());",
                1, "3", "4", "5", "Compile Error"),
            choice(OUTPUT,
                "for (int i = 0; i < 3; i++) {\n"
                + "    System.out.print(i);\n"
                + "}",
                0, "012", "123", "0123", "333"),
            choice(OUTPUT,
                "boolean b = 5 > 3 && 2 > 4;\n"
                + "System.out.println(b);",
                1, "true", "false", "0", "Compile Error"),
            choice(OUTPUT,
                "int[] a = {2, 4, 6};\n"
                + "System.out.println(a[1]);",
                1, "2", "4", "6", "Runtime Error"),
            choice("keyword ใดทำให้เรียก method ได้โดยไม่ต้องสร้าง object", "",
                2, "final", "void", "static", "this")
        );
    }

    /** ด่าน 2: Inheritance, Encapsulation, Abstract, Interface */
    public static List<Question> inheritance() {
        return shuffled(
            fill("เติม keyword ให้ Dog สืบทอดจาก Animal",
                "class Dog ___ Animal {\n"
                + "}", "extends"),
            fill("เติม keyword ให้ Bird ทำตาม interface Flyable",
                "class Bird ___ Flyable {\n"
                + "    public void fly() { }\n"
                + "}", "implements"),
            fill("เติม keyword ให้ Shape เป็น class ที่สร้าง object ไม่ได้",
                "public ___ class Shape {\n"
                + "    abstract double area();\n"
                + "}", "abstract"),
            fill("เติม keyword เพื่อเรียก constructor ของ class แม่",
                "class Dog extends Animal {\n"
                + "    Dog() {\n"
                + "        ___(\"Dog\");\n"
                + "    }\n"
                + "}", "super"),
            fill("เติม keyword สำหรับประกาศ interface",
                "public ___ Drawable {\n"
                + "    void draw();\n"
                + "}", "interface"),
            fill("เติม keyword ที่ทำให้ตัวแปรเปลี่ยนค่าไม่ได้",
                "___ int MAX = 10;", "final", "static final"),
            choice("access modifier ใดเข้าถึงได้เฉพาะภายใน class เดียวกัน", "",
                3, "public", "protected", "default", "private"),
            choice("ใน Java หนึ่ง class สามารถ extends ได้กี่ class", "",
                0, "1 class", "2 class", "ไม่จำกัด", "extends ไม่ได้เลย"),
            choice("ข้อใดถูกต้องเกี่ยวกับ abstract class", "",
                1, "ต้องมี abstract method เสมอ", "สร้างด้วย new โดยตรงไม่ได้",
                "class อื่น extends ไม่ได้", "มีตัวแปรไม่ได้"),
            choice("ตัวแปรใน interface เป็นแบบใดโดยอัตโนมัติ", "",
                2, "private", "protected", "public static final", "abstract"),
            choice("ถ้าสั่ง new B(); ผลลัพธ์จะเป็นอะไร",
                "class A {\n"
                + "    A() { System.out.print(\"A\"); }\n"
                + "}\n"
                + "class B extends A {\n"
                + "    B() { System.out.print(\"B\"); }\n"
                + "}",
                2, "A", "B", "AB", "BA"),
            choice("Encapsulation หมายถึงข้อใด", "",
                0, "ซ่อนข้อมูล ให้เข้าถึงผ่าน method", "สืบทอดคุณสมบัติจาก class แม่",
                "method เดียวทำงานได้หลายแบบ", "การรันหลายงานพร้อมกัน")
        );
    }

    /** ด่าน 3: Polymorphism, Exception, Thread, Collection, GUI */
    public static List<Question> advanced() {
        return shuffled(
            fill("เติม annotation ที่บอกว่า method นี้เขียนทับของ class แม่",
                "class Dog extends Animal {\n"
                + "    @___\n"
                + "    void sound() { }\n"
                + "}", "Override"),
            fill("เติมชื่อ method ที่สั่งให้ Thread เริ่มทำงาน",
                "Thread t = new Thread(task);\n"
                + "t.___();", "start"),
            fill("เติมชื่อ interface ที่ใช้สร้างงานให้ Thread",
                "class Task implements ___ {\n"
                + "    public void run() { }\n"
                + "}", "Runnable"),
            fill("เติม keyword สำหรับครอบโค้ดที่อาจเกิด Exception",
                "___ {\n"
                + "    int x = 10 / 0;\n"
                + "} catch (ArithmeticException e) {\n"
                + "    System.out.println(\"Error\");\n"
                + "}", "try"),
            fill("เติมชื่อ class ของ Swing ที่ใช้สร้างหน้าต่างโปรแกรม",
                "class GameWindow extends ___ {\n"
                + "}", "JFrame"),
            fill("เติมชื่อ class ที่เป็น list ขยายขนาดได้",
                "___<String> names = new ArrayList<>();", "ArrayList", "List"),
            choice("ผลลัพธ์ของโค้ดนี้คืออะไร (Polymorphism)",
                "class Animal { void sound() { System.out.print(\"...\"); } }\n"
                + "class Cat extends Animal {\n"
                + "    void sound() { System.out.print(\"Meow\"); }\n"
                + "}\n"
                + "Animal a = new Cat();\n"
                + "a.sound();",
                1, "...", "Meow", "...Meow", "Compile Error"),
            choice(OUTPUT,
                "try {\n"
                + "    System.out.print(\"A\");\n"
                + "    int x = 1 / 0;\n"
                + "    System.out.print(\"B\");\n"
                + "} catch (Exception e) { System.out.print(\"C\");\n"
                + "} finally { System.out.print(\"D\"); }",
                2, "AB", "ABD", "ACD", "ABCD"),
            choice(OUTPUT,
                "Object o = \"Hi\";\n"
                + "System.out.println(o instanceof String);",
                0, "true", "false", "Hi", "Compile Error"),
            choice("Method Overloading หมายถึงข้อใด", "",
                3, "เขียนทับ method ของ class แม่", "ซ่อน method ไม่ให้ใครเห็น",
                "สร้าง object หลายตัว", "ชื่อ method เหมือนกัน แต่ parameter ต่างกัน"),
            choice("synchronized ใช้ทำอะไร", "",
                1, "ทำให้โปรแกรมทำงานเร็วขึ้น", "กันหลาย thread ใช้ข้อมูลพร้อมกัน",
                "ทำให้ตัวแปรเปลี่ยนค่าไม่ได้", "สร้าง thread ใหม่"),
            choice("ต้อง override method ใดของ JPanel เพื่อวาดกราฟิกเอง", "",
                0, "paintComponent", "actionPerformed", "run", "setVisible")
        );
    }
}
