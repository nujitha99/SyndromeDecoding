# SyndromeDecoding

A Java implementation of the **Syndrome Decoding** algorithm — a foundational technique in coding theory for detecting and correcting errors in transmitted data.

## Overview

Every time data is sent across a network, stored on a hard drive, or beamed from a satellite, there is a chance that some bits get flipped or corrupted along the way. **Error-correcting codes** are the mechanism that makes reliable communication possible despite this. They work by adding structured redundancy to data so that a receiver can not only detect that something went wrong, but also figure out exactly what — and fix it.

This project implements **syndrome decoding**, one of the most elegant and widely studied decoding algorithms for **linear block codes**. A linear code encodes messages by multiplying them against a generator matrix, producing codewords with built-in redundancy. Syndrome decoding exploits a companion structure — the **parity-check matrix** — to efficiently identify and correct errors without needing to compare a received word against every possible codeword.

---

## How It Works

When a codeword is transmitted and arrives corrupted, the received word `r` can be written as:

```
r = c + e
```

where `c` is the original codeword and `e` is the error pattern introduced by the channel.

Syndrome decoding works in three steps:

1. **Compute the syndrome** — Multiply the received word by the transpose of the parity-check matrix `H`:
   ```
   s = r · Hᵀ
   ```
   If `s` is all zeros, no error is detected. Otherwise, `s` uniquely identifies the *coset* of the error pattern.

2. **Look up the coset leader** — Before decoding begins, a **standard array** (or syndrome lookup table) is precomputed. Each possible syndrome maps to its *coset leader* — the most likely (lowest-weight) error pattern that would produce that syndrome.

3. **Correct the error** — Subtract the coset leader from the received word to recover the original codeword:
   ```
   c = r - e
   ```

This approach is far more efficient than exhaustive search, especially for codes with large minimum distances.

---

## Why This Matters

### Digital Communications
Every modern communication standard — Wi-Fi (802.11), 4G/5G, Bluetooth, deep-space telemetry — relies on error-correcting codes at its physical layer. Syndrome decoding is the operational backbone behind many of these systems. When your phone reconstructs a dropped packet or a Mars rover sends images back to Earth without corruption, syndrome-style decoding is often at work.

### Data Storage
Hard drives, SSDs, and optical media (CDs, DVDs, Blu-ray) use error-correcting codes to silently fix the bit errors that occur constantly during read/write operations. RAID systems use similar parity-based techniques to recover data from failed drives. Syndrome decoding provides the mathematical framework that makes all of this transparent to the user.

### QR Codes
QR codes use **Reed-Solomon codes**, which are decoded using syndrome-based methods. This is why a QR code can still be scanned even when it is partially torn, dirty, or obscured — the syndrome decoder identifies which symbols are corrupted and reconstructs the original data.

### Cryptography
The hardness of syndrome decoding for random linear codes is the foundation of several **post-quantum cryptographic schemes** (such as code-based cryptosystems like McEliece). The problem of decoding an arbitrary linear code without knowing the structure is believed to be computationally intractable, making it a candidate for securing communications in a future of quantum computers.

### Education and Research
Syndrome decoding is a core topic in any course on information theory or algebraic coding theory. This implementation provides a clean, hands-on reference for understanding how the algorithm operates in practice.

---

## Project Structure

```
SyndromeDecoding/
├── Main.java       # Syndrome decoding algorithm implementation
└── LICENSE         # Apache 2.0 License
```

---

## Prerequisites

- **Java 8** or higher
- A standard JDK installation (no external libraries required)

---

## Getting Started

### Compile

```bash
javac Main.java
```

### Run

```bash
java Main
```

The parity-check matrix, generator matrix, and test received words can be configured directly in `Main.java`.

---

## Key Concepts

| Term | Description |
|------|-------------|
| **Linear Code** | A code where any linear combination of codewords is also a codeword |
| **Generator Matrix (G)** | Used to encode messages into codewords |
| **Parity-Check Matrix (H)** | Used to detect and locate errors; satisfies `G · Hᵀ = 0` |
| **Syndrome** | The result of `r · Hᵀ`; zero for valid codewords, non-zero for corrupted ones |
| **Coset Leader** | The minimum-weight error pattern associated with a given syndrome |
| **Standard Array** | A lookup table mapping each syndrome to its coset leader |

---

## License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for details.

---

## Author

**Nujitha Wickramasurendra**  
M.Sc. Computer Science — Brock University
