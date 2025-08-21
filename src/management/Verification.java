// Verification.java
package management;

import java.io.*;
import java.security.*;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Verification {
    public static boolean verify(String cipherPath, String sealedKeyPath) throws Exception {
        // 관리자 개인키 가져오기
        PrivateKey adminPrivateKey;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("key/admin_private.key"))) {
            adminPrivateKey = (PrivateKey) ois.readObject();
        }

        // 전자봉투 복호화
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, adminPrivateKey);
        
        byte[] secretKeyBytes;
        try (CipherInputStream cis = new CipherInputStream(new FileInputStream(sealedKeyPath), rsaCipher)) {
            secretKeyBytes = cis.readAllBytes();
        }
        
        // 복호화된 byte를 비밀키로 변경하기
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, 0, secretKeyBytes.length, "AES");

        // 암호문 복호화
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);

        // 복호화된 암호문의 내용들을 역직렬화
        try (CipherInputStream cis = new CipherInputStream(new FileInputStream(cipherPath), aesCipher);
             ObjectInputStream ois = new ObjectInputStream(cis)) {

            String title = (String) ois.readObject();
            String content = (String) ois.readObject();
            byte[] attachedFile = (byte[]) ois.readObject();
            byte[] signature = (byte[]) ois.readObject();
            PublicKey writerPublicKey = (PublicKey) ois.readObject();

            // 원문을 해시값 생성
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(title.getBytes());
            baos.write(content.getBytes());
            baos.write(attachedFile);
            byte[] hash = hashFunction.hash256(baos.toByteArray());

            // 서명 검증
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(writerPublicKey);
            sig.update(hash);

            // 원문 해시와 전자서명된 해시값을 비교
            boolean rslt = sig.verify(signature);

            System.out.println("\n[검증 결과]");
            
            //검증결과 출력하기
            if (rslt) {
                System.out.println("✅ 위조 체크 완료.");
                System.out.println("[제목] " + title);
                System.out.println("[내용] " + content);
                if (attachedFile.length > 0) {
                    System.out.println("[파일 첨부 내용] ");
                    System.out.println(new String(attachedFile));
                }
                System.out.println("\n1. 뒤로가기");

                try (Scanner sc = new Scanner(System.in)) {
                    int choice = sc.nextInt();
                    if (choice == 1) {
                        ReportList.show();
                    }
                }
            } else {
                System.out.println("❌ 위조되었을 가능성이 있습니다. 열람이 불가합니다.");
                System.out.println("\n1. 뒤로가기");
                try (Scanner sc = new Scanner(System.in)) {
                    int choice = sc.nextInt();
                    if (choice == 1) {
                        ReportList.show();
                    }
                }
            }
        }
        return false;
    }
}
