// KeyManager.java
package management;

import java.util.Scanner;

public class KeyManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("[관리자 키 생성기]");
        System.out.println("1. 관리자 키 생성 및 저장");
        System.out.println("2. 종료");
        System.out.print("선택: ");

        int choice = sc.nextInt();

        if (choice == 1) {
            try {
                MyKeyPair keyPair = MyKeyPair.getInstance(1024);
                keyPair.createKeys();
                keyPair.saveKeys("key/admin_private.key", "key/admin_public.key");
                System.out.println("\n관리자 키가 성공적으로 생성되어 저장되었습니다!");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("키 생성에 실패했습니다.");
            }
        } else {
            System.out.println("종료합니다.");
        }
    }
}
