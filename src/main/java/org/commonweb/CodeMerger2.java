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

        for(String srcDir : srcDirs) {
            Files.walkFileTree(Paths.get(srcDir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Java와 리액트 소스 파일을 모두 대상으로 함
                    if (file.toString().endsWith(".vue")
                            || file.toString().endsWith(".js")) {
                        allLines.addAll(Files.readAllLines(file));
                        allLines.add("\n"); // 파일 간 구분을 위해 빈 줄 추가
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }

        Files.write(Paths.get(outputFile), allLines, StandardOpenOption.CREATE);
    }
}
