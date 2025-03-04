package org.whispersystems.textsecuregcm.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whispersystems.textsecuregcm.models.*;
import org.whispersystems.textsecuregcm.services.KeyService;
import org.whispersystems.textsecuregcm.storage.KeysManager;
import org.whispersystems.textsecuregcm.storage.KeyTransparencyManager;

import java.util.List;

@Service
public class KeyServiceImpl implements KeyService {

    @Autowired
    private KeysManager keysManager;

    @Autowired
    private KeyTransparencyManager keyTransparencyManager;

    @Override
    public PreKeyBundle getPreKeyBundle(String accountId, int deviceId) {
        // 获取预密钥包
        return keysManager.getPreKeyBundle(accountId, deviceId);
    }

    @Override
    public int getPreKeyCount(String accountId, int deviceId) {
        // 获取预密钥数量
        return keysManager.getPreKeyCount(accountId, deviceId);
    }

    @Override
    public void setPreKeys(String accountId, int deviceId, List<PreKey> preKeys) {
        // 设置预密钥
        keysManager.storeECOneTimePreKeys(accountId, deviceId, preKeys);
    }

    @Override
    public void setSignedPreKey(String accountId, int deviceId, SignedPreKey signedPreKey) {
        // 设置签名预密钥
        keysManager.storeECSignedPreKey(accountId, deviceId, signedPreKey);
    }

    @Override
    public void setKemPreKey(String accountId, int deviceId, KEMSignedPreKey kemPreKey) {
        // 设置 KEM 预密钥
        keysManager.storeKemOneTimePreKeys(accountId, deviceId, List.of(kemPreKey));
    }

    @Override
    public KeyTransparencyProof getKeyTransparencyProof(String accountId) {
        // 获取密钥透明度证明
        return keyTransparencyManager.getProof(accountId);
    }

    @Override
    public boolean verifyKeyTransparency(String accountId, KeyTransparencyProof proof) {
        // 验证密钥透明度证明
        return keyTransparencyManager.verifyProof(accountId, proof);
    }

    @Override
    public void sendEncryptedMessage(String accountId, int deviceId, EncryptedMessage message) {
        // 发送加密消息
        keysManager.sendEncryptedMessage(accountId, deviceId, message);
    }

    @Override
    public List<EncryptedMessage> receiveEncryptedMessages(String accountId, int deviceId) {
        // 接收加密消息
        return keysManager.receiveEncryptedMessages(accountId, deviceId);
    }
} 