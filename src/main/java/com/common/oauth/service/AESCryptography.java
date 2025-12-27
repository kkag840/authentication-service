package com.common.oauth.service;

import com.common.exception.BusinessException;
import com.common.oauth.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AESCryptography {

	private TextEncryptor encryptor;

	private final String password;
	private final String salt;

	@Autowired
	public AESCryptography(@Value("${aes.encryption.password:your_aes_encryption_password}") final String password,
						   @Value("${aes.salt:9d7f1a3c5b8e2f44}") final String salt) {
		this.password = password;
		this.salt = salt;
	}

	@PostConstruct
	public void initIt() {
		this.encryptor = Encryptors.delux(password, salt);
	}

	public String encrypt(String text) {
		try {
			return encryptor.encrypt(text);
		} catch (Exception e) {
			log.error("****  AESCryptography encrypt :: Error while encrypting", e);
			throw new BusinessException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	public String decrypt(String text) {
		try {
			return encryptor.decrypt(text);
		} catch (Exception e) {
			log.error("****  AESCryptography decrypt :: Error while decrypting ****", e);
			throw new BusinessException(ErrorCode.INVALID_TOKEN_FORMAT, HttpStatus.UNAUTHORIZED);
		}
	}

}
