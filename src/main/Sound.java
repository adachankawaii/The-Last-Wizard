package main;

import javax.sound.sampled.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

public class Sound {
    private HashMap<String, Clip> soundEffects;
    private HashMap<String, FloatControl> volumeControls;

    public Sound() {
        soundEffects = new HashMap<>();
        volumeControls = new HashMap<>();

        // Import các tệp âm thanh tại đây
        addSound("background", "/sound/bgmusic.wav");
        addSound("got_sth", "/sound/got_sth.wav");
        addSound("wand", "/sound/wand.wav");
        addSound("player_die", "/sound/player_die.wav");
        addSound("slime_die", "/sound/slime_die.wav");
    }

    // Phương thức để thêm âm thanh vào danh sách quản lý
    private void addSound(String name, String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.out.println("Sound file not found: " + path);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Thêm clip vào danh sách soundEffects
            soundEffects.put(name, clip);

            // Lấy FloatControl để điều chỉnh âm lượng và thêm vào danh sách nếu được hỗ trợ
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControls.put(name, volumeControl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức để phát âm thanh
    public void play(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0); // Đưa về đầu
            clip.start();
        } else {
            System.out.println("Sound not found: " + name);
        }
    }

    // Phương thức để lặp âm thanh
    public void loop(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.out.println("Sound not found: " + name);
        }
    }

    // Phương thức để dừng âm thanh
    public void stop(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            clip.stop();
        } else {
            System.out.println("Sound not found: " + name);
        }
    }

    // Phương thức để tăng âm lượng từ từ
    public void increaseVolume(String name) {
        FloatControl volumeControl = volumeControls.get(name);
        if (volumeControl != null) {
            float currentVolume = volumeControl.getValue();
            float maxVolume = volumeControl.getMaximum();
            float newVolume = Math.min(currentVolume + 2.0f, maxVolume); // Tăng dần từng bước nhỏ (1.0f)
            volumeControl.setValue(newVolume);
        }
    }

    // Phương thức để giảm âm lượng từ từ
    public void decreaseVolume(String name) {
        FloatControl volumeControl = volumeControls.get(name);
        if (volumeControl != null) {
            float currentVolume = volumeControl.getValue();
            float minVolume = volumeControl.getMinimum();
            float newVolume = Math.max(currentVolume - 2.0f, minVolume); // Giảm dần từng bước nhỏ (1.0f)
            volumeControl.setValue(newVolume);
        }
    }

    public void setVolume(String name, float volume) {
        FloatControl volumeControl = volumeControls.get(name);
        if (volumeControl != null) {
            float minVolume = volumeControl.getMinimum();
            float maxVolume = volumeControl.getMaximum();
            float newVolume = Math.max(minVolume, Math.min(volume, maxVolume)); // Đảm bảo giá trị âm lượng nằm trong giới hạn
            volumeControl.setValue(newVolume);
        } else {
            System.out.println("Volume control not supported or sound not found: " + name);
        }
    }

    public void setVolumeAll(float volume) {
        for (String name : volumeControls.keySet()) {
            FloatControl volumeControl = volumeControls.get(name);
            if (volumeControl != null) {
                float minVolume = volumeControl.getMinimum();
                float maxVolume = volumeControl.getMaximum();
                float newVolume = Math.max(minVolume, Math.min(volume, maxVolume)); // Đảm bảo giá trị âm lượng nằm trong giới hạn
                volumeControl.setValue(newVolume);
            }
        }
    }

    public void muteAll() {
        for (String name : volumeControls.keySet()) {
            FloatControl volumeControl = volumeControls.get(name);
            if (volumeControl != null) {
                float minVolume = volumeControl.getMinimum();
                volumeControl.setValue(minVolume);
            }
        }
    }
}

