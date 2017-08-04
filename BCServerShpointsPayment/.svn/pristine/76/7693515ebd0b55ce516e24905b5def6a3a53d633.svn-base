package bc.core.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

public class CertUtils {

	private static final String JKS = "JKS";
	private static final String P12 = "P12";
	private static final String PKCS12 = "PKCS12";
	private static final String JCEKS = "JCEKS";
	private static final String JCK = "JCK";
	private static final String PFX = "PFX";

	// private static Logger log = Logger.getLogger(CertUtils.class);
	//
	// private static final Config config = Config.getConfig();
	// private static String mercCertPath = config.getClientCertPath();
	// private static String serCertPath = config.getServeKeyPath();
	// private static String serCertPass = config.getServeKeyPWD();
	// String mercCert = mercCertPath + mercID + ".cer";
	// log.debug("mercCert   --: " + mercCert);
	// log.debug("serCertPath--: " + serCertPath);

	/**
	 * 获取信息摘要
	 * 
	 * @param textBytes
	 *            原信息
	 * @param algorithm
	 *            算法
	 * @return 返回该算法的信息摘要
	 * @throws Exception
	 */
	public static byte[] msgDigest(byte[] textBytes, String algorithm)
			throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.update(textBytes);
		return messageDigest.digest();
	}

	/**
	 * 通过证书获取公钥
	 * 
	 * @param certPath
	 *            证书的路径
	 * @return 返回公钥
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String certPath) throws Exception {
		Certificate cert = getCert(certPath);
		return cert.getPublicKey();
	}

	public static Certificate getCert(String certPath) throws Exception {
		InputStream streamCert = new FileInputStream(certPath);
		CertificateFactory factory = CertificateFactory.getInstance("X.509");
		Certificate cert = factory.generateCertificate(streamCert);
		return cert;
	}

	public static byte[] getCertBase64(String certPath) throws Exception {
		Certificate cert = getCert(certPath);
		byte[] encoded = Base64.encode(cert.getEncoded());
		return encoded;
	}

	/**
	 * 通过密钥文件或者证书文件获取私钥
	 * 
	 * @param keyPath
	 *            密钥文件或者证书的路径
	 * @param passwd
	 *            密钥文件或者证书的密码
	 * @return 返回私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String keyPath, String passwd)
			throws Exception {

		String keySuffix = keyPath.substring(keyPath.lastIndexOf(".") + 1);

		String keyType = JKS;
		if (keySuffix == null || keySuffix.trim().equals(""))
			keyType = JKS;
		else
			keySuffix = keySuffix.trim().toUpperCase();

		if (keySuffix.equals(P12)) {
			keyType = PKCS12;
		} else if (keySuffix.equals(PFX)) {
			keyType = PKCS12;
		} else if (keySuffix.equals(JCK)) {
			keyType = JCEKS;
		} else
			keyType = JKS;

		return getPrivateKey(keyPath, passwd, keyType);

	}

	/**
	 * 通过证书或者密钥文件获取私钥
	 * 
	 * @param keyPath
	 *            证书或者密钥文件
	 * @param passwd
	 *            密钥保存密码
	 * @param keyType
	 *            密钥保存类型
	 * @return 返回私钥
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String keyPath, String passwd,
			String keyType) throws Exception {

		KeyStore ks = KeyStore.getInstance(keyType);
		char[] cPasswd = passwd.toCharArray();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(keyPath);
			ks.load(fis, cPasswd);
			fis.close();
		} finally {
			if (fis != null) {
				fis.close();
				fis = null;
			}
		}
		Enumeration aliasenum = ks.aliases();
		String keyAlias = null;
		PrivateKey key = null;
		while (aliasenum.hasMoreElements()) {
			keyAlias = (String) aliasenum.nextElement();
			key = (PrivateKey) ks.getKey(keyAlias, cPasswd);
			if (key != null)
				break;
		}
		return key;
	}

	/**
	 * 使用私钥签名
	 * 
	 * @param key
	 *            私钥
	 * @param b
	 *            需要签名的byte 数组
	 * @return 返回签名后的byte
	 * @throws Exception
	 */
	public static byte[] sign(PrivateKey priKey, byte[] b) throws Exception {
		// try{

		System.out.println("算法：" + priKey.getAlgorithm());
		Signature sig = Signature.getInstance("SHA1withRSA");
		// }catch(NoSuchAlgorithmException e){
		// System.out.println("错误："+e.getMessage());
		// }
		sig.initSign(priKey);
		sig.update(b);
		return sig.sign();
	}

	/**
	 * 使用公钥验证
	 * 
	 * @param pubKey
	 *            公钥
	 * @param orgByte
	 *            原始数据byte 数组
	 * @param signaByte
	 *            签名后的数据byte 数组
	 * @return 是否验证结果
	 * @throws Exception
	 */
	public static boolean verify(PublicKey pubKey, byte[] orgByte,
			byte[] signaByte) throws Exception {
		Signature sig = Signature.getInstance("SHA1withRSA");
		sig.initVerify(pubKey);
		sig.update(orgByte);
		return sig.verify(signaByte);
	}

	/**
	 * 使用公钥加密
	 * 
	 * @param pubKey
	 *            公钥
	 * @param plainText
	 *            需要加密的 byte 数组
	 * @return 返回加密的 byte 数据
	 * @throws Exception
	 */
	// public static byte[] keyEncode(Key pubKey, byte[] plainText)
	// throws Exception {
	// return doFinal(pubKey, plainText, Cipher.ENCRYPT_MODE);
	// }

	/**
	 * 使用私钥解密
	 * 
	 * @param priKey
	 *            私钥
	 * @param encrypText
	 *            需要解密的 byte 数组
	 * @return 返回解密的 byte 数据
	 * @throws Exception
	 */

	// public static byte[] keyDecode(Key priKey, byte[] encrypText)
	// throws Exception {
	// return doFinal(priKey, encrypText, Cipher.DECRYPT_MODE);
	// }

	// private static byte[] doFinal(Key key, byte[] textBytes, int MODE)
	// throws Exception {
	// Cipher cipher =
	// Cipher.getInstance(key.getAlgorithm(), new BouncyCastleProvider());
	//
	// cipher.init(MODE, key);
	// int blockSize = cipher.getBlockSize();
	// int textLength = textBytes.length;
	//
	// byte[] retBytes = new byte[0];
	// int loop = textLength / blockSize;
	// int mod = textLength % blockSize;
	// if (loop == 0)
	// return cipher.doFinal(textBytes);
	// for (int i = 0; i < loop; i++) {
	// byte[] dstBytes = new byte[blockSize];
	// System.arraycopy(textBytes, i * blockSize, dstBytes, 0, blockSize);
	// byte[] encryBytes = cipher.doFinal(dstBytes);
	// retBytes = Utils.appendArray(retBytes, encryBytes);
	// }
	// if (mod != 0) {
	// int iPos = loop * blockSize;
	// int leavingLength = textLength - iPos;
	// byte[] dstBytes = new byte[leavingLength];
	// System.arraycopy(textBytes, iPos, dstBytes, 0, leavingLength);
	// byte[] encryBytes = cipher.doFinal(dstBytes);
	// retBytes = Utils.appendArray(retBytes, encryBytes);
	// }
	// return retBytes;
	//
	// }

	// public static String[] encodePaRes(String mercID, byte[] reqByte) {
	//
	// log.debug("encodePaRes-----mertID:" + mercID);
	// String mercCert = mercCertPath + mercID + ".cer";
	// log.debug("encodePaRes mercCert   --: " + mercCert);
	// log.debug("encodePaRes serCertPath--: " + serCertPath);
	// String[] retStrs = new String[2];
	// try {
	// PublicKey pubKey = CertUtils.getPublicKey(mercCert);
	//
	// PrivateKey priKey =
	// CertUtils.getPrivateKey(serCertPath, serCertPass);
	//
	// //加密
	// byte[] keyBytes = CertUtils.keyEncode(pubKey, reqByte);
	//
	// //摘要
	// byte[] digestBytes = CertUtils.msgDigest(keyBytes, "MD5");
	//
	// //对信息摘要签名
	// byte[] signBytes = CertUtils.sign(priKey, digestBytes);
	//
	// // 转换加密信息为BASE64
	// byte[] paResByte = Base64.encode(keyBytes);
	// retStrs[0] = new String(paResByte, "UTF-8");
	//
	// //转换签名信息为BASE64
	// byte[] paResSignByte = Base64.encode(signBytes);
	// retStrs[1] = new String(paResSignByte, "UTF-8");
	//
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// return retStrs;
	// }

	// public static String decodePaReq(
	// String mercID,
	// byte[] reqByte,
	// byte[] singByte) {
	// log.debug("decodePaReq-----mertID:" + mercID);
	// String mercCert = mercCertPath + mercID + ".cer";
	// log.debug("decodePaReq mercCert   --: " + mercCert);
	// log.debug("decodePaReq serCertPath--: " + serCertPath);
	// String respXML = "";
	// try {
	//
	// PublicKey pubKey = CertUtils.getPublicKey(mercCert);
	// PrivateKey priKey =
	// CertUtils.getPrivateKey(serCertPath, serCertPass);
	//
	// //BASE64解码
	// byte[] paReqEncryp = Base64.decode(reqByte);
	//
	// //收到的信息摘要
	// byte[] signByte = Base64.decode(singByte);
	//
	// //收到加密信息的摘要
	// byte[] orgMsgDigestByte = CertUtils.msgDigest(paReqEncryp, "MD5");
	//
	// //验证签名
	// boolean isVerify =
	// CertUtils.verify(pubKey, orgMsgDigestByte, signByte);
	// if (!isVerify) { //如果没验证通过。返回信息
	// //
	// return null;
	// }
	// //解密
	// byte[] paReqByte = CertUtils.keyDecode(priKey, paReqEncryp);
	// respXML = new String(paReqByte, "UTF8");
	//
	// } catch (Exception e) {
	// return null;
	// }
	// return respXML;
	// }

	public static void main(String[] args) throws Exception {
		// Security.addProvider(new BouncyCastleProvider());
		// Security.addProvider(new IBMJSSEProvider());

		// String serverPubCert = "D:/cert/server.cer";
		String clientPriKey = "D:/cert/test.pfx";// "D:/cert/clientPri.pfx";

		String clientPubCert = "D:/cert/server.cer";
		// String serverPriKey = "D:/cert/server.pfx";

		String cliPasswd = "123456";
		String serPasswd = "123456";

		String orgStr = "600000000000032    410002600005000000000030020090808010212000";
		// "000000000000012    "
		// + "200001"
		// + "200006"
		// + "4033920001116385   "
		// +"0000000000100"+"2008091234123    ";
		// ----------------客户端--------------------
		// 用服务器公钥加密
		// PublicKey pubKey = getPublicKey(serverPubCert);
		// System.out.println(orgStr.getBytes("UTF8").length);
		// byte[] encrypted = keyEncode(pubKey, orgStr.getBytes("UTF8"));
		// SecureRandom sr = new SecureRandom();
		// String encryBase64 = new String(Base64.encode(encrypted), "UTF8");
		// System.out.println("原数据加密后的BASE64   : " + encryBase64);
		//
		// //获取加密后的信息摘要
		// byte[] msgDigestBytes = msgDigest(encrypted, "MD5");
		// String msgDigestBase64 =
		// new String(Base64.encode(msgDigestBytes), "UTF8");
		// System.out.println("原数据加密的信息摘要   : " + msgDigestBase64);
		//
		// //用客户端私钥签名
		// PrivateKey priKey = getPrivateKey(clientPriKey, cliPasswd);
		// byte[] signByte = sign(priKey, msgDigestBytes);
		//
		// //加密，签名后的BASE64
		//
		// String signBase64 = new String(Base64.encode(signByte), "UTF8");
		// System.out.println("信息摘要签名后的BASE64 : " + signBase64);
		//
		// //----------------服务器端--------------------
		// //服务器收到的BASE64支付串
		// byte[] orgByteBase64 = Base64.decode(encryBase64.getBytes("UTF8"));
		// byte[] signByteBase64 = Base64.decode(signBase64.getBytes("UTF8"));
		//
		// // 获取收到加密后的信息摘要
		// byte[] msgDigestBytes1 = msgDigest(orgByteBase64, "MD5");
		// String msgDigest1 = new String(Base64.encode(msgDigestBytes1),
		// "UTF8");
		// System.out.println("收到原数据的信息摘要   : " + msgDigest1);
		//
		// // 用客户端的公钥验证信息
		// PublicKey pubKey2 = getPublicKey(clientPubCert);
		// boolean isVerify = verify(pubKey2, msgDigestBytes1, signByteBase64);
		// System.out.println("签名验证结果           : " + isVerify);
		//
		// //用服务器端的私钥解密
		// PrivateKey priKey2 = getPrivateKey(serverPriKey, serPasswd);
		// byte[] orgStrByte = keyDecode(priKey2, orgByteBase64);
		// System.out.println("未加密原始信息         : " + orgStr);
		// System.out.println(
		// "解密后的信息           : " + new String(orgStrByte, "UTF8"));

		// 用客户端私钥签名
		PrivateKey priKey = getPrivateKey(clientPriKey, cliPasswd);

		// KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		// KeyPair key = gen.generateKeyPair();
		// priKey = key.getPrivate();

		byte[] testByte = sign(priKey, orgStr.getBytes("UTF8"));
		String testBase64 = new String(Base64.encode(testByte), "UTF8");
		// String testBase64 =
		// "TujtB6cpWUhwwFzCe1PxSjTQB+G9hyNqk8s92zc34YFdvkybrloLeslndEYrSOvDF5E5rzdp7sshQXL7j+M6bM2Kqn8EsaHldc+DSGe7P1wh63y82ZTQy85CQR+fRbFXr2S0BrXNxJtL9w0+xAI422HlJi5ucwjgIsab7Mmid8c=";
		System.out.println("测试签名结果           : " + testBase64);
		testBase64 = new String(testBase64.getBytes(), "GBK");

		byte[] signtestBase64 = Base64.decode(testBase64.getBytes("UTF8"));

		// 用客户端的公钥验证信息
		PublicKey pubKey = getPublicKey(clientPubCert);
		// pubKey = key.getPublic();
		boolean isv = verify(pubKey, orgStr.getBytes("UTF8"), signtestBase64);
		System.out.println("测试签名验证结果           : " + isv);

	}
}
