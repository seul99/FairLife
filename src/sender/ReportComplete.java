package sender;

import java.util.Scanner;

public class ReportComplete {

	 public static void show() {
        System.out.println("\n[제보 완료 화면]");
        System.out.println("제보가 정상적으로 완료되었습니다! 감사합니다.\n");

        System.out.println("1. 메인 화면으로 돌아가기");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        if (choice == 1) {
            try {
                Main.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sc.close();
       }

}
