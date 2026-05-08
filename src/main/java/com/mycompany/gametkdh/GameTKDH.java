/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.gametkdh;

/**
 *
 * @author lequo
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class GameTKDH extends JPanel implements ActionListener, MouseMotionListener, MouseListener, KeyListener {
    int windowWidth = 900;
    int windowHeight = 600;
    BufferedImage healthImage = Image.loadImage("/Health.png");
    BufferedImage mapBackground = Image.loadImage("/grass.png");
    CoinAnimation coinAnimation = new CoinAnimation();
    Player player = new Player();
    Compass compass = new Compass();
    Sword sword = new Sword();

    Enemy[] enemies = new Enemy[10];
    ArrayList<Boss> bosses = new ArrayList<>();
    ArrayList<Shuriken> shurikens = new ArrayList<>();
    ArrayList<Obstacle> obstacles = new ArrayList<>();
    // Trong hàm khởi tạo GameTKDH, thêm một vài bức tường mẫu:
    
    PauseGame pauseGame = new PauseGame();
    int score = 0;

    double cameraX = 0;
    double cameraY = 0;

    // --- CÁC BIẾN CHO HIỆU ỨNG NHẤP NHÁY ---
    int frameCount = 0;
    int previousHp = 5; 
    int flashWarningTimer = 0;

    Timer timer = new Timer(16, this);
    Clip backgroundMusic;
    
    GameOverScreen gameOverScreen = new GameOverScreen();
    JFrame frame;
    
    public GameTKDH(JFrame frame) {
        
        this.frame = frame;
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setBackground(new Color(220,220,220));
        setFocusable(true);

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
        }

        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        playBackgroundMusic("/sound.wav", -20.0f);
        
        //tường
        obstacles.add(new Obstacle(-3.0, 2.0, 2.0, 0.3)); // ngang trên trái

        obstacles.add(new Obstacle(2.0, 1.0, 0.3, 2.0)); // dọc bên phải

        obstacles.add(new Obstacle(-1.0, -2.0, 3.0, 0.3)); // ngang dưới

        obstacles.add(new Obstacle(-4.0, -1.0, 0.3, 2.5)); // dọc bên trái

        obstacles.add(new Obstacle(1.0, 3.0, 1.5, 0.3)); // ngang trên
        //timer.start();
    }
    
    public void playBackgroundMusic(String soundFileName, float volume) {
        try {

            File soundFile = new File("data" + soundFileName);

            if (!soundFile.exists()) {
                System.err.println("Không tìm thấy file nhạc: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);

            // chỉnh volume
            if (backgroundMusic.isControlSupported(FloatControl.Type.MASTER_GAIN)) {

                FloatControl gainControl =
                    (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);

                gainControl.setValue(volume);
            }

            // loop vô hạn
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

            backgroundMusic.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void playSound(String soundFileName, float volume) {
        try {
            // Tạo đối tượng File trỏ vào thư mục data (giống cách làm của class Image)
            File soundFile = new File("data" + soundFileName);

            if (!soundFile.exists()) {
                System.err.println("Không tìm thấy file âm thanh tại: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(volume); // volume là giá trị dB (ví dụ: -10.0f để giảm âm thanh)
            }
            
            clip.start();

            // Tự động đóng clip sau khi phát xong để tránh tốn bộ nhớ
            clip.addLineListener(event -> {
                if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception ex) {
            System.err.println("Lỗi phát âm thanh: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void startGame() {
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // =========================
        // VẼ WORLD: map, enemy, player, sword
        // =========================
        Graphics2D world = (Graphics2D) g.create();
        world.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        world.translate(getWidth() / 2, getHeight() / 2);
        // Lấy chiều cao (Height) làm thước đo chuẩn cho cả 2 trục
        double scale = getHeight() / 2.0 / 1000.0; 
        world.scale(getWidth() / 2.0 / 1000.0, -getHeight() / 2.0 / 1000.0);

        // giống glTranslatef(-cameraX, -cameraY, 0)
        world.translate(-cameraX * 1000, -cameraY * 1000);

        drawMap(world);

        for (Enemy e : enemies) {
            e.draw(world);
        }
        
        // VẼ BOSS VÀ PHI TIÊU
        for (Boss b : bosses) {
            b.draw(world);
        }
        for (Shuriken s : shurikens) {
            s.draw(world);
        }
        
        if (player.isAlive()) {
            player.draw(world);
            sword.draw(world, player);
        }

         // Vẽ trong khối world.draw...
        for (Obstacle ob : obstacles) {
            ob.draw(world);
        }
        world.dispose();


        // =========================
        // VẼ UI: máu, score, la bàn
        // =========================
        Graphics2D ui = (Graphics2D) g.create();
        ui.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ui.translate(getWidth() / 2, getHeight() / 2);
        // Lấy chiều cao (Height) làm chuẩn cho UI
        ui.scale(getWidth() / 2.0 / 1000.0, -getHeight() / 2.0 / 1000.0);

        // KHÔNG có camera ở đây
        drawHealth(ui);
        drawScore(ui);
        pauseGame.draw((Graphics2D) g, getWidth(), getHeight());
        compass.draw(ui, player, enemies);

        if (!player.isAlive()) {
            gameOverScreen.draw((Graphics2D) g, getWidth(), getHeight(), score);
        }

        ui.dispose();

        // =========================
        // THÊM: HIỆU ỨNG VIỀN ĐỎ NHẤP NHÁY
        // =========================
        if (player.isAlive()) {
            boolean drawRedBorder = false;

            if (player.hp == 1) {
                // CÒN 1 MÁU: Nhấp nháy liên tục (chu kỳ 40 frame: 20 hiện, 20 ẩn)
                if (frameCount % 40 < 20) {
                    drawRedBorder = true;
                }
            } else if (flashWarningTimer > 0) {
                // NHẬN SÁT THƯƠNG: Nhấp nháy 3 lần (chu kỳ 20 frame: 10 hiện, 10 ẩn)
                if (flashWarningTimer % 20 >= 10) {
                    drawRedBorder = true;
                }
            }

            if (drawRedBorder) {
                Graphics2D gBorder = (Graphics2D) g.create();
                
                int thickness = 16; 
                gBorder.setColor(new Color(255, 0, 0, 150)); 
                gBorder.setStroke(new BasicStroke(thickness));
                
                gBorder.drawRect(thickness / 2, thickness / 2, getWidth() - thickness, getHeight() - thickness);
                
                gBorder.dispose();
            }
        }
        
       
    }

    void drawMap(Graphics2D g) {
        int mapSize = 9000; // kích thước map

        g.drawImage(
            mapBackground,
            -mapSize / 2,
            -mapSize / 2,
            mapSize,
            mapSize,
            null
        );
    }

    void drawHealth(Graphics2D g) {

        int heartW = 80;
        int heartH = 80;

        int startX = 480;
        int startY = 820;

        for (int i = 0; i < player.hp; i++) {

            int x = startX + i * 85;

            g.drawImage(
                    healthImage,
                    x,
                    startY,
                    heartW,
                    heartH,
                    null
            );
        }
    }

    void drawScore(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.scale(1, -1);

        int coinX = (int)(0.48 * 1000);
        int coinY = (int)(-0.72 * 1000);

        coinAnimation.draw(g2, coinX, coinY - 45, 60, 60);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString("Coins: " + score, coinX + 80, coinY);

        g2.dispose();
    }

    void drawGameOver(Graphics2D g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.scale(1, -1);
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString("GAME OVER", -130, 0);

        g2.dispose();
    }

    void restartGame() {
        player = new Player();
        sword = new Sword();
        compass = new Compass();
        pauseGame = new PauseGame();
        gameOverScreen = new GameOverScreen();

        score = 0;
        cameraX = 0;
        cameraY = 0;
        
        // Reset các biến máu và nhấp nháy
        previousHp = player.hp;
        flashWarningTimer = 0;

        for (int i = 0; i < enemies.length; i++) {
            enemies[i] = new Enemy();
        }

        timer.start();
        requestFocusInWindow();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pauseGame.isPaused() && player.isAlive()) {
            frameCount++; 
            
            player.update(obstacles);
            
            // XỬ LÝ PHÁT HIỆN MẤT MÁU
            if (player.hp < previousHp) {
                if (player.hp > 1) {
                    // Nếu lớn hơn 1 máu, chạy timer nhấp nháy 3 lần (60 frames)
                    flashWarningTimer = 60; 
                }
                previousHp = player.hp;
            } else if (player.hp > previousHp) {
                // Cập nhật lại trong trường hợp nhặt được máu
                previousHp = player.hp; 
            }
            
            // Đếm ngược timer cảnh báo
            if (flashWarningTimer > 0) {
                flashWarningTimer--;
            }

            for (Enemy enemy : enemies) {
                enemy.update(player);
            }

            coinAnimation.update();
            
            int previousScore = score;
            score += sword.update(player, enemies, bosses);
            
            if (score > previousScore) {
                playSound("/sound_coin.wav", -20.0f); 
            }

            // sinh boss
            if (score / 5 > previousScore / 5) {
                int numberOfNewBosses = (score / 5) - (previousScore / 5);
                for (int i = 0; i < numberOfNewBosses; i++) {
                    bosses.add(new Boss());
                }
            }
            // Cập nhật Boss
            for (Boss b : bosses) {
                b.update(player, shurikens);
            }

            // Cập nhật Phi tiêu 
            for (int i = shurikens.size() - 1; i >= 0; i--) {
                Shuriken s = shurikens.get(i);
                s.update(player);
                if (!s.active) {
                    shurikens.remove(i);
                }
            }
            
            cameraX = player.x;
            cameraY = player.y;
        }

        repaint();
    }

    double screenToWorldX(int mouseX) {
        return cameraX + ((double) mouseX / getWidth() * 2.0 - 1.0);
    }

    double screenToWorldY(int mouseY) {
        return cameraY + (1.0 - (double) mouseY / getHeight() * 2.0);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        pauseGame.handleMouseMoved(e.getPoint());
        repaint();

        if (!pauseGame.isPaused()) {
            player.updateAngle(screenToWorldX(e.getX()), screenToWorldY(e.getY()));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!player.isAlive()) {
            if (gameOverScreen.clickRestart(e.getPoint())) {
                restartGame();
                repaint();
                return;
            }

            if (gameOverScreen.clickHome(e.getPoint())) {
                timer.stop();

                GameMenu menu = new GameMenu(frame);

                frame.setContentPane(menu);
                frame.revalidate();
                frame.repaint();

                return;
            }

            return;
        }
        if (pauseGame.handleMousePressed(e.getPoint())) {
            repaint();
            return;
        }
        if (player.isAlive() && e.getButton() == MouseEvent.BUTTON1) {
            player.setTarget(screenToWorldX(e.getX()), screenToWorldY(e.getY()));
        }
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (player.isAlive() && e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!sword.attacking) {
                playSound("/sound_sword.wav",-20); // Gọi file âm thanh vung kiếm
            }
            sword.startAttack();
        }
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game TKDH");

        GameMenu menu = new GameMenu(frame);

        frame.setContentPane(menu);
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}