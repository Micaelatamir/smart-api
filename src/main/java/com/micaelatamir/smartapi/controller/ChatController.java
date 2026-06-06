package com.micaelatamir.smartapi.controller;

import com.micaelatamir.smartapi.service.AssistantService;
import com.micaelatamir.smartapi.service.TranscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final AssistantService assistantService;
    private final TranscriptionService transcriptionService;

    @PostMapping
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        String message = body.get("message");
        String response = assistantService.chat(message);
        return ResponseEntity.ok(Map.of("response", response));
    }

    // Recebe audio, transcreve via Groq Whisper e manda pro assistente
    @PostMapping("/transcribe")
    public ResponseEntity<Map<String, String>> transcribe(@RequestParam("file") MultipartFile file) throws IOException {
        String transcription = transcriptionService.transcribe(file);
        String aiResponse = assistantService.chat(transcription);
        return ResponseEntity.ok(Map.of(
                "transcription", transcription,
                "response", aiResponse
        ));
    }
}
