package com.example.project;

public class GeminiRequest {

    public Content[] contents;

    public GeminiRequest(String prompt) {
        contents = new Content[]{
                new Content("user", new Part[]{new Part(prompt)})
        };
    }

    static class Content {
        public String role;
        public Part[] parts;
        Content(String role, Part[] parts) {
            this.role = role;
            this.parts = parts;
        }
    }

    static class Part {
        public String text;
        Part(String text) {
            this.text = text;
        }
    }
}
