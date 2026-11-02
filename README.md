# แก้โจทย์ต่อสู้ (Problem Battle)

เกมต่อสู้ที่ต้องตอบโจทย์ Java ให้ถูกและทันเวลา ตัวละครของเราถึงจะโจมตีศัตรูได้
เขียนด้วย Java Swing ล้วน ไม่ต้องลงไลบรารีเพิ่ม

## วิธีรัน

ดับเบิลคลิก `run.bat` หรือสั่งเองจากโฟลเดอร์โปรเจกต์ดังนี้ (ใช้ JDK 8 ขึ้นไป)

```
javac -encoding UTF-8 -d bin src\*.java
java -cp bin Main
```

ต้องรันจากโฟลเดอร์นี้เท่านั้น เพราะเกมอ่านรูปจาก `assets/`

## รูปตัวละคร (Animation)

ตัวละครแต่ละตัวมีรูปหลายท่าใน `assets/ชื่อตัวละคร/` ตั้งชื่อเป็น `ชื่อท่า_เลขเฟรม.png`
คลาส `Animation` จะโหลดรูปของท่านั้นมาวนแสดงทีละรูป ทำให้ดูเหมือนขยับได้

| ท่า | จำนวนรูป | ใช้ตอน |
|---|---|---|
| `idle` | 4 | ยืนเฉย (วนซ้ำ) |
| `run` | 4 | วิ่งเข้าหาศัตรูและวิ่งกลับ (วนซ้ำ) |
| `attack` | 4 | ฟัน (เล่นครั้งเดียว) |
| `cast` | 3 | ร่ายท่าไม้ตาย (วนซ้ำ) |
| `hurt` | 2 | โดนโจมตี (เล่นครั้งเดียว) |
| `dead` | 4 | ล้มลง (ค้างที่รูปสุดท้าย) |

อยากใช้รูปของตัวเอง ให้เอาไฟล์ไปทับชื่อเดิมได้เลย โดยไม่ต้องแก้โค้ด
จะเพิ่มจำนวนรูปก็ได้ แค่ตั้งเลขเฟรมต่อกันไป ภาพต้องหันไปทางขวา พื้นหลังโปร่งใส
และเว้นที่ให้ตัวละครอยู่กลางรูป เท้าอยู่ที่ระดับ 8/9 ของความสูงรูป (เกมวาดลงกรอบ 240x216)

รูปชุดปัจจุบันสร้างด้วย `tools/SpriteGenerator.java` (ไม่ได้เป็นส่วนหนึ่งของตัวเกม)

## กติกา

- ตอบถูก เราตี / ตอบผิดหรือหมดเวลา ศัตรูตี (ดาเมจ 1)
- ถูก 3 ข้อติด เราใช้ท่าไม้ตาย / ผิด 3 ข้อติด ศัตรูใช้ท่าไม้ตาย (ดาเมจ 1.5)
- เลือดฝั่งละ 5 หน่วย ฝั่งไหนหมดก่อนแพ้
- โจทย์มี 2 แบบ คือเติมคำลงช่องว่าง (กด Enter ได้) และช้อยส์

## ด่าน

| ด่าน | เนื้อหาโจทย์ | ศัตรู | เวลาต่อข้อ | อุปสรรค |
|---|---|---|---|---|
| 1 ทุ่งหญ้าสายฝน | Java พื้นฐาน | อัศวินเงิน | 30 วิ | ฝนตกหนัก: ตอบผิดติดกัน เวลาข้อถัดไปลดข้อละ 5 วิ |
| 2 ป่าหมอกมรณะ | Inheritance / Abstract / Interface | แม่มดป่าหมอก | 25 วิ | หมอกหนาทึบ: หมอกลอยบังกล่องโจทย์ |
| 3 ปล่องภูเขาไฟ | Polymorphism / Exception / Thread | จอมมารลาวา | 20 วิ | ฝนอุกกาบาต: ต้องคลิกทำลาย ถ้าโดนเสียเลือด 0.5 |

## เนื้อหา OOP อยู่ตรงไหน

| เรื่อง | ตัวอย่างในโปรเจกต์ |
|---|---|
| Inheritance | `GameCharacter` > `Enemy` > `SilverKnight`, `ForestWitch`, `DemonLord` และ `Level`, `Obstacle`, `Question`, `Effect` ที่มี class ลูกหลายตัว |
| Polymorphism | `BattlePanel` เรียก `question.isCorrect()`, `obstacle.adjustTimeLimit()`, `effect.update()` โดยไม่ต้องรู้ว่าเป็น class ลูกตัวไหน |
| Abstract class | `GameCharacter`, `Enemy`, `Question`, `Obstacle`, `Level`, `Effect` |
| Interface | `Drawable`, `Updatable`, `CountdownThread.Listener`, และ `Runnable` |
| Encapsulation | ตัวแปรเป็น `private` ทั้งหมด เช่น `hp` ใน `GameCharacter` แก้ได้ผ่าน `takeDamage()` เท่านั้น |
| GUI | `GameFrame` (JFrame + CardLayout), `MenuPanel`, `LevelSelectPanel`, `BattlePanel`, `QuestionPanel`, `ResultPanel`, `GameButton` |
| Graphics | `Animation` + `GameCharacter` วนรูปตัวละคร, `Graphics2D` วาดฉากใน `Level` ลูกแต่ละตัว, เอฟเฟกต์ใน `EnergyBall`, อุปสรรคใน `RainObstacle`, `FogObstacle`, `Meteor` |
| Threads | `GameLoop` (อัปเดตทุกเฟรม), `CountdownThread` (นับเวลาแต่ละข้อ), `MeteorObstacle` (ปล่อยอุกกาบาต ใช้ `synchronized` ป้องกัน list) |

## เพิ่มด่านใหม่

1. สร้าง class ลูกของ `Level` (เลือกศัตรู อุปสรรค โจทย์ และวาดฉาก)
2. เพิ่มลงใน array `levels` ของ `GameFrame`
