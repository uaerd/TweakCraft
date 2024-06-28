package example.myplugin;

import org.yaml.snakeyaml.Yaml;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
public class badwords {
    private File dataFile;
    private Yaml yaml;
    private Map<String, Object> data;

    public badwords(File pluginFolder) {
        // ตรวจสอบและสร้างไดเรกทอรี pluginFolder หากไม่มีอยู่
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();  // ใช้ mkdirs() เพื่อสร้างไดเรกทอรีและ parent directories ทั้งหมด
        }
    
        // สร้างไฟล์ config.yml ในไดเรกทอรี pluginFolder
        dataFile = new File(pluginFolder, "badwords.yml");
        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();  // ใช้ createNewFile() เพื่อสร้างไฟล์ config.yml ใหม่
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // สร้าง DumperOptions เพื่อกำหนดตัวเลือกการเขียน YAML
        DumperOptions options = new DumperOptions();
        options.setIndent(2);  // กำหนดขนาดการเยื้องเป็น 2 spaces
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);  // กำหนดรูปแบบการเขียนให้เป็น block style
    
        // ใช้ DumperOptions ในการสร้าง Yaml object
        yaml = new Yaml(options);
        data = loadData();  // โหลดข้อมูลจากไฟล์
    }

    private Map<String, Object> loadData() {
        try (FileReader reader = new FileReader(dataFile)) {
            Map<String, Object> loadedData = yaml.load(reader);
            if (loadedData != null) {
                return loadedData;
            }
        } catch (IOException e) {
            // จัดการกับข้อผิดพลาดในการโหลดไฟล์
            e.printStackTrace();
        }
        // ในกรณีที่โหลดข้อมูลไม่สำเร็จหรือไม่มีข้อมูลในไฟล์  ให้คืนค่า DefaultData()
        return DefaultData();
    }

    public void saveData() {
        try (FileWriter writer = new FileWriter(dataFile)) {
            yaml.dump(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean containsBadWord(String message) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
    
            // ตรวจสอบว่า value เป็น List<String> หรือไม่
            if (value instanceof List) {
                List<String> badWords = (List<String>) value; // แปลง value เป็น List<String>
    
                // วนลูปตรวจสอบคำหยาบใน List ของคำหยาบ
                for (String badWord : badWords) {
                    if (message.toLowerCase().contains(badWord.toLowerCase())) {
                        return true; // พบคำหยาบในข้อความ
                    }
                }
            }
        }
        return false; // ไม่พบคำหยาบในข้อความ
    }
    
    private Map<String, Object> DefaultData() {
        // สร้าง LinkedHashMap ที่จะใช้เป็น defaultData
        Map<String, Object> defaultData = new LinkedHashMap<>();
    
        List<String> defaultbadwords_en = Arrays.asList(
            "พ่อมึงตาย", "แม่มึงตาย", "ควย", "ไอสัส", 
            "สัส", "ไอเหี้ย", "เหี้ย", "ไอลูกกระรี่", "ควาย",
             "สมองหมาปัญญาควาย", "โง่", "เบิ้นเลิ้นเอ้ย");
        List<String> defaultbadwords_th = Arrays.asList(
            "fuck", "Noob", "ass hole");
        // เพิ่มข้อมูลเริ่มต้นลงใน defaultData
        defaultData.put("en", defaultbadwords_en); // เริ่มต้นด้วย Map ว่างสำหรับ Admin
        defaultData.put("th", defaultbadwords_th); // เพิ่ม List defaultCommand ลงใน key "command"
        data = defaultData;
        saveData();
        // ส่งกลับ defaultData ที่มีข้อมูลเริ่มต้น
        return defaultData;
    }
}
