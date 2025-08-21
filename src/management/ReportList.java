package management;

import java.io.*;
import java.util.*;

public class ReportList {
    public static void show() throws Exception {
        System.out.println("\n[제보 리스트 화면]");

        File folder = new File("report");

        // 암호화된 제보파일만 필터링 후 파일리스트 보여주기        
        String[] fileNames = folder.list();
        if (fileNames == null || fileNames.length == 0) {
            System.out.println("제보된 내용이 없습니다.");
            return;
        }

        List<String> titles = new ArrayList<>();
        for (String name : fileNames) {
            if (name.endsWith("_cipher.bin")) {
                String title = name.substring(0, name.indexOf("_cipher.bin"));
                titles.add(title);
            }
        }

        if (titles.isEmpty()) {
            System.out.println("제보된 내용이 없습니다.");
            return;
        }

        // 제목 목록 출력
        System.out.println("확인할 제보를 선택하세요:");
        for (int i = 0; i < titles.size(); i++) {
            System.out.println((i + 1) + ". " + titles.get(i));
        }

        // 사용자 입력 처리
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("번호 입력: ");
            int choice = sc.nextInt();
            sc.nextLine(); // 개행 처리

            if (choice < 1 || choice > titles.size()) {
                System.out.println("❌ 잘못된 선택입니다.");
                ManagerMain.main(null);
                return;
            }

            String selectedTitle = titles.get(choice - 1);
            String cipherFile = "report/" + selectedTitle + "_cipher.bin";
            String sealedFile = "report/" + selectedTitle + "_sealed.bin";

            Verification.verify(cipherFile, sealedFile);
        }
    }
}
