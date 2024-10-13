
package com.orrish.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
class MockController {

    String fileName = "response.json";
    Queue<String> fileQueue = new ArrayDeque<>();
    Queue<Integer> statusQueue = new ArrayDeque<>();

    @GetMapping({"setFile"})
    public ResponseEntity<Object> setFile(@RequestParam String fileName, @RequestBody Map payload) {
        fileQueue.clear();
        statusQueue.clear();
        Map<String, String> map = new HashMap<>();
        if (!new File(fileName).exists()) {
            map.put("error", "No file exists with the name " + fileName + " at the same location as the jar.");
            return ResponseEntity.badRequest().body(map);
        }
        this.fileName = fileName;
        map.put("message", "File set.");
        return ResponseEntity.ok(map);
    }

    @PostMapping({"setFiles"})
    public ResponseEntity<Object> setFiles(@RequestBody Map payload, @RequestHeader Map headers) {

        fileQueue = new ArrayDeque<>((Collection) payload.get("fileList"));
        if (payload.containsKey("statusList"))
            statusQueue = new ArrayDeque<>((Collection) payload.get("statusList"));
        else {
            statusQueue.clear();
            for (int i = 0; i < fileQueue.size(); i++) {
                statusQueue.add(200);
            }
        }

        Map<String, String> map = new HashMap<>();
        if (fileQueue.size() != statusQueue.size()) {
            map.put("error", "Size of list of response files and status should match.");
            return ResponseEntity.badRequest().body(map);
        }
        for (String eachFile : fileQueue) {
            if (!new File(eachFile).exists()) {
                fileQueue.clear();
                statusQueue.clear();
                map.put("error", "No file exists with the name " + eachFile + " at the same location as the jar.");
                return ResponseEntity.badRequest().body(map);
            }
        }
        map.put("message", "Files and status set.");
        return ResponseEntity.ok(map);
    }

    @RequestMapping({"/**"})
    public ResponseEntity<Object> mockResponse() throws IOException {

        if (!fileQueue.isEmpty()) {
            fileName = fileQueue.remove();
        }
        if (!new File(fileName).exists()) {
            Map<String, String> map = new HashMap<>();
            map.put("error", "No file exists with the name " + fileName + " at the same location as the jar.");
            return ResponseEntity.badRequest().body(map);
        }
        BufferedReader bodyBufferedReader = new BufferedReader(new FileReader(fileName));
        String bodyFromFile = bodyBufferedReader.lines().collect(Collectors.joining());

        int status = 200;
        if (!statusQueue.isEmpty()) {
            status = statusQueue.remove();
        } else {
            File file = new File("status.txt");
            if (file.exists()) {
                BufferedReader statusBufferedReader = new BufferedReader(new FileReader("status.txt"));
                String statusString = statusBufferedReader.readLine();
                status = Integer.parseInt(statusString);
            }
        }
        System.out.printf("Serving content of file %s.%n", fileName);
        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.status(status).body(mapper.readTree(bodyFromFile));
    }
}
