import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** หน้าต่างหลัก ใช้ CardLayout สลับระหว่างหน้าจอต่าง ๆ */
public class GameFrame extends JFrame {
    private static final String MENU = "menu";
    private static final String LEVEL_SELECT = "levelSelect";
    private static final String BATTLE = "battle";
    private static final String RESULT = "result";

    // เพิ่มด่านใหม่ได้ที่นี่ที่เดียว
    private final Level[] levels = {new GrasslandLevel(), new FogForestLevel(), new VolcanoLevel()};

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final BattlePanel battlePanel = new BattlePanel(this);
    private final ResultPanel resultPanel = new ResultPanel(this);

    public GameFrame() {
        super("แก้โจทย์ต่อสู้ (Problem Battle)");
        root.setPreferredSize(new Dimension(GameConfig.WIDTH, GameConfig.HEIGHT));
        root.add(new MenuPanel(this), MENU);
        root.add(new LevelSelectPanel(this, levels), LEVEL_SELECT);
        root.add(battlePanel, BATTLE);
        root.add(resultPanel, RESULT);

        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }

    public void showMenu() {
        cards.show(root, MENU);
    }

    public void showLevelSelect() {
        cards.show(root, LEVEL_SELECT);
    }

    public void startLevel(int index) {
        cards.show(root, BATTLE);
        battlePanel.startLevel(levels[index]);
    }

    public void showResult(Level level, boolean won, int correct, int wrong, double hpLeft) {
        int index = java.util.Arrays.asList(levels).indexOf(level);
        boolean hasNextLevel = index + 1 < levels.length;
        resultPanel.showResult(level, index, hasNextLevel, won, correct, wrong, hpLeft);
        cards.show(root, RESULT);
    }
}
