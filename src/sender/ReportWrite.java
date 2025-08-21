package sender;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportWrite {
	private static final String REPORT_DIR = "report/";
    private static final String CIPHER_SUFFIX = "_cipher.bin";
    private static final String SEALED_SUFFIX = "_sealed.bin";
    private static final String ADMIN_PUBLIC_KEY_PATH = "key/admin_public.key";
    
    public static void writeReport(KeyPair writerKeyPair, SecretKey secretKey) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n[익명 제보 작성]");
        System.out.print("제목: ");
        String title = sc.nextLine();

        System.out.print("내용: ");
        String content = sc.nextLine();

        System.out.print("파일 첨부(파일명 입력): ");
        String filename = sc.nextLine();

        // 파일 내용 읽기
        byte[] fileContent;
        try {
        	// 첨부파일을 바이트로 읽어옴
            fileContent = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("첨부파일 없음");
            fileContent = new byte[0]; // 빈 byte 배열
        }

        // 제목+내용+파일을 byte로 변환 -> 해싱 -> 전자서명 준비
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(title.getBytes());
        baos.write(content.getBytes());
        baos.write(fileContent);
        
        // 해시 생성
        byte[] hash = hashFunction.hash256(baos.toByteArray());

        // 전자서명 생성
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(writerKeyPair.getPrivate());
        signature.update(hash);
        byte[] digiSig = signature.sign();


        // 모두 묶어서 하나의 바이트로 암호화
        byte[] totalData;
        try (ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            // 객체를 직렬화
            ObjectOutputStream oos = new ObjectOutputStream(baos2)) {
            oos.writeObject(title);
            oos.writeObject(content);
            oos.writeObject(fileContent);
            oos.writeObject(digiSig);
            oos.writeObject(writerKeyPair.getPublic());
            oos.flush();
            
            // 직렬화된 전체 데이터를 바이트로 꺼내오기
            totalData = baos2.toByteArray();
        }

        // AES 암호화
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);        
//        byte[] encryptedData = aesCipher.doFinal(totalData);

        // 저장 경로 지정
        String cipherFile = REPORT_DIR + title + CIPHER_SUFFIX;
        String sealedFile = REPORT_DIR + title + SEALED_SUFFIX;

    
        // 암호화된 제보 데이터 저장
        try (FileOutputStream fos = new FileOutputStream(cipherFile);
       	     CipherOutputStream cos = new CipherOutputStream(fos, aesCipher)) {
       	    cos.write(totalData); // 암호화하면서 저장
       	}
        
        // 관리자 공개키 불러오기
        PublicKey adminPublicKey;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ADMIN_PUBLIC_KEY_PATH))) {
            adminPublicKey = (PublicKey) ois.readObject();
        }

        // 비밀키 암호화 (전자봉투 생성)
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, adminPublicKey);
//        byte[] sealedKey = rsaCipher.doFinal(secretKey.getEncoded());

    
        // 파일 저장
//        try (FileOutputStream fos1 = new FileOutputStream(cipherFile);
//             FileOutputStream fos2 = new FileOutputStream(sealedFile)) {
//            fos1.write(encryptedData);
//            fos2.write(sealedKey);
//        }



        try (FileOutputStream fos = new FileOutputStream(sealedFile);
        	     CipherOutputStream cos = new CipherOutputStream(fos, rsaCipher)) {
        	    cos.write(secretKey.getEncoded());
        	}
        	
        System.out.println("\n[제보가 성공적으로 제출되었습니다]");
        ReportComplete.show();
    }
}