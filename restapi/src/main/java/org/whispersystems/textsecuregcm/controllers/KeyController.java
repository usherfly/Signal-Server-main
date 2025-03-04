package org.whispersystems.textsecuregcm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.whispersystems.textsecuregcm.models.*;
import org.whispersystems.textsecuregcm.services.KeyService;

import java.util.List;

@RestController
@RequestMapping("/v1/keys")
public class KeyController {

    @Autowired
    private KeyService keyService;

    // 获取预密钥包
    @GetMapping("/{accountId}/prekey")
    public ResponseEntity<PreKeyBundle> getPreKeyBundle(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId) {
        return ResponseEntity.ok(keyService.getPreKeyBundle(accountId, deviceId));
    }

    // 获取预密钥数量
    @GetMapping("/{accountId}/prekey/count")
    public ResponseEntity<Integer> getPreKeyCount(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId) {
        return ResponseEntity.ok(keyService.getPreKeyCount(accountId, deviceId));
    }

    // 设置预密钥
    @PutMapping("/{accountId}/prekey")
    public ResponseEntity<Void> setPreKeys(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId,
            @RequestBody List<PreKey> preKeys) {
        keyService.setPreKeys(accountId, deviceId, preKeys);
        return ResponseEntity.ok().build();
    }

    // 设置签名预密钥
    @PutMapping("/{accountId}/signed-prekey")
    public ResponseEntity<Void> setSignedPreKey(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId,
            @RequestBody SignedPreKey signedPreKey) {
        keyService.setSignedPreKey(accountId, deviceId, signedPreKey);
        return ResponseEntity.ok().build();
    }

    // 设置 KEM 预密钥
    @PutMapping("/{accountId}/kem-prekey")
    public ResponseEntity<Void> setKemPreKey(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId,
            @RequestBody KEMSignedPreKey kemPreKey) {
        keyService.setKemPreKey(accountId, deviceId, kemPreKey);
        return ResponseEntity.ok().build();
    }

    // 获取密钥透明度证明
    @GetMapping("/{accountId}/transparency-proof")
    public ResponseEntity<KeyTransparencyProof> getKeyTransparencyProof(
            @PathVariable String accountId) {
        return ResponseEntity.ok(keyService.getKeyTransparencyProof(accountId));
    }

    // 验证密钥透明度证明
    @PostMapping("/{accountId}/verify-transparency")
    public ResponseEntity<Boolean> verifyKeyTransparency(
            @PathVariable String accountId,
            @RequestBody KeyTransparencyProof proof) {
        return ResponseEntity.ok(keyService.verifyKeyTransparency(accountId, proof));
    }

    // 发送加密消息
    @PostMapping("/{accountId}/message")
    public ResponseEntity<Void> sendEncryptedMessage(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId,
            @RequestBody EncryptedMessage message) {
        keyService.sendEncryptedMessage(accountId, deviceId, message);
        return ResponseEntity.ok().build();
    }

    // 接收加密消息
    @GetMapping("/{accountId}/message")
    public ResponseEntity<List<EncryptedMessage>> receiveEncryptedMessages(
            @PathVariable String accountId,
            @RequestParam(defaultValue = "1") int deviceId) {
        return ResponseEntity.ok(keyService.receiveEncryptedMessages(accountId, deviceId));
    }
} 