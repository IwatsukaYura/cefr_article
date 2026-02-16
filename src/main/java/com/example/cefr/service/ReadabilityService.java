package com.example.cefr.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReadabilityService {

    public String calculateCefrLevel(String text) {
        if (text == null || text.isBlank()) {
            return "N/A";
        }

        double score = calculateFleschKincaidGradeLevel(text);
        
        // Mapping as per design
        // Grade 0-3: A1/A2
        // Grade 4-8: B1/B2
        // Grade 9+: C1/C2
        
        if (score <= 3.9) {
            return "A2"; // or A1/A2
        } else if (score <= 8.9) {
            return "B2"; // or B1/B2
        } else {
            return "C1"; // or C1/C2
        }
    }

    public double calculateFleschKincaidGradeLevel(String text) {
        int sentences = countSentences(text);
        int words = countWords(text);
        int syllables = countSyllables(text);

        if (sentences == 0 || words == 0) return 0.0;

        // Formula: 0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59
        return 0.39 * ((double) words / sentences) + 11.8 * ((double) syllables / words) - 15.59;
    }

    private int countSentences(String text) {
        // Simple heuristic: split by [.!?] followed by space or end of line
        String[] sentences = text.split("[.!?]+(\\s+|$)");
        return Math.max(1, sentences.length);
    }

    private int countWords(String text) {
        if (text == null || text.isEmpty()) return 0;
        String[] words = text.trim().split("\\s+");
        return words.length;
    }

    private int countSyllables(String text) {
        int count = 0;
        String[] words = text.trim().split("\\s+");
        for (String word : words) {
            count += countSyllablesInWord(word);
        }
        return count;
    }

    private int countSyllablesInWord(String word) {
        word = word.toLowerCase().replaceAll("[^a-z]", "");
        if (word.isEmpty()) return 0;

        if (word.length() <= 3) return 1;

        // Subtract silent 'e'
        if (word.endsWith("e")) {
            word = word.substring(0, word.length() - 1);
        }

        // Count vowel groups
        Pattern pattern = Pattern.compile("[aeiouy]+");
        Matcher matcher = pattern.matcher(word);
        int syllables = 0;
        while (matcher.find()) {
            syllables++;
        }
        
        return Math.max(1, syllables);
    }
}
