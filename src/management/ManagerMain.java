package management;

import java.util.Scanner;

public class ManagerMain {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n[관리자 메인 화면]");
        System.out.println("1. 제보 내용 확인하기");
        System.out.println("2. 종료");
        System.out.print("선택: ");

        int choice = sc.nextInt();
        switch (choice) {
        case 1:
            ReportList.show();
            break;
        case 2:
            System.out.println("프로그램을 종료합니다.");
            break;
        default:
            System.out.println("다시 선택해주세요.");
            break;
        }
        
    }
}
