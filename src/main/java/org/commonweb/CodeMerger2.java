package org.commonweb;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class CodeMerger2 {

    public static void main(String[] args) throws IOException {
        String[] srcDirs = {
                "C:\\Users\\sgsg9\\Desktop\\MyPJ\\my-vue-app\\src" // 프로젝트의 vue 소스 코드 디렉토리 경로
        };
        String outputFile = "C:\\Users\\sgsg9\\Desktop\\MyPJCodeText\\MyPJVueText.txt"; // 결과를 저장할 파일 경로

        List<String> allLines = new ArrayList<>();

        for (String srcDir : srcDirs) {
            try {
                Files.walkFileTree(Paths.get(srcDir), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // JavaScript 와 Json 과 VUE 소스 파일을 대상으로 함
                        if (file.toString().endsWith(".vue")
                                || file.toString().endsWith(".json")
                                || file.toString().endsWith(".js")) {
                            allLines.add("/* File: " + file.toString() + " */"); // 파일 이름 추가
                            allLines.addAll(Files.readAllLines(file));
                            allLines.add("\n"); // 파일 간 구분을 위해 빈 줄 추가
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.write(Paths.get(outputFile), allLines, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
