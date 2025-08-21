package management;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MyKeyPair {
	private static final String KeyAlgorithm = "RSA";
	private KeyPairGenerator keyGen;
	private KeyPair pair;
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public static MyKeyPair getInstance(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		MyKeyPair rslt = new MyKeyPair();
		
		rslt.keyGen = KeyPairGenerator.getInstance(KeyAlgorithm);
		rslt.keyGen.initialize(keylength);
		
		return rslt;
	}
	
	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}
	
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}
	
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	public void saveKeys(String privatePath, String publicPath) throws IOException {
	    try (ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(privatePath))) {
	        oos1.writeObject(privateKey);
	    }

	    try (ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(publicPath))) {
	        oos2.writeObject(publicKey);
	    }
	}

	
}