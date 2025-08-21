package sender;

import java.io.Serializable;
import java.security.PublicKey;

public class ReportData implements Serializable  {

	// 작성자 글 + 서명 + 공개키를 담는 객체 생성
	private String title;
    private String content;
    private byte[] signature;
    private PublicKey writerPublicKey;
    
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public PublicKey getWriterPublicKey() {
		return writerPublicKey;
	}
	public void setWriterPublicKey(PublicKey writerPublicKey) {
		this.writerPublicKey = writerPublicKey;
	}
	

}
