package org.commonweb;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ProjectCodeMerger {
    public static void main(String[] args) throws IOException {
        String srcDir = "C:\\Users\\sgsg9\\Desktop\\MyPJ\\src"; // 프로젝트 소스 코드 디렉토리 경로
        String outputFile = "C:\\Users\\sgsg9\\Desktop\\MyPJCodeText\\MyPJText.txt"; // 결과를 저장할 파일 경로

        List<String> allLines = new ArrayList<>();
        Files.walkFileTree(Paths.get(srcDir), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".java")) { // Java 소스 파일만 대상으로 함
                    allLines.addAll(Files.readAllLines(file));
                    allLines.add("\n"); // 파일 간 구분을 위해 빈 줄 추가
                }
                return FileVisitResult.CONTINUE;
            }
        });

        Files.write(Paths.get(outputFile), allLines, StandardOpenOption.CREATE);
    }
}
