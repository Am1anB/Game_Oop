import java.util.List;

/** กติกาการต่อสู้ แยกจากหน้าจอ ไม่มีโค้ดวาดรูปในนี้ */
public class Battle {
    public static final int STREAK_FOR_SPECIAL = 3;

    private final Player player;
    private final Enemy enemy;
    private final List<Question> questions;
    private int questionIndex;
    private int correctStreak;
    private int wrongStreak;
    private int correctCount;
    private int wrongCount;

    public Battle(Player player, Enemy enemy, List<Question> questions) {
        this.player = player;
        this.enemy = enemy;
        this.questions = questions;
    }

    /** โจทย์หมดแล้วจะวนกลับไปข้อแรก */
    public Question nextQuestion() {
        Question question = questions.get(questionIndex % questions.size());
        questionIndex++;
        return question;
    }

    public int getQuestionNumber() {
        return questionIndex;
    }

    /** ตอบถูก เราตี / ตอบผิด ศัตรูตี / ติดกันครบ 3 ครั้ง ฝ่ายที่ตีได้ใช้ท่าไม้ตาย */
    public Attack submitAnswer(boolean correct) {
        if (correct) {
            correctCount++;
            wrongStreak = 0;
            correctStreak++;
            boolean special = correctStreak >= STREAK_FOR_SPECIAL;
            if (special) {
                correctStreak = 0;
            }
            return new Attack(player, enemy, special);
        }
        wrongCount++;
        correctStreak = 0;
        wrongStreak++;
        boolean special = wrongStreak >= STREAK_FOR_SPECIAL;
        if (special) {
            wrongStreak = 0;
        }
        return new Attack(enemy, player, special);
    }

    public boolean isOver() {
        return player.isDead() || enemy.isDead();
    }

    public boolean isPlayerWinner() {
        return enemy.isDead() && !player.isDead();
    }

    public int getCorrectStreak() {
        return correctStreak;
    }

    public int getWrongStreak() {
        return wrongStreak;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }
}
