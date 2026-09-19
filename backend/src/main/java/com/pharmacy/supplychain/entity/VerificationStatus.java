package com.pharmacy.supplychain.entity;

/**
 * Represents the verification status of a medicine batch in the supply chain.
 * Used to safeguard the hospital against counterfeit or compromised stock.
 */
public enum VerificationStatus {
    UNVERIFIED,
    VERIFIED,
    REJECTED
}
