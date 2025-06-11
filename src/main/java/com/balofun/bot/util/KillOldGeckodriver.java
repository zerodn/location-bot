package com.balofun.bot.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KillOldGeckodriver {
    public static void kill() {
        try {
            // Chạy lệnh lấy danh sách tiến trình geckodriver_v0.36.0.exe
            Process process = Runtime.getRuntime().exec("wmic process where name='geckodriver_v0.36.0.exe' get ProcessId,CreationDate");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            List<ProcessInfo> processes = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.toLowerCase().contains("processid")) continue;

                String[] parts = line.trim().split("\\s+");
                if (parts.length == 2) {
                    String creationDate = parts[0]; // Thời gian tạo tiến trình
                    int pid = Integer.parseInt(parts[1]); // Process ID
                    processes.add(new ProcessInfo(pid, creationDate));
                }
            }

            // Sắp xếp danh sách theo thời gian tạo (mới nhất -> cũ nhất)
            Collections.sort(processes, (a, b) -> b.creationDate.compareTo(a.creationDate));

            // Giữ lại 3 tiến trình mới nhất, kill những tiến trình còn lại
            if (processes.size() > 3) {
                for (int i = 3; i < processes.size(); i++) {
                    int pidToKill = processes.get(i).pid;
                    System.out.println("Killing process: " + pidToKill);
                    Runtime.getRuntime().exec("taskkill /F /PID " + pidToKill);
                }
            } else {
                System.out.println("Không có quá 3 tiến trình, không cần xóa.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
