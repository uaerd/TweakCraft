package example.myplugin;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
public class Config {
    private File configFile;
    private Yaml yaml;
    private Map<String, Object> data;

    public Config(File pluginFolder) {
        // ตรวจสอบและสร้างไดเรกทอรี pluginFolder หากไม่มีอยู่
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();  // ใช้ mkdirs() เพื่อสร้างไดเรกทอรีและ parent directories ทั้งหมด
        }
    
        // สร้างไฟล์ config.yml ในไดเรกทอรี pluginFolder
        configFile = new File(pluginFolder, "config.yml");
        try {
            if (!configFile.exists()) {
                configFile.createNewFile();  // ใช้ createNewFile() เพื่อสร้างไฟล์ config.yml ใหม่
            }
        } catch (IOException e) {
            // จัดการกับข้อผิดพลาดในการสร้างไฟล์
            e.printStackTrace();
        }
    
        // สร้าง DumperOptions เพื่อกำหนดตัวเลือกการเขียน YAML
        DumperOptions options = new DumperOptions();
        options.setIndent(2);  // กำหนดขนาดการเยื้องเป็น 2 spaces
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);  // กำหนดรูปแบบการเขียนให้เป็น block style
    
        // ใช้ DumperOptions ในการสร้าง Yaml object
        yaml = new Yaml(options);
        data = loadData();  // โหลดข้อมูลจากไฟล์ config.yml
        saveData();
    }
    
    private Map<String, Object> loadData() {
        try (FileReader reader = new FileReader(configFile)) {
            Map<String, Object> loadedData = yaml.load(reader);
            if (loadedData != null) {
                return loadedData;
            }
        } catch (IOException e) {
            // จัดการกับข้อผิดพลาดในการโหลดไฟล์
            e.printStackTrace();
        }
        // ในกรณีที่โหลดข้อมูลไม่สำเร็จหรือไม่มีข้อมูลในไฟล์ config.yml ให้คืนค่า DefaultData()
        return DefaultData();
    }
    

    public void saveData() {
        try (FileWriter writer = new FileWriter(configFile)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Object value) {
        data.put(key, value);
        saveData();
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void remove(String key) {
        data.remove(key);
        saveData();
    }

    private Map<String, Object> DefaultData() {
        // สร้าง LinkedHashMap ที่จะใช้เป็น defaultData
        Map<String, Object> defaultData = new LinkedHashMap<>();
    
        Map<String, Object> defaultCommand = new LinkedHashMap<>();
        defaultCommand.put("op", true);
        defaultCommand.put("Admin", true);
    
        // เพิ่มข้อมูลเริ่มต้นลงใน defaultData
        defaultData.put("Admins", Arrays.asList()); // ใช้ List ว่างสำหรับ Admins
        defaultData.put("run-command-by", defaultCommand); // เพิ่ม defaultCommand ลงใน key "run-command-by"
        data = defaultData;
        // ส่งกลับ defaultData ที่มีข้อมูลเริ่มต้น
        return defaultData;
    }
    
}