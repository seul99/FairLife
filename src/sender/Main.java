package sender;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.security.*;
import javax.crypto.*;


public class Main {

	public static KeyPair writerKeyPair;
    public static SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        System.out.println("=== FAIRLIFE ===");
        System.out.println("1. 지금 바로 제보하러 가기");
        System.out.println("2. 종료");

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("번호를 입력하세요: ");
            int choice = sc.nextInt();
            
            if (choice == 1) {
                // 작성자용 RSA 키쌍 생성
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(2048);
                writerKeyPair = keyGen.generateKeyPair();

                // 대칭키(AES) 생성 (비밀키)
                KeyGenerator aesGen = KeyGenerator.getInstance("AES");
                aesGen.init(128);
                secretKey = aesGen.generateKey();

                // 글 작성 화면으로 이동, 키쌍과 비밀키를 전달
                ReportWrite.writeReport(writerKeyPair, secretKey);
            }
            
        } catch (InputMismatchException e) {
            System.out.println("잘못된 입력입니다.");
            System.exit(1);
        }
        
        
    }

}
