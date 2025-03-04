package org.whispersystems.textsecuregcm.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreKeyBundle {
    @JsonProperty("identity_key")
    private String identityKey;
    
    @JsonProperty("device_id")
    private int deviceId;
    
    @JsonProperty("pre_key")
    private PreKey preKey;
    
    @JsonProperty("signed_pre_key")
    private SignedPreKey signedPreKey;
    
    @JsonProperty("pq_pre_key")
    private KEMSignedPreKey pqPreKey;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreKey {
    @JsonProperty("key_id")
    private int keyId;
    
    @JsonProperty("public_key")
    private String publicKey;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignedPreKey {
    @JsonProperty("key_id")
    private int keyId;
    
    @JsonProperty("public_key")
    private String publicKey;
    
    @JsonProperty("signature")
    private String signature;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KEMSignedPreKey {
    @JsonProperty("key_id")
    private int keyId;
    
    @JsonProperty("public_key")
    private String publicKey;
    
    @JsonProperty("signature")
    private String signature;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyTransparencyProof {
    @JsonProperty("account_id")
    private String accountId;
    
    @JsonProperty("key_history")
    private List<KeyHistoryEntry> keyHistory;
    
    @JsonProperty("proof")
    private Proof proof;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyHistoryEntry {
    @JsonProperty("identity_key")
    private String identityKey;
    
    @JsonProperty("timestamp")
    private long timestamp;
    
    @JsonProperty("reason")
    private String reason;
    
    @JsonProperty("signature")
    private String signature;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proof {
    @JsonProperty("merkle_root")
    private String merkleRoot;
    
    @JsonProperty("inclusion_proof")
    private List<String> inclusionProof;
    
    @JsonProperty("timestamp")
    private long timestamp;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncryptedMessage {
    @JsonProperty("type")
    private int type;
    
    @JsonProperty("destination_device_id")
    private int destinationDeviceId;
    
    @JsonProperty("destination_registration_id")
    private int destinationRegistrationId;
    
    @JsonProperty("content")
    private String content;
    
    @JsonProperty("timestamp")
    private long timestamp;
} 