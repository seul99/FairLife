package management;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//클래스 외부 공개 막기
final class hashFunction {
	// 실수로 인스턴스 생성 방지
	private hashFunction() {}
		
	public static byte[] hash256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data);
    }

    public static byte[] hash256(String input) throws NoSuchAlgorithmException {
        return hash256(input.getBytes());
    }
}
