package org.commonweb;

// Shift을(를) 두 번 눌러 전체 검색 대화상자를 열고 'show whitespaces'를 입력한 다음,
// Enter를 누르세요. 그러면 코드 내에서 공백 문자를 확인할 수 있습니다.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        // 캐럿을 강조 표시된 텍스트에 놓고 Alt+Enter을(를) 누르면
        // IntelliJ IDEA의 수정 제안을 볼 수 있습니다.
        System.out.println("Hello and welcome!");

        // Shift+F10을(를) 누르거나 여백의 녹색 화살표 버튼을 클릭하여 코드를 실행합니다.
        for (int i = 1; i <= 5; i++) {

            System.out.println("i = " + i);
        }
    }
}