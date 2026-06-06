package com.micaelatamir.smartapi.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TranscriptionService {

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscriptionService(OpenAiAudioTranscriptionModel transcriptionModel) {
        this.transcriptionModel = transcriptionModel;
    }

    public String transcribe(MultipartFile file) throws IOException {
        Resource audio = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                String original = file.getOriginalFilename();
                return (original != null && !original.isBlank()) ? original : "audio.mp3";
            }
        };

        return transcriptionModel.call(new AudioTranscriptionPrompt(audio))
                .getResult()
                .getOutput();
    }
}
