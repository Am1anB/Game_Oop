import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * เครื่องมือสร้างรูปตัวละคร ไม่ได้เป็นส่วนหนึ่งของตัวเกม
 * รันครั้งเดียวเพื่อสร้าง assets/ ถ้าจะใช้รูปของตัวเองก็เอาไฟล์ไปทับได้เลย
 *
 * วิธีรัน: java tools\SpriteGenerator.java
 *
 * ภาพต้นแบบเก็บเป็น String ตัวอักษร 1 ตัว = 1 พิกเซล และ . = โปร่งใส ทุกตัวหันขวา
 * แต่ละท่าสร้างจากภาพต้นแบบ โดยแยกร่างเป็น 3 ส่วน คือ ตัวบน ขา และอาวุธ
 * แล้วขยับหรือหมุนแต่ละส่วนไม่เท่ากันในแต่ละเฟรม
 */
public class SpriteGenerator {
    private static final int SIZE = 24;       // ขนาดภาพต้นแบบ
    private static final int CANVAS_W = 40;   // เผื่อขอบให้อาวุธที่หมุนออกไป
    private static final int CANVAS_H = 36;
    private static final int OFFSET_X = 8;
    private static final int OFFSET_Y = 8;
    private static final int SCALE = 6;
    private static final int LEG_ROW = 19;    // ขาเริ่มที่แถวนี้

    /** ข้อมูลของตัวละคร 1 ตัวสำหรับสร้างท่าต่าง ๆ */
    private static class Model {
        String folder;
        String[] sprite;
        int weaponMinCol, weaponMinRow, weaponMaxRow; // ขอบเขตของอาวุธในภาพ
        int pivotCol, pivotRow;                       // จุดที่มือจับอาวุธ
        int legSplitCol;                              // คั่นขาซ้าย/ขวา (-1 = ชุดยาวไม่มีขา)
        int[] attackAngles;
        int[] attackPush;                             // ระยะดันอาวุธไปข้างหน้า
        int castAngle;

        Model(String folder, String[] sprite, int minCol, int minRow, int maxRow,
              int pivotCol, int pivotRow, int legSplitCol,
              int[] attackAngles, int[] attackPush, int castAngle) {
            this.folder = folder;
            this.sprite = sprite;
            this.weaponMinCol = minCol;
            this.weaponMinRow = minRow;
            this.weaponMaxRow = maxRow;
            this.pivotCol = pivotCol;
            this.pivotRow = pivotRow;
            this.legSplitCol = legSplitCol;
            this.attackAngles = attackAngles;
            this.attackPush = attackPush;
            this.castAngle = castAngle;
        }

        boolean isWeapon(int row, int col) {
            return col >= weaponMinCol && row >= weaponMinRow && row <= weaponMaxRow;
        }

        char at(int row, int col) {
            if (row < 0 || row >= SIZE || col < 0 || col >= sprite[row].length()) {
                return '.';
            }
            return sprite[row].charAt(col);
        }
    }

    /** ท่าทางของ 1 เฟรม */
    private static class Pose {
        int bodyX, bodyY;          // ตัวบนขยับไปเท่าไร
        int legSwing;              // ขาก้าวไปข้างหน้า/หลัง
        int weaponAngle, weaponPush;
        int lean;                  // เอนตัว (ใช้ตอนโดนตี)
    }

    public static void main(String[] args) throws IOException {
        Model[] models = {
            new Model("player", PLAYER, 16, 13, 19, 16, 16, 10,
                new int[] {-110, -60, 20, 45}, new int[] {0, 0, 1, 0}, -90),
            new Model("silver_knight", SILVER_KNIGHT, 18, 13, 21, 18, 16, 10,
                new int[] {-15, -5, 10, 5}, new int[] {-2, 0, 3, 1}, -20),
            new Model("forest_witch", FOREST_WITCH, 20, 6, 23, 20, 16, -1,
                new int[] {-25, 10, 50, 30}, new int[] {0, 1, 2, 1}, 20),
            new Model("demon_lord", DEMON_LORD, 20, 10, 22, 20, 15, 11,
                new int[] {-30, 10, 60, 35}, new int[] {0, 0, 1, 0}, -15),
        };
        for (Model model : models) {
            File folder = new File("assets", model.folder);
            folder.mkdirs();
            generate(model, folder);
        }
        System.out.println("สร้างรูปใน assets/ เรียบร้อย");
    }

    private static void generate(Model m, File folder) throws IOException {
        // ยืนหายใจ: ตัวบนยุบลง 1 พิกเซลสลับกับปกติ
        int[] idleBody = {0, 0, 1, 1};
        for (int i = 0; i < idleBody.length; i++) {
            Pose p = new Pose();
            p.bodyY = idleBody[i];
            save(render(m, p), folder, "idle", i);
        }

        // วิ่ง: ขาสลับหน้าหลัง ตัวกระเด้ง อาวุธแกว่ง
        int[] swing = {3, 0, -3, 0};
        int[] bob = {0, 1, 0, 1};
        int[] armSwing = {-12, 0, 12, 0};
        for (int i = 0; i < swing.length; i++) {
            Pose p = new Pose();
            p.legSwing = swing[i];
            p.bodyY = bob[i];
            p.weaponAngle = armSwing[i];
            save(render(m, p), folder, "run", i);
        }

        // โจมตี: เงื้ออาวุธ > ฟันลง > ค้างท่า
        int[] attackBody = {-1, 0, 2, 1};
        for (int i = 0; i < m.attackAngles.length; i++) {
            Pose p = new Pose();
            p.bodyX = attackBody[i];
            p.weaponAngle = m.attackAngles[i];
            p.weaponPush = m.attackPush[i];
            p.legSwing = i >= 2 ? 1 : 0;
            save(render(m, p), folder, "attack", i);
        }

        // ร่ายพลัง: ชูอาวุธค้างไว้
        for (int i = 0; i < 3; i++) {
            Pose p = new Pose();
            p.weaponAngle = m.castAngle;
            p.bodyY = i == 1 ? 1 : 0;
            save(render(m, p), folder, "cast", i);
        }

        // โดนตี: เอนตัวไปด้านหลัง
        for (int i = 0; i < 2; i++) {
            Pose p = new Pose();
            p.lean = i == 0 ? 3 : 2;
            p.bodyX = -1;
            p.weaponAngle = -20;
            save(render(m, p), folder, "hurt", i);
        }

        // ล้ม: หมุนทั้งตัวไปด้านหลังจนนอนราบ
        int[] fallAngles = {-20, -45, -70, -90};
        for (int i = 0; i < fallAngles.length; i++) {
            save(rotateWhole(render(m, new Pose()), fallAngles[i]), folder, "dead", i);
        }
    }

    private static BufferedImage render(Model m, Pose p) {
        BufferedImage image = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_ARGB);

        // วาดขาก่อน ตัวบนจะได้ทับรอยต่อ
        for (int row = LEG_ROW; row < SIZE; row++) {
            int depth = row - LEG_ROW + 1;
            int shift = p.legSwing * depth / (SIZE - LEG_ROW);
            for (int col = 0; col < SIZE; col++) {
                if (m.isWeapon(row, col)) {
                    continue;
                }
                int x = col;
                int y = row;
                if (m.legSplitCol < 0) {
                    x += shift / 2; // ชุดยาว: แกว่งชายกระโปรงทั้งผืน
                } else if (col < m.legSplitCol) {
                    x += shift;
                    y -= p.legSwing < 0 && row == SIZE - 1 ? 1 : 0;
                } else {
                    x -= shift;
                    y -= p.legSwing > 0 && row == SIZE - 1 ? 1 : 0;
                }
                put(image, x + p.lean / 2 * -1, y, m.at(row, col));
            }
        }

        // ตัวบน
        for (int row = 0; row < LEG_ROW; row++) {
            int leanShift = -p.lean * (LEG_ROW - row) / LEG_ROW;
            for (int col = 0; col < SIZE; col++) {
                if (!m.isWeapon(row, col)) {
                    put(image, col + p.bodyX + leanShift, row + p.bodyY, m.at(row, col));
                }
            }
        }

        drawWeapon(image, m, p);
        return image;
    }

    /** หมุนอาวุธรอบมือ โดยไล่ทุกช่องปลายทางแล้วหมุนกลับไปดูว่าตรงกับพิกเซลไหนของต้นฉบับ */
    private static void drawWeapon(BufferedImage image, Model m, Pose p) {
        double radians = Math.toRadians(-p.weaponAngle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        int pivotX = m.pivotCol + p.bodyX + p.weaponPush;
        int pivotY = m.pivotRow + p.bodyY;

        for (int y = -OFFSET_Y; y < CANVAS_H - OFFSET_Y; y++) {
            for (int x = -OFFSET_X; x < CANVAS_W - OFFSET_X; x++) {
                double dx = x - pivotX;
                double dy = y - pivotY;
                int sourceCol = (int) Math.round(m.pivotCol + dx * cos - dy * sin);
                int sourceRow = (int) Math.round(m.pivotRow + dx * sin + dy * cos);
                if (m.isWeapon(sourceRow, sourceCol)) {
                    put(image, x, y, m.at(sourceRow, sourceCol));
                }
            }
        }
    }

    private static BufferedImage rotateWhole(BufferedImage source, int angle) {
        BufferedImage result = new BufferedImage(CANVAS_W, CANVAS_H, BufferedImage.TYPE_INT_ARGB);
        double radians = Math.toRadians(-angle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double centerX = OFFSET_X + SIZE / 2.0;
        double centerY = OFFSET_Y + SIZE / 2.0;
        for (int y = 0; y < CANVAS_H; y++) {
            for (int x = 0; x < CANVAS_W; x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                int sourceX = (int) Math.round(centerX + dx * cos - dy * sin);
                int sourceY = (int) Math.round(centerY + dx * sin + dy * cos);
                if (sourceX >= 0 && sourceX < CANVAS_W && sourceY >= 0 && sourceY < CANVAS_H) {
                    result.setRGB(x, y, source.getRGB(sourceX, sourceY));
                }
            }
        }
        return result;
    }

    private static void put(BufferedImage image, int col, int row, char pixel) {
        Color color = colorOf(pixel);
        int x = col + OFFSET_X;
        int y = row + OFFSET_Y;
        if (color != null && x >= 0 && x < CANVAS_W && y >= 0 && y < CANVAS_H) {
            image.setRGB(x, y, color.getRGB());
        }
    }

    private static void save(BufferedImage image, File folder, String pose, int index) throws IOException {
        BufferedImage scaled = new BufferedImage(CANVAS_W * SCALE, CANVAS_H * SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.drawImage(image, 0, 0, CANVAS_W * SCALE, CANVAS_H * SCALE, null);
        g.dispose();
        ImageIO.write(scaled, "png", new File(folder, pose + "_" + index + ".png"));
    }

    private static Color colorOf(char c) {
        switch (c) {
            case 'K': return new Color(16, 16, 24);
            case 'B': return new Color(40, 120, 215);
            case 'L': return new Color(120, 195, 245);
            case 'C': return new Color(90, 240, 240);
            case 'O': return new Color(235, 140, 40);
            case 'Y': return new Color(250, 205, 70);
            case 'W': return new Color(240, 240, 240);
            case 'G': return new Color(165, 165, 175);
            case 'D': return new Color(70, 70, 85);
            case 'R': return new Color(215, 35, 35);
            case 'M': return new Color(115, 15, 25);
            case 'P': return new Color(95, 45, 150);
            case 'V': return new Color(165, 100, 220);
            case 'N': return new Color(95, 175, 85);
            case 'F': return new Color(240, 190, 145);
            case 'T': return new Color(150, 105, 60);
            default:  return null;
        }
    }

    private static final String[] PLAYER = {
        "...OO..........OO.......",
        "..OOK..........KOO......",
        "..OK............KO......",
        "..OK...KKKKKK...KO......",
        "..KOK.KBBBBBBK.KOK......",
        "...KOKBLLBBBBBKOK.......",
        "....KBLBBBBBBBBK........",
        "....KBBBBBBBBBBK........",
        "....KBKKKKKKKKBK........",
        "....KBKKCKKCKKBK........",
        "....KBBKKKKKKBBK........",
        ".....KBBBKKBBBK.........",
        "...KKKKBBBBBBKKKK.......",
        "..KLLBKKKKKKKKBLLK..Y...",
        "..KLBBBKOYYOKBBBLK.KYK..",
        ".KBBKBBBKOOKBBBKBKKKYWWW",
        ".KBBKBBBBBBBBBBKFFKYYLLW",
        ".KBBKBBBBBBBBBBKKKKKYWWW",
        "..KK.KYYYYYYYYK....KYK..",
        ".....KBBBKKBBBK.....Y...",
        ".....KBBK..KBBK.........",
        "....KLBBK..KBLBK........",
        "....KKKKK..KKKKK........",
        "...KKKKKK..KKKKKK.......",
    };

    private static final String[] SILVER_KNIGHT = {
        ".......RRRRR............",
        "......RRRRRRR...........",
        ".....RRRMMRRRR..........",
        ".....RRKKKKKRR..........",
        ".....RKGGGGGGKR.........",
        "......KGWWGGGGK.........",
        "......KGWGGGGGK.........",
        "......KYYYYYYYK.........",
        "......KKKKYKKKK.........",
        "......KGKKYKKGK.........",
        "......KGGGYGGGK.........",
        ".......KKGYGKK..........",
        "....KKKKGGGGGKKKK.......",
        "...KGGGKKKKKKKGGGK.KKKK.",
        "..KWWGGKWWRWWKGGGKKWWWWK",
        "..KWGGKGWWRWWGKGGKKWRRWK",
        "..KGGGKGRRRRRGKFFKKRRRRK",
        "..KKKK.KGWRWGK.KKKKWRRWK",
        "......KDDDDDDDK....KWWWK",
        "......KGGGKGGGK.....KWK.",
        "......KGGK.KGGK......K..",
        ".....KWGGK.KGGWK........",
        ".....KKKKK.KKKKK........",
        "....KKKKKK.KKKKKK.......",
    };

    private static final String[] FOREST_WITCH = {
        "...........KK...........",
        "..........KPPK..........",
        ".........KPPVK..........",
        "........KPPPPK..........",
        "........KPPPPPK.........",
        ".......KPPPPVPPK........",
        ".......KPPPPPPPK.....NN.",
        ".....KKKYYYYYYYKKK..NCCN",
        "...KKPPPPPPPPPPPPPKKNCCN",
        ".....KKKNNNNNNNKKK...NN.",
        ".......KNKNNNKNK.....KT.",
        ".......KNNNNNNNK.....TK.",
        ".......KNNNMMNNK.....KT.",
        "........KKNNNKK......TK.",
        "......KKPPPPPPPKKK..KT..",
        ".....KPVPPYYYPPPVPKFFT..",
        "....KPVPKPPYPPKPPPVFFK..",
        "....KFFKKPPPPPPK.KKKTK..",
        ".....KK.KPPVPPPK....KT..",
        "........KPPVPPPPK...TK..",
        ".......KPPPVPPPPPK..KT..",
        "......KPPPVPPPPPPPK.TK..",
        ".....KPPPVPPPPPPPPPKKT..",
        ".....KKKKKKKKKKKKKKK.K..",
    };

    private static final String[] DEMON_LORD = {
        "..YY............YY......",
        "..YTK..........KTY......",
        "..KTTK........KTTK......",
        "...KTTKKKKKKKKTTK.......",
        "....KKRRRRRRRRKK........",
        "....KRRRRRRRRRRK........",
        "....KRKKRRRRKKRK........",
        "....KRYYKRRKYYRK........",
        "....KRRRRRRRRRRK........",
        ".....KRKWKWKWRK.........",
        "KK....KRRRRRRK.......OO.",
        "KMKKKKKKDDDDKKKKKKM.OYYO",
        "KMMMKDDDDDDDDDDDDKMMOYYO",
        "KMMMKDDRDDDDDDRDDKMMKOOK",
        ".KMMKDKDDRYRDDKDDKFFKDK.",
        ".KMMKDKDRYYYRDKDKFFKKDK.",
        "..KMKFFKDRYRDDKKKKK..DK.",
        "...KMFFKDDDDDDK......DK.",
        "....KKK.KYYYYYK......DK.",
        "........KDDKDDK......DK.",
        ".......KDDDKDDDK.....DK.",
        ".......KRRDKDRRK.....DK.",
        "......KKKKKKKKKKK....KK.",
        ".....KKKKKK.KKKKKK......",
    };
}
